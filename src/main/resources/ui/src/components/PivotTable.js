import React, {PropTypes} from 'react'
import {Segment, Table, Label, Form} from 'semantic-ui-react'

import {connect} from 'react-redux'

import {pivotTablePageChanged} from '../actions/ActionCreators'

const PivotTable = (props) => {
  const {
    isConnected,
    pivotTables,
    tableSelected,
    loading,
    onPageChanged,
    pageSelected,
    pageLabels
  } = props
  if (!isConnected || !tableSelected || pivotTables.length === 0 || pageSelected === -1) {
    return <div></div>
  }
  const {rowLabels, columnLabels, data} = pivotTables[pageSelected]
  // concatenate rowlabel with data to use the same
  // (almost) algorithm as rendering the raw report
  const pageOptions = pageLabels.map ( (val, index) => ({
    text: val,
    value: index,
    key: index
  }))
  const rows = data.map ((row, idx) => {
    return [rowLabels[idx]].concat(row)
  })

  //console.log('rows', rows)

  const pageDropDown = pageOptions.length > 0 ? (
    <div>
      <Form.Field>
        <label>Page</label>
        <Form.Dropdown
          value={pageSelected}
          onChange={onPageChanged}
          options={pageOptions}
          placeholder="Select Page"/>
      </Form.Field>
      <hr/>
    </div>
    ) : null;

  return (
    <Segment loading={loading}>
      <Label attached="top">Pivot Table: {tableSelected}</Label>
      {pageDropDown}
      <div className="raw-report-table">
        <Table definition={true} celled={true}>
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell/>
              {columnLabels.map( (it, idx) => {
                return (
                  <Table.HeaderCell key={idx}>{`${it}`}</Table.HeaderCell>
                )
              })}
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {rows.map( (it, idx) => {
              return (
                <Table.Row key={idx}>
                  {it.map ( (cellContent, index) => {
                    return (
                      <Table.Cell key={index}>{cellContent}</Table.Cell>
                    )
                  } )}
                </Table.Row>
              )
            }) }
          </Table.Body>
        </Table>
      </div>
    </Segment>
  )
}

PivotTable.propTypes = {
  isConnected: PropTypes.bool.isRequired,
  pivotTables: PropTypes.arrayOf(PropTypes.object).isRequired,
  tableSelected: PropTypes.string.isRequired,
  loading: PropTypes.bool.isRequired,
  pageSelected: PropTypes.number.isRequired,
  pageLabels: PropTypes.arrayOf(PropTypes.string),
  onPageChanged: PropTypes.func
}

const mapStateToProps = (state) => ({
  isConnected: state.connectedSuccessfully,
  pivotTables: state.pivotTables,
  pageSelected: state.pageSelected,
  pageLabels: state.pageLabels,
  tableSelected: state.selectedTable,
  loading: state.pivotTableLoading,
})

const mapDispatchToProps = (dispatch) => ({
  // value contains the index of the page
  onPageChanged: (e, {value}) => {
    dispatch(pivotTablePageChanged(value))
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (PivotTable)
