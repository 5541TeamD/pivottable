import React, {PropTypes} from 'react'
import {Table} from 'semantic-ui-react'

import {multiplyByArrayLength} from '../utils'

// just something to generate unique id's
let staticId = 0
function getNextId() {
  return ++staticId;
}

const buildHeaderRows = (rowLabels, columnLabels, schema) => {
  //console.log('rows', rows)
  let headerRows = []
  const {aliasMap} = schema;
  const replaceAliasName = (key) => {
    const alias = aliasMap[key];
    return (alias !== undefined && alias !== '') ? alias : key;
  };
  const schemaRowLabels = schema.rowLabels.map (replaceAliasName);
  const schemaColumnLabels = schema.columnLabels.map(replaceAliasName);
  const {tableSummFuncName} = schema;
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
    /*if (i === 0) {
      headerCells.push(
        <Table.HeaderCell className="table-definition-header-empty-cell" key="0" rowSpan={columnLabels.length} colSpan={rowLabels.length} />
      )
    }*/
    // Labels
    for (let hi=0; hi < schemaRowLabels.length; ++hi) {
      if (i === (schemaColumnLabels.length - 1) &&
        hi === (schemaRowLabels.length-1)) {
        headerCells.push(
          <Table.HeaderCell
            className="table-definition-header-cell-label"
            key={getNextId()}
          >
            <div>
              {`${schemaColumnLabels[i]}➡️`}
            </div>
            <hr/>
            <div>
              {`${schemaRowLabels[hi]}⬇️`}
            </div>
          </Table.HeaderCell>
        )
      } else if (hi === (schemaRowLabels.length-1)) {
          headerCells.push(
            <Table.HeaderCell
              className="table-definition-header-cell-label"
              key={getNextId()}
            >
              {`${schemaColumnLabels[i]}`}
            </Table.HeaderCell>
          )
      } else if (i === (schemaColumnLabels.length-1)) {
          headerCells.push(
            <Table.HeaderCell
              className="table-definition-header-cell-label"
              key={getNextId()}
            >
              {`${schemaRowLabels[hi]}`}
            </Table.HeaderCell>
          )
      } else {
        headerCells.push(<Table.HeaderCell
          className="table-definition-header-cell-label box-shadow-cell"
          key={getNextId()}
        />);
      }
    }
    for (let k = 0; k < repeatTimes; ++k) {
      for (const item of column) {
        headerCells.push(
          <Table.HeaderCell className="table-definition-header-cell" key={getNextId()}
                            colSpan={columnSpan}>{`${item}`}</Table.HeaderCell>
        )
      }
    }
    if (i === 0) {
      headerCells.push(
        <Table.HeaderCell
          className="table-definition-header-empty-cell"
          key={getNextId()}
          rowSpan={columnLabels.length}
          colSpan={rowLabels.length}>
          {tableSummFuncName}
        </Table.HeaderCell>
      )
    }
    headerRows.push(
      <Table.Row children={headerCells} key={getNextId()}/>
    )
    ++i;
  }
  return headerRows
}

const buildDataRows = (rowLabels, data, rowSummary, colSummary) => {
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
          <Table.Cell rowSpan={rowSpan} key={getNextId()} className="table-definition-column-cell">
            {`${rowLabels[k][y]}`}
          </Table.Cell>
        )
      }
    }
    for (const element of row) {
      cells.push(
        <Table.Cell key={getNextId()}>{`${element}`}</Table.Cell>
      )
    }
    // summary rows
    for (let k = 0; k < rowSummary.length; ++k) {
      /*let rowSpan = 1
      if ( k > 0) {
        rowSpan = rowLabels.slice(0, k).reduce(multiplyByArrayLength, 1)
      }*/
      let rowSpan = Math.floor(data.length / rowSummary[k].length);
      if (i % rowSpan === 0) {
        //const normalizedI = i % (rowSpan * rowSummary[k].length);
        const y = Math.floor( i / rowSpan);
        cells.push(
          <Table.Cell rowSpan={rowSpan} key={getNextId()} className="table-definition-column-cell">
            {`${rowSummary[k][y]}`}
          </Table.Cell>
        )
      }
    }
    rows.push(
      <Table.Row key={getNextId()} children={cells}/>
    )
    ++i;
  }
  return rows
}

const PivotTable = ({rowLabels, columnLabels, data, schema, colSummaryData, rowSummaryData}) => {

  const headerRows = buildHeaderRows(rowLabels, columnLabels, schema)

  const rows = buildDataRows(rowLabels, data, rowSummaryData, colSummaryData)

  const {valueField, functionName} = schema;

  const valueAlias = schema.aliasMap[valueField] ? schema.aliasMap[valueField] : valueField;

  return (
    <div>
      <h3>{`${functionName}(${valueAlias})`}</h3>
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
  schema: PropTypes.object.isRequired,
  rowSummaryData: PropTypes.array,
  colSummaryData: PropTypes.array,
  pageSummary: PropTypes.oneOf([PropTypes.string, PropTypes.number]),
}

export default  PivotTable
