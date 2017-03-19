import {getTableList, checkAccess, getRawReport, getPivotTable} from '../api/endpoints'
import {C} from '../reducers/RootReducer'

export const fetchTableList = () => async (dispatch, getState) => {
  dispatch({type: C.FETCH_TABLE_LIST})
  const {username, password, sourceName} = getState()
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
  const {username, password, sourceName} = getState()
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

const buildSchemaToSend = ({tableSchema, selectedTable}) => ({
  tableName: selectedTable,
  columnLabels: tableSchema.selectedColumnLabels.map ( it => it.name),
  rowLabels: tableSchema.selectedRowLabels.map (it => it.name),
  pageLabel: tableSchema.selectedPageLabel,
  functionName: tableSchema.selectedFunction,
  valueField: tableSchema.selectedValue
})

export const generatePivotTable = () => async(dispatch, getState) => {
  dispatch({type: C.GENERATE_PIVOT_TABLE})
  try {
    const currentState = getState()
    const {username, password, sourceName} = currentState
    const resp = await getPivotTable(buildSchemaToSend(currentState),
      sourceName, username, password
    )
    dispatch({type: C.GENERATE_PIVOT_TABLE_SUCCESS, data: resp.data})
  } catch (e) {
    console.log(e)
    dispatch({type: C.GENERATE_PIVOT_TABLE_ERROR})
  }
}
