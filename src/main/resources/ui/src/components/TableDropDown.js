import React, {PropTypes} from 'react'
import {Dropdown, Segment, Label} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {fetchRawReport} from '../actions/ActionCreators'

import {getPivotTableState} from '../reducers/RootReducer'

export const TableDropDown = (props) => {
  if (!props.isConnected) {
    return <div></div>;
  }
  const tableOptions = [{
    text: '',
    value: ''
  }].concat(props.tables.map( (name) => {
      return {
        text: name,
        value: name
      }
    })
  )
  return (
    <Segment loading={props.loading}>
      <Label as="div" attached="top">Select a data set</Label>
      <Dropdown placeholder='Select Data Set'
                fluid
                selection={true}
                name="tableListDropDown"
                options={tableOptions}
                value={props.currentTable}
                onChange={props.tableSelected} />
    </Segment>
  )
}

TableDropDown.propTypes = {
  loading: PropTypes.bool.isRequired,
  tableSelected : PropTypes.func.isRequired,
  tables : PropTypes.arrayOf(PropTypes.string).isRequired,
  currentTable: PropTypes.string.isRequired,
  isConnected: PropTypes.bool.isRequired,
}

const mapStateToProps = (rootState) => {
  const state = getPivotTableState(rootState)
  return {
    loading: state.fetchTableListLoading,
    tables: state.tableList,
    currentTable: state.selectedTable,
    isConnected: state.connectedSuccessfully
  }
}

const mapDispatchToProps = (dispatch) => ({
  tableSelected:  (event, obj) => {
    const {value} = obj
    dispatch(fetchRawReport(value))
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (TableDropDown)
