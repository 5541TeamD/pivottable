
// action types
const C = {
  FETCH_TABLE_LIST: 'FETCH_TABLE_LIST',
  FETCH_TABLE_LIST_SUCCESS: 'FETCH_TABLE_LIST_SUCCESS',
  FETCH_TABLE_LIST_ERROR: 'FETCH_TABLE_LIST_ERROR',
  TABLE_SELECTED: 'TABLE_SELECTED',
  CONNECT_DATA_SOURCE: 'CONNECT_DATA_SOURCE',
  CONNECT_DATA_SOURCE_SUCCESS: 'CONNECT_DATA_SOURCE_SUCCESS',
  CONNECT_DATA_SOURCE_ERROR: 'CONNECT_DATA_SOURCE_ERROR',
  USERNAME_CHANGED: 'USERNAME_CHANGED',
  PASSWORD_CHANGED: 'PASSWORD_CHANGED',
  DATASOURCE_CHANGED: 'DATASOURCE_CHANGED',
  DISCONNECT: 'DISCONNECT',
  FETCH_RAW_REPORT: 'FETCH_RAW_REPORT',
  FETCH_RAW_REPORT_SUCCESS: 'FETCH_RAW_REPORT_SUCCESS',
  FETCH_RAW_REPORT_ERROR: 'FETCH_RAW_REPORT_ERROR',
  SCHEMA_ROW_LABELS_SELECTED: 'SCHEMA_ROW_LABELS_SELECTED',
  SCHEMA_COLUMN_LABELS_SELECTED: 'SCHEMA_COLUMN_LABELS_SELECTED',
  SCHEMA_PAGE_LABEL_SELECTED: 'SCHEMA_PAGE_LABEL_SELECTED',
  SCHEMA_FUNCTION_SELECTED: 'SCHEMA_FUNCTION_SELECTED',
  SCHEMA_VALUE_SELECTED: 'SCHEMA_VALUE_SELECTED',
  SCHEMA_RESET: 'SCHEMA_RESET',
  GENERATE_PIVOT_TABLE: 'GENERATE_PIVOT_TABLE',
  GENERATE_PIVOT_TABLE_SUCCESS: 'GENERATE_PIVOT_TABLE_SUCCESS',
  GENERATE_PIVOT_TABLE_ERROR: 'GENERATE_PIVOT_TABLE_ERROR',
  PIVOT_TABLE_PAGE_CHANGED: 'PIVOT_TABLE_PAGE_CHANGED',
}

export {C}

// immutable initial state (everything is immutable)
const initialState = {
  connectionLoading: false,
  fetchTableListLoading: false,
  selectedTable: '',
  username: '',
  password: '',
  sourceName: '',
  connectedSuccessfully: false,
  tableList: [],
  rawReportLoading: false,
  rawReport: {
    columns: [],
    rows: []
  }, // data set
  tableSchema: {
    rowLabels: [],
    selectedRowLabels: [],
    columnLabels: [],
    selectedColumnLabels: [],
    pageLabels: [],
    selectedPageLabel: '', //name of the field
    functionList: ['count', 'sum'],
    selectedFunction: '', //name of the function
    possibleValues: [],
    selectedValue: '' //name of the value
  },
  pivotTableLoading: false,
  // 1 pivot table data per page
  pivotTables: [/*{
    rowLabels: [],
    columnLabels: [],
    data: [],
  }*/],
  pageLabels: [],
  pageSelected: -1, // index of the selected page
  infoMessage: '',
  errorMessage: ''
}

/**
 * row label index: 0
 * column label index: 1
 * data index: dataIndex (usually, 2)
 */
const insertIn2D = (rowMap, columnMap, row, dataIndex, data) => {
  //console.log('row from api is: ', row)
  const x = rowMap[row[0]]
  const y = columnMap[row[1]]
  //console.log('x, y, row[dataIndex]', x, y, row[dataIndex])
  data[x][y] = row[dataIndex]
}

// TODO make it generic for multiple rows
// This code is imperative -> takes data from API and
// returns an object with rowLabels, columnLabels, data and schema
const mapPivotTableDataToRender = (schema, apiDataList) => {
  console.log('Schema: ', schema, 'apiDataList: ', apiDataList)
  return apiDataList.map ( apiData => {

    // get the row labels, column labels, page labels
    let rowSet = new Set()
    let columnSet = new Set()
    // use first one to get the rows and columns
    apiData.forEach(row => {
      row.forEach((element, idx) => {
        if (idx === 0) { // idx < schema.rowLabels.length
          rowSet.add(element)
        } else if (idx === 1) {// idx < (schema.rowLabels.length + schema.columnLabels.length)
          columnSet.add(element)
        }
        // could optimize if break here
      })
    })

    const rows = Array.from(rowSet)
    const columns = Array.from(columnSet)

    const rowMap = rows.reduce((result, val, index) => {
      result[val] = index
      return result
    }, {})

    const columnMap = columns.reduce((result, val, index) => {
      result[val] = index
      return result
    }, {})

    // create an array of arrays filled with empty string
    // number of rows is the same as rowLabels and
    // columns is the same as column labels
    let data = rows.map( val => {
      let p = new Array(columns.length)
      p.fill(' ')
      return p
    })


    // insert data
    apiData.forEach((row, idx) => {
      const rowIndex = (schema.rowLabels.length + schema.rowLabels.length)
      // single label on row and column for now
      insertIn2D(rowMap, columnMap, row, rowIndex, data)
    })

    //console.log(data)
    return {
      rowLabels: rows,
      columnLabels: columns,
      data
    };
  })
}

// note: The Spread Operator in {...state} creates a shallow copy of the state object.
// (it can be used with arrays, function arguments also)
// {...state, infoMessage: 'Success"} will create a shallow copy of the state object with infoMessage set to 'Success'
// for more info: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Spread_operator
const rootReducer = (state = initialState, action) => {
  // console.log(action)
  const {type} = action
  switch (type) {
    case C.FETCH_TABLE_LIST:
      return {...state, fetchTableListloading: true}
    case C.FETCH_TABLE_LIST_SUCCESS:
      return {...state, fetchTableListloading: false, tableList: action.data, errorMessage: ''}
    case C.FETCH_TABLE_LIST_ERROR:
      return {...state, fetchTableListloading: false, errorMessage: 'Error getting list of tables'}
    case C.TABLE_SELECTED:
      return {...state, selectedTable: action.value, rawReport: initialState.rawReport}
    case C.CONNECT_DATA_SOURCE:
      return {...state, connectionLoading: true}
    case C.CONNECT_DATA_SOURCE_SUCCESS:
      return {...state,
        connectionLoading: false,
        connectedSuccessfully: true,
        errorMsg: '',
      }
    case C.CONNECT_DATA_SOURCE_ERROR:
      return {...state,
        connectionLoading: false,
        connectedSuccessfully: false,
        errorMessage: 'There was an error connecting.'
      }
    case C.USERNAME_CHANGED:
      return {...state, username: action.value}
    case C.PASSWORD_CHANGED:
      return {...state, password: action.value}
    case C.DATASOURCE_CHANGED:
      return {...state, sourceName: action.value}
    case C.FETCH_RAW_REPORT:
      return {...state, rawReportLoading: true}
    case C.FETCH_RAW_REPORT_SUCCESS:
      return {...state,
        rawReport: action.data,
        rawReportLoading: false,
        tableSchema: {
          rowLabels: action.data.columns,
          selectedRowLabels: [],
          columnLabels: [],
          selectedColumnLabels: [],
          pageLabels: [],
          selectedPageLabel: '',
          functionList: initialState.tableSchema.functionList,
          selectedFunction: '',
          possibleValues: [],
          selectedValue: ''
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: [],
        errorMessage: ''
      }
    case C.FETCH_RAW_REPORT_ERROR:
      return {...state,
        rawReportLoading: false,
        rawReport: initialState.rawReport,
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: [],
        errorMessage: 'There was a problem fetching the raw report'
      }
    case C.DISCONNECT:
      return {...initialState,
        username: state.username,
        sourceName: state.sourceName
      }
    case C.SCHEMA_ROW_LABELS_SELECTED:
      //console.log('Called the reducer for SCHEMA_ROW_LABELS_SELECTED')
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedRowLabels: state.tableSchema.rowLabels.filter(val => {
            return action.value.indexOf(val.name) !== -1
          }),
          columnLabels: state.tableSchema.rowLabels.filter(val => {
            return action.value.indexOf(val.name) === -1
          }),
          selectedColumnLabels: [],
          selectedPageLabel: '',
          selectedFunction: '',
          selectedValue: '',
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.SCHEMA_COLUMN_LABELS_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedColumnLabels: state.tableSchema.columnLabels.filter (val => {
            return action.value.indexOf(val.name) !== -1
          }),
          pageLabels: state.tableSchema.columnLabels.filter(val => {
            return action.value.indexOf(val.name) === -1
          }),
          selectedPageLabel: '',
          selectedFunction: '',
          selectedValue: '',
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: [],
      }
    case C.SCHEMA_PAGE_LABEL_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedPageLabel: action.value,
          /*functionList: state.rawReport.columns.find( val => {
            return val.name === action.value
          }).type === 'TYPE_NUMERIC' ? ['sum', 'count'] : ['count'],*/
          selectedFunction: '',
          selectedValue: '',
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.SCHEMA_FUNCTION_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedFunction: action.value,
          possibleValues: state.tableSchema.pageLabels.filter(val => {
            if ((action.value) === 'sum') {
              return val.type === 'TYPE_NUMERIC'
            }
            // else (count) -> all fields are good (need to filter out selected page!)
            return true
          }),
          selectedValue: ''
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.SCHEMA_VALUE_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedValue: action.value
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.SCHEMA_RESET:
      return {
        ...state,
        tableSchema: {
          rowLabels: state.rawReport.columns,
          selectedRowLabels: [],
          columnLabels: [],
          selectedColumnLabels: [],
          pageLabels: [],
          selectedPageLabel: '',
          functionList: initialState.tableSchema.functionList,
          selectedFunction: '',
          possibleValues: [],
          selectedValue: ''
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.GENERATE_PIVOT_TABLE:
      return {...state, pivotTableLoading: true}
    case C.GENERATE_PIVOT_TABLE_SUCCESS:
      return {...state,
        pivotTableLoading: false,
        // this function returns an array...
        pivotTables: mapPivotTableDataToRender(
          action.data.schema,
          action.data.data
        ),
        pageLabels: action.data.pageLabelValues,
        pageSelected: 0,
        errorMessage: ''
      }
    case C.GENERATE_PIVOT_TABLE_ERROR:
      return {...state,
        pivotTableLoading: false,
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: [],
        errorMessage: 'Error generating pivot table'
      }
    case C.PIVOT_TABLE_PAGE_CHANGED:
      return {
        ...state,
        pageSelected: action.page
      }
    default:
      return state
  }
}

export default rootReducer
