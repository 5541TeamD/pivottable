import {C, getPivotTableState} from '../reducers/RootReducer'
import {getTableList,
  checkAccess,
  getRawReport,
  getPivotTable,
  login,
  logout,
  register
} from '../api/endpoints'

const handleErrorResponse = (e, type, dispatch, additionalMessage = '') => {
  console.log(e)
  let message = `An error occurred. ${additionalMessage}`;
  if (e.response && e.response.data && e.response.data.details) {
    message = `${message} ${e.response.data.details}`
    if (e.response.status >= 400) {
      console.log(`Received a code ${e.response.status}...`);
    }
  }
  dispatch({type, message})
}

export const fetchTableList = () => async (dispatch, getState) => {
  dispatch({type: C.FETCH_TABLE_LIST})
  const {username, password, sourceName} = getPivotTableState(getState())
  try {
    const resp = await getTableList(sourceName, username, password)
    dispatch({type: C.FETCH_TABLE_LIST_SUCCESS, data: resp.data})
  } catch (e) {
    dispatch({type: C.FETCH_TABLE_LIST_ERROR})
  }
}

export const connectToDataSource = (sourceName, username, password) => async (dispatch) => {
  dispatch({type: C.CONNECT_DATA_SOURCE})
  try {
    const resp = await checkAccess(sourceName, username, password)
    dispatch({type: C.CONNECT_DATA_SOURCE_SUCCESS, data: resp.data})
    dispatch(fetchTableList())
  } catch (e) {
    console.log(e)
    dispatch({type: C.CONNECT_DATA_SOURCE_ERROR})
  }
}

const tableSelected = (value) => ({
  type: C.TABLE_SELECTED,
  value
})

export const pivotTablePageChanged = (value) => ({
  type: C.PIVOT_TABLE_PAGE_CHANGED,
  page: value
})

export const fetchRawReport = (tableName) => async (dispatch, getState) => {
  dispatch(tableSelected(tableName))
  if (!tableName)
    return
  dispatch({type:C.FETCH_RAW_REPORT})
  const {username, password, sourceName} = getPivotTableState(getState())
  try {
    const resp = await getRawReport(tableName, sourceName, username, password)
    dispatch({type: C.FETCH_RAW_REPORT_SUCCESS, data: resp.data})
  } catch (e) {
    dispatch({type: C.FETCH_RAW_REPORT_ERROR})
  }
}

// Sync actions

export const userNameChanged = (value) => ({
  type: C.USERNAME_CHANGED,
  value
})

export const passwordChanged = (value) => ({
  type: C.PASSWORD_CHANGED,
  value
})

export const dataSourceNameChanged = (value) => ({
  type: C.DATASOURCE_CHANGED,
  value: value
})

// disconnect is sync since all state is stored in UI
export const disconnect = () => ({
  type: C.DISCONNECT
})

export const rowLabelsChanged = (value) => ({
  type: C.SCHEMA_ROW_LABELS_SELECTED,
  value
})

export const columnLabelsChanged = (value) => ({
  type: C.SCHEMA_COLUMN_LABELS_SELECTED,
  value
})

export const pageLabelChanged = (value) => ({
  type: C.SCHEMA_PAGE_LABEL_SELECTED,
  value
})

export const resetSchema = () => ({
  type: C.SCHEMA_RESET
})

export const functionChanged = (value) => ({
  type: C.SCHEMA_FUNCTION_SELECTED,
  value
})

export const valueChanged = (value) => ({
  type: C.SCHEMA_VALUE_SELECTED,
  value
})

export const filterFieldSelected = (value) => ({
  type: C.FILTER_FIELD_SELECTED,
  value
})

export const filterValueSelected = (value) => ({
  type: C.FILTER_VALUE_SELECTED,
  value
})

export const sortFieldSelected = (value) => ({
  type: C.SORT_FIELD_SELECTED,
  value
})

export const sortOrderSelected = (value) => ({
  type: C.SORT_ORDER_SELECTED,
  value
})

const buildSchemaToSend = ({tableSchema, selectedTable}) => ({
  tableName: selectedTable,
  columnLabels: tableSchema.selectedColumnLabels.map ( it => it.name),
  rowLabels: tableSchema.selectedRowLabels.map (it => it.name),
  pageLabel: tableSchema.selectedPageLabel,
  functionName: tableSchema.selectedFunction,
  valueField: tableSchema.selectedValue,
  filterField: tableSchema.selectedFilterField,
  filterValue: tableSchema.filterValue,
  sortField: tableSchema.selectedSortField,
  sortOrder: tableSchema.sortOrder,
  aliasMap: tableSchema.aliasMap,
  tableSummFuncName: tableSchema.summaryFunction,
})

export const generatePivotTable = () => async(dispatch, getState) => {
  dispatch({type: C.GENERATE_PIVOT_TABLE})
  try {
    const currentState = getPivotTableState(getState())
    const {username, password, sourceName} = currentState
    const resp = await getPivotTable(buildSchemaToSend(currentState),
      sourceName, username, password
    )
    dispatch({type: C.GENERATE_PIVOT_TABLE_SUCCESS, data: resp.data})
  } catch (e) {
    handleErrorResponse(e, C.GENERATE_PIVOT_TABLE_ERROR, dispatch)
  }
}

export const printableViewChanged = (value) => (
  {type: C.TOGGLE_PRINTABLE_VIEW, value}
)

export const aliasChanged = (name, value) => ({
  type: C.SCHEMA_LABEL_ALIAS_CHANGED,
  name,
  value
})

export const summaryFunctionChanged = (value) => ({
  type: C.SCHEMA_SUMMARY_FUNCTION_SELECTED,
  value
})

export const loginFormUsernameChanged = (value) => ({
  type: C.LOGIN_USERNAME_CHANGED,
  value
})

export const loginFormPasswordChanged = (value) => ({
  type: C.LOGIN_PASSWORD_CHANGED,
  value
})

export const registerFormUsernameChanged = (value) => ({
  type: C.REGISTER_USERNAME_CHANGED,
  value
})

export const registerFormPassword1Changed = (value) => ({
  type: C.REGISTER_PASSWORD1_CHANGED,
  value
})

export const registerFormPassword2Changed = (value) => ({
  type: C.REGISTER_PASSWORD2_CHANGED,
  value
})

export const doLogin = (username, password) => async (dispatch) => {
  dispatch({type: C.LOGIN_SUBMIT})
  try {
    const resp = await login(username, password);
    // success
    dispatch({type: C.LOGIN_SUBMIT_SUCCESS, user: resp.data.username})
  } catch (e) {
    handleErrorResponse(e, C.LOGIN_SUBMIT_FAILURE, dispatch, 'Log in failed.');
  }
}

export const doLogout = () => async (dispatch) => {
  dispatch({type: C.LOGOUT})
  try {
    const resp = await logout();
    dispatch({type: C.LOGOUT_SUCCESS})
  } catch (e) {
    handleErrorResponse(e, C.LOGOUT_FAILURE, dispatch, 'Failed to logout.')
  }
}

export const doRegister = (username, password) => async (dispatch) => {
  dispatch({type: C.REGISTER_SUBMIT})
  try {
    const resp = await register(username, password);
    dispatch({type: C.REGISTER_SUBMIT_SUCCESS});
  } catch (e) {
    handleErrorResponse(e, C.REGISTER_SUBMIT_FAILURE, dispatch, 'Registration failed...');
  }
}
