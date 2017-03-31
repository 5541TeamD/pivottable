
import {multiplyByArrayLength} from '../utils'

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
  FETCH_FILTER_FIELDS: 'FETCH_FILTER_FIELDS',
  FETCH_FILTER_FIELDS_SUCCESS: 'FETCH_FILTER_FIELDS_SUCCESS',
  FETCH_FILTER_FIELDS_FAILURE: 'FETCH_FILTER_FIELDS_FAILURE',
  SORT_FIELD_SELECTED: 'SORT_FIELD_SELECTED',
  SORT_ORDER_SELECTED: 'SORT_ORDER_SELECTED',
  FILTER_FIELD_SELECTED: 'FILTER_FIELD_SELECTED',
  FILTER_VALUE_SELECTED: 'FILTER_VALUE_SELECTED',
  TOGGLE_PRINTABLE_VIEW: 'TOGGLE_PRINTABLE_VIEW',
  SCHEMA_LABEL_ALIAS_CHANGED: 'SCHEMA_LABEL_ALIAS_CHANGED',
}

export {C}

const numericalFunctions = [
  'sum',
  'min',
  'max',
  'avg',
  'product',
  'standard deviation',
  'variance',
]

const allFunctions = ['count'].concat(numericalFunctions)

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
    functionList: allFunctions,
    selectedFunction: '', //name of the function
    possibleValues: [],
    selectedValue: '', //name of the value
    filterFields: [],
    selectedFilterField: '',
    filterValue: '',
    sortFields: [],
    selectedSortField: '',
    sortOrder: 'asc',
    aliasMap: {}, // stores the [name:alias] for each value
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
  errorMessage: '',
  printableView: false,
}

/**
 * row label index: 0
 * column label index: 1
 * data index: dataIndex (usually, 2)
 */
/*const insertIn2D = (rowMap, columnMap, row, dataIndex, data) => {
  //console.log('row from api is: ', row)
  const x = rowMap[row[0]]
  const y = columnMap[row[1]]
  //console.log(`x: ${x}, y: ${y}, data: ${row[dataIndex]}`);
  data[x][y] = row[dataIndex]
} */

const insertInGeneric = (rowMaps, columnMaps, row, rowLabelsLength, rowLabels, columnLabels, dataIndex, data) => {
  const x = rowMaps.reduce( (sum, rowMap, index) => {
    //const sizeOfSpan = (index+1) < (rowLabels.length) ? rowLabels[index+1].length : 1;
    let sizeOfSpan = 1;
    if (index+1 < rowLabels.length) {
      sizeOfSpan = rowLabels.slice(index+1).reduce(multiplyByArrayLength, 1);
    }
    return sum + rowMap[row[index]]*sizeOfSpan
  }, 0);
  const y = columnMaps.reduce( (sum, columnMap, index) => {
    //const sizeOfSpan = (index+1) < columnLabels.length ? columnLabels[index+1].length : 1;
    let sizeOfSpan = 1;
    if (index+1 < columnLabels.length) {
      sizeOfSpan = columnLabels.slice(index+1).reduce(multiplyByArrayLength, 1);
    }
    return sum + columnMap[row[index+rowLabelsLength]]*sizeOfSpan
  }, 0);
  //console.log(`x: ${x}, y: ${y}, data: ${row[dataIndex]}`);
  data[x][y] = row[dataIndex];
};

// This code is imperative -> takes data from API and
// returns an object with rowLabels, columnLabels, data and schema
const mapPivotTableDataToRender = (schema, apiDataList) => {
  //console.log('Schema: ', schema, 'apiDataList: ', apiDataList)


  return apiDataList.map ( apiData => {

    // get the row labels, column labels (page labels are handled in the reduce)
    let rowSets = schema.rowLabels.map (it => new Set())
    let columnSets = schema.columnLabels.map (it => new Set())
    // use first one to get the rows and columns
    apiData.forEach( row => {
      row.forEach((element, idx) => {
        if (idx < schema.rowLabels.length) {
          rowSets[idx].add(element)
        } else if (idx >= schema.rowLabels.length && idx < (schema.rowLabels.length + schema.columnLabels.length)) {
          columnSets[idx-schema.rowLabels.length].add(element)
        }
        // could optimize one loop cycle if break here in traditional for loop (vs forEach loop)
      })
    })

    const rows = rowSets.map (rowSet => Array.from(rowSet))
    const columns = columnSets.map (columnSet => Array.from(columnSet))

    const rowMaps = rows.map ( rowSetArray => rowSetArray.reduce((result, val, index) => {
        result[val] = index
        return result
      }, {})
    )

    const columnMaps = columns.map( columnSetArray => columnSetArray.reduce((result, val, index) => {
        result[val] = index
        return result
      }, {})
    )

    // create an array of arrays filled with empty string
    const gridRows = rows.reduce ( multiplyByArrayLength, 1);
    const gridColumns = columns.reduce( multiplyByArrayLength, 1);
    let data = new Array(gridRows)
    for (let i=0; i < data.length; ++i) {
      let col = new Array(gridColumns)
      data[i] = col.fill(' ')
    }

    //console.log('rowMaps', rowMaps)
    //console.log('columnMaps', columnMaps)
    // insert data
    apiData.forEach( row => {
      const rowIndex = (schema.rowLabels.length + schema.columnLabels.length)
      // single label on row and column for now
      // insertIn2D(rowMaps[0], columnMaps[0], row, rowIndex, data)
      // generic one
      insertInGeneric(rowMaps, columnMaps, row, schema.rowLabels.length, rows, columns, rowIndex, data)
    })

    //console.log('final data set is', data)
    return {
      rowLabels: rows,
      columnLabels: columns,
      data,
      schema
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
          ...initialState.tableSchema,
          rowLabels: action.data.columns,
          selectedRowLabels: [],
          columnLabels: [],
          selectedColumnLabels: [],
          pageLabels: [],
          selectedPageLabel: '',
          functionList: initialState.tableSchema.functionList,
          selectedFunction: '',
          possibleValues: [],
          selectedValue: '',
          filterFields: action.data.columns
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
      // const newSelectedRowLabels = state.tableSchema.rowLabels.filter(val => action.value.indexOf(val.name) !== -1)
      const newSelectedRowLabels = action.value.map ( name => {
        return state.tableSchema.rowLabels.find ( val => val.name === name );
      })
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedRowLabels: newSelectedRowLabels,
          columnLabels: state.tableSchema.rowLabels.filter(val => {
            return action.value.indexOf(val.name) === -1
          }),
          selectedColumnLabels: [],
          selectedPageLabel: '',
          selectedFunction: '',
          selectedValue: '',
          sortFields: newSelectedRowLabels,
          selectedSortField: '',
          //filterFields: [],
          selectedFilterField: '',
        },
        pivotTables: initialState.pivotTables,
        pageSelected: -1,
        pageLabels: []
      }
    case C.SCHEMA_COLUMN_LABELS_SELECTED:
      //const newSelectedColumnLabels =  state.tableSchema.columnLabels.filter (val => {
      //  return action.value.indexOf(val.name) !== -1
      //}) // below one preserves selection order
      const newSelectedColumnLabels = action.value.map ( name => {
        return state.tableSchema.columnLabels.find ( val => val.name === name );
      })
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedColumnLabels: newSelectedColumnLabels,
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
            if (numericalFunctions.indexOf(action.value) !== -1) {
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
          ...initialState.tableSchema,
          rowLabels: state.rawReport.columns,
          selectedRowLabels: [],
          columnLabels: [],
          selectedColumnLabels: [],
          pageLabels: [],
          selectedPageLabel: '',
          functionList: initialState.tableSchema.functionList,
          selectedFunction: '',
          possibleValues: [],
          selectedValue: '',
          aliasMap: {},
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
    case C.SORT_ORDER_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          sortOrder: action.value
        },
        pivotTables: initialState.pivotTables,
      }
    case C.SORT_FIELD_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedSortField: action.value
        },
        pivotTables: initialState.pivotTables,
      }
    case C.FILTER_FIELD_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          selectedFilterField: action.value
        },
        pivotTables: initialState.pivotTables
      }

    case C.FILTER_VALUE_SELECTED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          filterValue: action.value
        },
        pivotTables: initialState.pivotTables,
      }
      // TODO might not need the 3 action types below
    case C.FETCH_FILTER_FIELDS_FAILURE:
      return {
        ...state,
        errorMsg: 'Some error occurred while fetching filter fields'
      }
    case C.TOGGLE_PRINTABLE_VIEW:
      return {
        ...state,
        printableView: action.value //!state.printableView
      }
    case C.SCHEMA_LABEL_ALIAS_CHANGED:
      return {
        ...state,
        tableSchema: {
          ...state.tableSchema,
          aliasMap: {...state.tableSchema.aliasMap, [action.name]: action.value}
        }
      }
    case C.FETCH_FILTER_FIELDS_SUCCESS:
    case C.FETCH_FILTER_FIELDS:
    default:
      return state
  }
}

export default rootReducer
