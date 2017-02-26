import React, {PropTypes} from 'react'
import {Dropdown, Segment, Label} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {tableSelected} from '../actions/ActionCreators'

export const TableDropDown = (props) => {
  if (!props.isConnected) {
    return null;
  }
  const tableOptions = props.tables.map( (name) => {
    return {
      text: name,
      value: name
    }
  })
  return (
    <Segment>
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
  tableSelected : PropTypes.func.isRequired,
  tables : PropTypes.arrayOf(PropTypes.string).isRequired,
  currentTable: PropTypes.string.isRequired,
  isConnected: PropTypes.bool.isRequired,
}

const mapStateToProps = (state) => ({
  tables: state.tableList,
  currentTable: state.selectedTable,
  isConnected: state.connectedSuccessfully
})

const mapDispatchToProps = (dispatch) => ({
  tableSelected:  (event, obj) => {
    const {value} = obj
    dispatch(tableSelected(value))
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (TableDropDown)
