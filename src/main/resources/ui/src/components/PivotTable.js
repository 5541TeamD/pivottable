import React, {PropTypes} from 'react'
import {Segment, Table, Label} from 'semantic-ui-react'

import {connect} from 'react-redux'

const PivotTable = (props) => {
  const {
    isConnected,
    pivotTable,
    tableSelected,
    loading,
    onPageChanged
  } = props
  if (!isConnected || !tableSelected || !pivotTable.schema || pivotTable.pageSelected === -1) {
    return <div></div>
  }
  const {rowLabels, columnLabels, pageLabels, data, pageSelected} = pivotTable
  // concatenate rowlabel with data to use the same
  // (almost) algorithm as rendering the raw report
  const pageValues = pageLabels.map ( (val, index) => ({
    text: val,
    value: index,
    key: index
  }))
  const rows = data[pageSelected].map ((row, idx) => {
    return [rowLabels[idx]].concat(row)
  })

  const pageDropDown = pageValues.length > 0 ? (
      <Form.Field>
        <label>Page</label>
        <Form.Dropdown
          value={pageSelected}
          onChange={onPageChanged}
          options={pageValues}
          placeholder="Select Page"/>
      </Form.Field>
    ) : null;

  return (
    <Segment loading={loading}>
      <Label attached="top">Pivot Table: {tableSelected}</Label>
      <Form.Field>
        <label>Page</label>
        <Form.Dropdown disabled={selectedColumnLabels.length === 0}
                       value={selectedPageLabel}
                       onChange={onPageLabelChanged}
                       options={pageLabels}
                       placeholder="Select Page Label"/>
      </Form.Field>
      <div className="raw-report-table">
        <Table definition={true} celled={true}>
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell/>
              {columnLabels.map( (it, idx) => {
                return (
                  <Table.HeaderCell key={idx}>{it}</Table.HeaderCell>
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
  pivotTable: PropTypes.object.isRequired,
  tableSelected: PropTypes.string.isRequired,
  loading: PropTypes.bool.isRequired,
  onPageChanged: PropTypes.func
}

const mapStateToProps = (state) => ({
  isConnected: state.connectedSuccessfully,
  pivotTable: state.pivotTable,
  tableSelected: state.selectedTable,
  loading: state.pivotTableLoading,
})

export default connect(mapStateToProps) (PivotTable)
