import React, {PropTypes} from 'react'

import PivotTable from './PivotTable'
import {printableViewChanged} from '../actions/ActionCreators'
import {connect} from 'react-redux'

import {Checkbox, Label} from 'semantic-ui-react'

const PrintablePivotTables = (props) => {
  const {
    isConnected,
    pivotTables,
    tableSelected,
    pageLabels,
    onPrintableViewChanged,
    isPrintableView,
    tableSummary
  } = props;
  const checkbox = (
    <Checkbox
      toggle={true}
      checked={isPrintableView}
      onChange={onPrintableViewChanged}
      label="Simple View"
    />
  );
  if (!isConnected || !tableSelected || pivotTables.length === 0) {
    return (<div>
      {checkbox}
    </div>)
  }
  if (pivotTables.length > 0 && pageLabels.length === 0) {
    const {columnLabels, rowLabels, data, schema, rowSummaryData, colSummaryData, pageSummary} = pivotTables[0]
    const {valueField, functionName} = schema;
    const valueAlias = schema.aliasMap[valueField] ? schema.aliasMap[valueField] : valueField;
    return (
      <div>
        {checkbox}
        <br />
        <h3>{`${functionName}(${valueAlias})`}</h3>
        <PivotTable
          columnLabels={columnLabels}
          rowLabels={rowLabels}
          data={data}
          schema={schema}
          rowSummaryData={rowSummaryData}
          colSummaryData={colSummaryData}
          pageSummary={pageSummary}
        />
      </div>
    )
  }

  const tables = pivotTables.map ( (pivotTable, pageNumber) => {
    const {columnLabels, rowLabels, data, schema, rowSummaryData, colSummaryData, pageSummary} = pivotTable
    let pageAlias = schema.aliasMap[schema.pageLabel]
    if (!pageAlias) {
      pageAlias = schema.pageLabel
    }
    const {valueField, functionName} = schema;
    const valueAlias = schema.aliasMap[valueField] ? schema.aliasMap[valueField] : valueField;
    return (
      <div key={pageNumber}>
        {pageNumber === 0 ? (
            <h3>{`${functionName}(${valueAlias})`}</h3>
          ): null}
        <span><label>{`${pageAlias}: `}</label>{pageLabels[pageNumber]}</span>
        <hr/>
        <PivotTable
          columnLabels={columnLabels}
          rowLabels={rowLabels}
          data={data}
          schema={schema}
          rowSummaryData={rowSummaryData}
          colSummaryData={colSummaryData}
          pageSummary={pageSummary}
        />
        <br />
      </div>
    )
  })
  return (
    <div className="raw-report-table-visible" >
      {checkbox}
      <br />
      {tables}
      <br />
      <Label basic size="large">{`Report Summary: ${tableSummary}`}</Label>
    </div>
  )
}

PrintablePivotTables.propTypes = {
  isConnected: PropTypes.bool.isRequired,
  pivotTables: PropTypes.arrayOf(PropTypes.object).isRequired,
  tableSelected: PropTypes.string.isRequired,
  pageLabels: PropTypes.arrayOf(PropTypes.string),
  onPrintableViewChanged: PropTypes.func,
  isPrintableView: PropTypes.bool,
  tableSummary: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
}

const mapStateToProps = (state) => ({
  isConnected: state.connectedSuccessfully,
  pivotTables: state.pivotTables,
  pageLabels: state.pageLabels,
  tableSelected: state.selectedTable,
  isPrintableView: state.printableView,
  tableSummary: state.tableSummary,
})

const mapDispatchToProps = (dispatch) => ({
  // value contains the index of the page
  onPrintableViewChanged: (e, {checked}) => {
    dispatch(printableViewChanged(checked))
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(PrintablePivotTables)
