import React from 'react'

import {Segment, Form} from 'semantic-ui-react'

const PivotTableSchema = (props) => {
  const {isConnected, rowLabels, selectedRowLabels, columnLables,
    selectedColumnLabels, pageLabels, selectedPageLabel,
    functionList, selectedFunction, possibleValues, selectedValue,
    onRowLabelsChanged, onColumnLabelsChanged, onPageLabelChanged, onReset,
    onFunctionChanged, onValueChanged, onGeneratePivotTable
  } = props

  return !isConnected ? (
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
                           options={columnLables}
                           disabled={selectedRowLabels.length === 0}
                           value={selectedColumnLabels}
                           onChange={onColumnLabelsChanged}
                           placeholder="Select Column Labels"/>
          </Form.Field>
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
            <Form.Dropdown disabled={true /* TODO */}
                           onChange={onPageLabelChanged}
                           placeholder="Select Report Filter"/>
          </Form.Field>
          <Form.Field>
            <label>Function</label>
            <Form.Dropdown disabled={!selectedPageLabel}
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
