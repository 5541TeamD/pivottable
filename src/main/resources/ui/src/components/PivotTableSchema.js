import React from 'react'

import {Segment, Form, Button, Label} from 'semantic-ui-react'
import {connect} from 'react-redux'

import {rowLabelsChanged,
  columnLabelsChanged,
  pageLabelChanged,
  resetSchema,
  functionChanged,
  valueChanged,
  generatePivotTable
} from '../actions/ActionCreators'

const PivotTableSchema = (props) => {
  const {isConnected, loading, rowLabels, selectedRowLabels, columnLabels,
    selectedColumnLabels, pageLabels, selectedPageLabel,
    functionList, selectedFunction, possibleValues, selectedValue,
    onRowLabelsChanged, onColumnLabelsChanged, onPageLabelChanged, onReset,
    onFunctionChanged, onValueChanged, onGeneratePivotTable
  } = props

  return isConnected ? (
    <Segment loading={loading}>
      <Label as="div" attached="top" color="blue">Pivot Table Schema</Label>
      <Form as="div">
        <div>
          <Form.Field>
            <label>Row Labels</label>
            <Form.Dropdown multiple options={rowLabels}
                           value={selectedRowLabels}
                           onChange={onRowLabelsChanged}
                           placeholder="Select Row Labels"/>
          </Form.Field>
          <Form.Field>
            <label>Column Labels</label>
            <Form.Dropdown multiple
                           options={columnLabels}
                           disabled={selectedRowLabels.length === 0}
                           value={selectedColumnLabels}
                           onChange={onColumnLabelsChanged}
                           placeholder="Select Column Labels"/>
          </Form.Field>
          {/*
          <Form.Field>
            <label>Page Label</label>
            <Form.Dropdown disabled={selectedColumnLabels.length === 0}
                           value={selectedPageLabel}
                           onChange={onPageLabelChanged}
                           options={pageLabels}
                           placeholder="Select Page Label"/>
          </Form.Field>
          <Form.Field>
            <label>Report Filter</label>
            <Form.Dropdown disabled={true}
                           onChange={onPageLabelChanged}
                           placeholder="Select Report Filter"/>
          </Form.Field>
           */}
          <Form.Field>
            <label>Function</label>
            <Form.Dropdown disabled={selectedColumnLabels.length === 0}
                           onChange={onFunctionChanged}
                           options={functionList}
                           value={selectedFunction}
                           placeholder="Select Function"/>
          </Form.Field>
          <Form.Field>
            <label>Values</label>
            <Form.Dropdown disabled={!selectedFunction}
                           onChange={onValueChanged}
                           options={possibleValues}
                           value={selectedValue}
                           placeholder="Select Value to apply function on"/>
          </Form.Field>
          <Form.Field>
            <Button primary={true} onClick={onGeneratePivotTable}
                    disabled={!selectedValue}>
              Generate Pivot Table
            </Button>
            <Button onClick={onReset}>
              Reset
            </Button>
          </Form.Field>
        </div>
      </Form>
    </Segment>
    )
    : (
    <div>
    </div>
    )

}

const mapStateToProps = (state) => ({
  loading: state.rawReportLoading,
  isConnected: state.connectedSuccessfully,
  rowLabels: state.tableSchema.rowLabels.map ( val => ({
    text: val.alias,
    value: val.name,
    key: val.name
  })),
  selectedRowLabels: state.tableSchema.selectedRowLabels.map( val => val.name),
  columnLabels: state.tableSchema.columnLabels.map (val => ({
    text: val.alias,
    value: val.name,
    key: val.name
  })),
  selectedColumnLabels: state.tableSchema.selectedColumnLabels.map (val => val.name),
  pageLabels: state.tableSchema.pageLabels.map( val => ({
    text: val.alias,
    value: val.name,
    key: val.name
  })),
  selectedPageLabel: state.tableSchema.selectedPageLabel,
  functionList: state.tableSchema.functionList.map( val => ({
    text: val,
    value: val,
    key: val
  })),
  selectedFunction: state.tableSchema.selectedFunction,
  possibleValues: state.tableSchema.possibleValues.map( val => ({
    text: val.alias,
    key: val.name,
    value: val.name
  })),
  selectedValue: state.tableSchema.selectedValue
})


const mapDispatchToProps = (dispatch) => ({
  onRowLabelsChanged: (e, {value}) => {
    dispatch(rowLabelsChanged(value))
  },
  onColumnLabelsChanged: (e, {value}) => {
    dispatch(columnLabelsChanged(value))
  },
  onPageLabelChanged: (e, {value}) => {
    pageLabelChanged(value)
  },
  onReset: () => {
    dispatch(resetSchema())
  },
  onFunctionChanged: (e, {value}) => {
    dispatch(functionChanged(value))
  },
  onValueChanged: (e, {value}) => {
    dispatch(valueChanged(value))
  },
  onGeneratePivotTable: () => {
    dispatch(generatePivotTable())
  }
})


export default connect(mapStateToProps, mapDispatchToProps) (PivotTableSchema)
