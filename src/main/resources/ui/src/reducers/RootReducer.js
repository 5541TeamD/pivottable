
const constants = {
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
  DISCONNECT: 'DISCONNECT'
}

export {constants}

const initialState = () => ({
  connectionLoading: false,
  fetchTableLoading: false,
  selectedTable: '',
  username: '',
  password: '',
  sourceName: '',
  connectedSuccessfully: false,
  tableList: [],
  rawReport: {}, // data set
  tableSchema: {},
  pivotTable: {},
  infoMessage: '',
  errorMessage: ''
})

const rootReducer = (state = initialState(), action) => {
  // console.log(action)
  const {type} = action
  switch (type) {
    case constants.FETCH_TABLE_LIST:
      return {...state, fetchTableloading: true}
    case constants.FETCH_TABLE_LIST_SUCCESS:
      return {...state, fetchTableloading: false, tableList: action.data}
    case constants.FETCH_TABLE_LIST_ERROR:
      return {...state, fetchTableloading: false, errorMessage: 'Error getting list of tables'}
    case constants.TABLE_SELECTED:
      return {...state, selectedTable: action.value}
    case constants.CONNECT_DATA_SOURCE:
      return {...state, connectionLoading: true}
    case constants.CONNECT_DATA_SOURCE_SUCCESS:
      return {...state, connectionLoading: false, connectedSuccessfully: true}
    case constants.USERNAME_CHANGED:
      return {...state, username: action.value}
    case constants.PASSWORD_CHANGED:
      return {...state, password: action.value}
    case constants.DATASOURCE_CHANGED:
      return {...state, sourceName: action.value}
    case constants.DISCONNECT:
      return initialState()
    default:
      return state
  }
}

export default rootReducer
