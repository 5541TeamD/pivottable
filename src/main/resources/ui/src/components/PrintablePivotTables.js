import React, {PropTypes} from 'react'

import PivotTable from './PivotTable'
import {printableViewChanged} from '../actions/ActionCreators'
import {connect} from 'react-redux'

import {Checkbox} from 'semantic-ui-react'

const PrintablePivotTables = (props) => {
  const {
    isConnected,
    pivotTables,
    tableSelected,
    pageLabels,
    onPrintableViewChanged,
    isPrintableView
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
    const {columnLabels, rowLabels, data} = pivotTables[0]
    return (
      <div>
        {checkbox}
        <br />
        <PivotTable columnLabels={columnLabels} rowLabels={rowLabels} data={data}/>
      </div>
    )
  }

  const tables = pivotTables.map ( (pivotTable, pageNumber) => {
    const {columnLabels, rowLabels, data} = pivotTable
    return (
      <div key={pageNumber}>
        <span><label>Page: </label>{pageLabels[pageNumber]}</span>
        <hr/>
        <PivotTable columnLabels={columnLabels} rowLabels={rowLabels} data={data}/>
        <br />
      </div>
    )
  })
  return (
    <div>
      {checkbox}
      <br />
      {tables}
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
}

const mapStateToProps = (state) => ({
  isConnected: state.connectedSuccessfully,
  pivotTables: state.pivotTables,
  pageLabels: state.pageLabels,
  tableSelected: state.selectedTable,
  isPrintableView: state.printableView,
})

const mapDispatchToProps = (dispatch) => ({
  // value contains the index of the page
  onPrintableViewChanged: (e, {checked}) => {
    dispatch(printableViewChanged(checked))
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(PrintablePivotTables)
