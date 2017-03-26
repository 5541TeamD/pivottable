import React, {PropTypes} from 'react'
import {Segment, Table, Label, Form} from 'semantic-ui-react'

import {connect} from 'react-redux'

import {pivotTablePageChanged} from '../actions/ActionCreators'

import {multiplyByArrayLength} from '../utils'

const buildHeaderRows = (rowLabels, columnLabels) => {
  //console.log('rows', rows)
  let headerRows = []
  let i = 0;
  for (const column of columnLabels) {
    let columnSpan = 1
    if ( (i+1) < columnLabels.length ) {
      columnSpan = columnLabels.slice(i+1).reduce(multiplyByArrayLength,1)
    }
    let repeatTimes = 1
    if (i-1 >= 0) {
      repeatTimes = columnLabels.slice(0, i).reduce(multiplyByArrayLength,1)
    }
    let headerCells = [];
    if (i === 0) {
      headerCells.push(
        <Table.HeaderCell className="table-definition-header-empty-cell" key="0" rowSpan={columnLabels.length} colSpan={rowLabels.length} />
      )
    }
    for (let k = 0; k < repeatTimes; ++k) {
      for (const item of column) {
        headerCells.push(
          <Table.HeaderCell className="table-definition-header-cell" key={Math.random()}
                            colSpan={columnSpan}>{`${item}`}</Table.HeaderCell>
        )
      }
    }
    headerRows.push(
      <Table.Row children={headerCells} key={i}/>
    )
    ++i;
  }
  return headerRows
}

// TODO
const buildDataRows = (rowLabels, data) => {
  let rows = []
  let i = 0;
  for (const row of data) {
    let cells = [];
    for (let k = 0; k < rowLabels.length; ++k) {
      let rowSpan = 1
      if ((k + 1) < rowLabels.length) {
        rowSpan = rowLabels.slice(k + 1).reduce(multiplyByArrayLength, 1)
      }
      if (i % rowSpan === 0) {
        const normalizedI = i % (rowSpan * rowLabels[k].length);
        const y = Math.floor( normalizedI / rowSpan);
        cells.push(
          <Table.Cell rowSpan={rowSpan} key={Math.random()} className="table-definition-column-cell">
            {`${rowLabels[k][y]}`}
          </Table.Cell>
        )
      }
    }
    for (const element of row) {
      cells.push(
        <Table.Cell key={Math.random()}>{`${element}`}</Table.Cell>
      )
    }
    rows.push(
      <Table.Row key={i} children={cells}/>
    )
    ++i;
  }
  return rows
}

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
  /*const rows = data.map ((row, idx) => {
    return [rowLabels[idx]].concat(row)
  })*/

  const headerRows = buildHeaderRows(rowLabels, columnLabels)

  const rows = buildDataRows(rowLabels, data)

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
        <Table celled={true}>
          <Table.Header>
              {headerRows}
          </Table.Header>
          <Table.Body>
            {/*rows.map( (it, idx) => {
              return (
                <Table.Row key={idx}>
                  {it.map ( (cellContent, index) => {
                    return (
                      <Table.Cell key={index}>{cellContent}</Table.Cell>
                    )
                  } )}
                </Table.Row>
              )
            }) */}
            {rows}
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
