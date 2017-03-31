import React, {PropTypes} from 'react'
import PivotTable from './PivotTable'
import {Segment, Label, Form} from 'semantic-ui-react'

import {connect} from 'react-redux'

import {pivotTablePageChanged, printableViewChanged} from '../actions/ActionCreators'

const PivotTableSegment = (props) => {
  const {
    isConnected,
    pivotTables,
    tableSelected,
    loading,
    onPageChanged,
    pageSelected,
    pageLabels,
    isPrintableView,
    onPrintableViewChanged,
  } = props
  //console.log('props', props)
  if (!isConnected || !tableSelected || pivotTables.length === 0 || pageSelected === -1) {
    return <div></div>
  }
  const {rowLabels, columnLabels, data, schema} = pivotTables[pageSelected]
  const pageOptions = pageLabels.map ( (val, index) => ({
    text: val,
    value: index,
    key: index
  }))
  let pageAlias = schema.aliasMap[schema.pageLabel]
  if (!pageAlias) {
    pageAlias = schema.pageLabel
  }

  const pageDropDown = pageOptions.length > 0 ? (
      <div>
        <Form.Field>
          <label>{`${pageAlias}`}</label>
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
      <Form.Checkbox
        toggle={true}
        checked={isPrintableView}
        onChange={onPrintableViewChanged}
        label="Simple View"
      />
      <br />
      {pageDropDown}
      <div className="raw-report-table">
        <PivotTable
          rowLabels={rowLabels}
          columnLabels={columnLabels}
          data={data}
          schema={schema}
        />
      </div>
    </Segment>
  )
}

PivotTableSegment.propTypes = {
  isConnected: PropTypes.bool.isRequired,
  pivotTables: PropTypes.arrayOf(PropTypes.object).isRequired,
  tableSelected: PropTypes.string.isRequired,
  loading: PropTypes.bool.isRequired,
  pageSelected: PropTypes.number.isRequired,
  pageLabels: PropTypes.arrayOf(PropTypes.string),
  onPageChanged: PropTypes.func,
  onPrintableViewChanged: PropTypes.func,
  isPrintableView: PropTypes.bool,
}

const mapStateToProps = (state) => ({
  isConnected: state.connectedSuccessfully,
  pivotTables: state.pivotTables,
  pageSelected: state.pageSelected,
  pageLabels: state.pageLabels,
  tableSelected: state.selectedTable,
  loading: state.pivotTableLoading,
  isPrintableView: state.printableView,
})

const mapDispatchToProps = (dispatch) => ({
  // value contains the index of the page
  onPageChanged: (e, {value}) => {
    dispatch(pivotTablePageChanged(value))
  },
  onPrintableViewChanged: (e, {checked}) => {
    dispatch(printableViewChanged(checked))
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (PivotTableSegment)
