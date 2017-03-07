import {getTableList, checkAccess, getRawReport} from '../api/endpoints'
import {C} from '../reducers/RootReducer'

export const fetchTableList = () => async (dispatch) => {
  dispatch({type: C.FETCH_TABLE_LIST})
  try {
    const resp = await getTableList()
    dispatch({type: C.FETCH_TABLE_LIST_SUCCESS, data: resp.data})
  } catch (e) {
    dispatch({type: C.FETCH_TABLE_LIST_ERROR})
  }
}

export const connectToDataSource = (sourceName, username, password) => async (dispatch) => {
  dispatch({type: C.CONNECT_DATA_SOURCE})
  try {
    const resp = await checkAccess(sourceName, username, password)
    console.log('checkAccess response status', resp.status)
    dispatch({type: C.CONNECT_DATA_SOURCE_SUCCESS})
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

export const fetchRawReport = (tableName) => async (dispatch) => {
  dispatch(tableSelected(tableName))
  if (!tableName)
    return
  dispatch({type:C.FETCH_RAW_REPORT})
  try {
    const resp = await getRawReport(tableName)
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

export const generatePivotTable = () => ({
  type: C.GENERATE_PIVOT_TABLE
})
