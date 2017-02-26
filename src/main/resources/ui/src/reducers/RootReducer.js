
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
  tableSchema: {}, // TODO
  pivotTableLoading: false,
  pivotTable: {}, // TODO
  infoMessage: '',
  errorMessage: ''
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
      return {...state, fetchTableListloading: false, tableList: action.data}
    case C.FETCH_TABLE_LIST_ERROR:
      return {...state, fetchTableListloading: false, errorMessage: 'Error getting list of tables'}
    case C.TABLE_SELECTED:
      return {...state, selectedTable: action.value, rawReport: initialState.rawReport}
    case C.CONNECT_DATA_SOURCE:
      return {...state, connectionLoading: true}
    case C.CONNECT_DATA_SOURCE_SUCCESS:
      return {...state, connectionLoading: false, connectedSuccessfully: true}
    case C.CONNECT_DATA_SOURCE_ERROR:
      // TODO error message?
      return {...state, connectionLoading: false, connectedSuccessfully: false}
    case C.USERNAME_CHANGED:
      return {...state, username: action.value}
    case C.PASSWORD_CHANGED:
      return {...state, password: action.value}
    case C.DATASOURCE_CHANGED:
      return {...state, sourceName: action.value}
    case C.FETCH_RAW_REPORT:
      return {...state, rawReportLoading: true}
    case C.FETCH_RAW_REPORT_SUCCESS:
      return {...state, rawReport: action.data, rawReportLoading: false}
    case C.FETCH_RAW_REPORT_ERROR:
      // TODO
      return {...state, rawReportLoading: false, rawReport: initialState.rawReport}
    case C.DISCONNECT:
      return initialState
    default:
      return state
  }
}

export default rootReducer
