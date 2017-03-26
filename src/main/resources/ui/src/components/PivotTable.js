import React, {PropTypes} from 'react'
import {Table} from 'semantic-ui-react'



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

const PivotTable = ({rowLabels, columnLabels, data}) => {

  const headerRows = buildHeaderRows(rowLabels, columnLabels)

  const rows = buildDataRows(rowLabels, data)

  return (
    <div>
      <Table celled={true}>
        <Table.Header>
            {headerRows}
        </Table.Header>
        <Table.Body>
          {rows}
        </Table.Body>
      </Table>
    </div>
  )
}

PivotTable.propTypes = {
  data: PropTypes.array,
  rowLabels: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.string)),
  columnLabels: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.string)),
}

export default  PivotTable
