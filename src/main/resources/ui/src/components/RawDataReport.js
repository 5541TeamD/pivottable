import React, {PropTypes} from 'react'
import {Segment, Table, Label} from 'semantic-ui-react'
import {getPivotTableState} from '../reducers/RootReducer'
import {connect} from 'react-redux'


const RawDataReport = (props) => {
  const {isConnected, rawReport, tableSelected, loading} = props
  if (!isConnected && !tableSelected) {
    return <div></div>
  }
  /*const rows = rawReport.rows.map( (item) => {
    return rawReport.columns.reduce( (row, col, index) => {
      row[col.name] = item[index]
      return row
    }, {})
  })*/
  const rows = rawReport.rows
  return (
    <Segment loading={loading}>
      <Label attached="top">Raw Data: {tableSelected}</Label>
      <div className="raw-report-table">
        <Table celled={true}>
          <Table.Header>
            <Table.Row>
              {rawReport.columns.map( (it, idx) => {
                return (
                  <Table.HeaderCell key={idx}>{it.alias}</Table.HeaderCell>
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

RawDataReport.propTypes = {
  isConnected: PropTypes.bool.isRequired,
  rawReport: PropTypes.object.isRequired,
  tableSelected: PropTypes.string.isRequired,
  loading: PropTypes.bool.isRequired
}

const mapStateToProps = (rootState) => {
  const state = getPivotTableState(rootState)
  return {
    isConnected: state.connectedSuccessfully,
    rawReport: state.rawReport,
    tableSelected: state.selectedTable,
    loading: state.rawReportLoading
  }
}

export default connect(mapStateToProps) (RawDataReport)
