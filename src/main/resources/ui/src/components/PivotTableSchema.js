import React from 'react'

import {Segment, Form, Button, Label, Input, Modal} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {getPivotTableState} from '../reducers/RootReducer'
import {rowLabelsChanged,
  columnLabelsChanged,
  pageLabelChanged,
  resetSchema,
  functionChanged,
  valueChanged,
  generatePivotTable,
  filterFieldSelected,
  filterValueSelected,
  sortFieldSelected,
  sortOrderSelected,
  aliasChanged,
  summaryFunctionChanged,
  schemaNameChanged,
  savePivotTableSchema,
} from '../actions/ActionCreators'

const PivotTableSchema = (props) => {
  const {isConnected, loading, rowLabels, selectedRowLabels, columnLabels,
    selectedColumnLabels, pageLabels, selectedPageLabel,
    functionList, selectedFunction, possibleValues, selectedValue,
    onRowLabelsChanged, onColumnLabelsChanged, onPageLabelChanged, onReset,
    onFunctionChanged, onValueChanged, onGeneratePivotTable,
    filterFields,
    selectedFilterField,
    filterValue,
    sortFields,
    selectedSortField,
    sortOrder,
    onFilterFieldSelected,
    onFilterValueChanged,
    onSortFieldSelected,
    onSortOrderSelected,
    allColumns,
    aliasMap,
    onAliasChanged,
    onSummaryFunctionChanged,
    selectedSummaryFunction,
    onSchemaNameChanged,
    schemaName,
    onSchemaSave,
    isReadOnly
  } = props

  const aliasFields = allColumns.filter (item => {
    const finderFunc = (field => field === item.name);
    return (
      item.name === selectedValue ||
      item.name === selectedPageLabel ||
      selectedRowLabels.findIndex(finderFunc) >= 0 ||
      selectedColumnLabels.findIndex(finderFunc) >= 0
    )
  })

  return isConnected ? (
    <Segment loading={loading}>
      <Label as="div" attached="top" color="blue">Pivot Table Schema</Label>
      <Form as="div" onSubmit={onGeneratePivotTable(selectedValue, selectedSummaryFunction)}>
          <Form.Field>
            <label>Row Labels</label>
            <Form.Dropdown multiple options={rowLabels}
                           value={selectedRowLabels}
                           onChange={onRowLabelsChanged}
                           disabled={isReadOnly}
                           placeholder="Select Row Labels"/>
          </Form.Field>
          <Form.Field>
            <label>Column Labels</label>
            <Form.Dropdown multiple
                           options={columnLabels}
                           disabled={selectedRowLabels.length === 0 || isReadOnly}
                           value={selectedColumnLabels}
                           onChange={onColumnLabelsChanged}
                           placeholder="Select Column Labels"/>
          </Form.Field>
          <Form.Field>
            <label>Page Label</label>
            <Form.Dropdown disabled={selectedColumnLabels.length === 0 || isReadOnly}
                           value={selectedPageLabel}
                           onChange={onPageLabelChanged}
                           options={pageLabels}
                           placeholder="Select Page Label"/>
          </Form.Field>
          <Form.Group inline>
            <Form.Field>
              <label>Report Filter</label>
              <Form.Dropdown disabled={selectedColumnLabels.length === 0 || isReadOnly}
                             onChange={onFilterFieldSelected}
                             options={filterFields}
                             value={selectedFilterField}
                             placeholder="Select Report Filter"/>
            </Form.Field>
            <Form.Field>
              <label>Filter Value</label>
              <Input disabled={selectedFilterField === '' || isReadOnly}
                          type="text"
                          onChange={onFilterValueChanged}
                          value={filterValue}
                          placeholder="Select Filter Value"
              >
              </Input>
            </Form.Field>
          </Form.Group>
          <Form.Group inline>
            <Form.Field>
              <label>Sort Field</label>
              <Form.Dropdown disabled={selectedColumnLabels.length === 0 || isReadOnly}
                             onChange={onSortFieldSelected}
                             options={sortFields}
                             value={selectedSortField}
                             placeholder="Select Sort Field" />
            </Form.Field>
            <Form.Field>
              <label>Sort Order</label>
              <Form.Dropdown disabled={selectedSortField === '' || isReadOnly}
                             onChange={onSortOrderSelected}
                             value={sortOrder}
                             options={[
                               {key: 0, text: 'Ascending', value: 'asc'},
                               {key: 1, text: 'Descending', value: 'desc'}
                               ]}/>
            </Form.Field>
          </Form.Group>
          <Form.Field>
            <label>Function</label>
            <Form.Dropdown disabled={selectedColumnLabels.length === 0 || isReadOnly}
                           onChange={onFunctionChanged}
                           options={functionList}
                           value={selectedFunction}
                           placeholder="Select Function"/>
          </Form.Field>
          <Form.Field>
            <label>Values</label>
            <Form.Dropdown disabled={!selectedFunction || isReadOnly}
                           onChange={onValueChanged}
                           options={possibleValues}
                           value={selectedValue}
                           placeholder="Select Value to apply function on"/>
          </Form.Field>
          <Form.Field>
            <label>Summary Function</label>
            <Form.Dropdown disabled={selectedColumnLabels.length === 0 || isReadOnly}
                           onChange={onSummaryFunctionChanged}
                           options={functionList}
                           value={selectedSummaryFunction}
                           placeholder="Select Summary Function"/>
          </Form.Field>
        {isReadOnly ? null :(
          <Form.Field>
              <Modal closeIcon={true}
                   trigger={<Form.Button disabled={!selectedValue}>Customize labels</Form.Button>}>
              <Modal.Header>
                Customize the text of the labels to show in the pivot table
              </Modal.Header>
              <Modal.Content>
                <Form as="div">
                  {aliasFields.map( (item, indx) => (
                    <Form.Field key={indx}>
                      <label>{item.name}</label>
                      <Form.Input
                        name={item.name}
                        value={
                          (aliasMap[item.name] !== undefined)
                            ? aliasMap[item.name]
                            : item.alias
                        }
                        onChange={onAliasChanged} />
                    </Form.Field>
                  ))}
                </Form>
              </Modal.Content>
            </Modal>
          </Form.Field>
          )}
          <Form.Group inline>
            <Form.Field>
              <label>Schema Name</label>
              <Input value={schemaName}
                     type="text"
                     onChange={onSchemaNameChanged}
                     disabled={isReadOnly}
                     placeholder="Enter a name for this schema to save it."
              />
            </Form.Field>
            {isReadOnly ? null : (
            <Form.Field>
              <Button disabled={!selectedValue || !selectedSummaryFunction || !schemaName}
                      onClick={onSchemaSave}
                      color="green">
                Save
              </Button>
            </Form.Field>
            )}
          </Form.Group>
          <Form.Field>
            <Button primary={true} onClick={onGeneratePivotTable(selectedValue, selectedSummaryFunction)}
                    disabled={(!selectedValue || !selectedSummaryFunction)}>
              Generate Pivot Table
            </Button>
            {isReadOnly ? null : (
            <Button onClick={onReset}>
              Reset
            </Button>
            )}
          </Form.Field>
      </Form>
    </Segment>
  )
    : (
    <div>
    </div>
    )
}

const mapStateToProps = (rootstate) => {
  const state = getPivotTableState(rootstate)
  return {
    loading: state.rawReportLoading || state.pivotTableLoading,
    isConnected: state.connectedSuccessfully,
    allColumns: state.tableSchema.rowLabels,
    aliasMap: state.tableSchema.aliasMap,
    rowLabels: state.tableSchema.rowLabels.map(val => ({
      text: val.alias,
      value: val.name,
      key: val.name
    })),
    selectedRowLabels: state.tableSchema.selectedRowLabels.map(val => val.name),
    columnLabels: state.tableSchema.columnLabels.map(val => ({
      text: val.alias,
      value: val.name,
      key: val.name
    })),
    selectedColumnLabels: state.tableSchema.selectedColumnLabels.map(val => val.name),
    pageLabels: [{text: '', value: null, key: -1}].concat(state.tableSchema.pageLabels.map(val => ({
      text: val.alias,
      value: val.name,
      key: val.name
    }))),
    selectedPageLabel: state.tableSchema.selectedPageLabel,
    functionList: state.tableSchema.functionList.map(val => ({
      text: val,
      value: val,
      key: val
    })),
    selectedFunction: state.tableSchema.selectedFunction,
    possibleValues: state.tableSchema.possibleValues.map(val => ({
      text: val.alias,
      key: val.name,
      value: val.name
    })),
    selectedValue: state.tableSchema.selectedValue,
    // TODO review below ^^;
    filterFields: ([{name: '', alias: ''}].concat(state.tableSchema.filterFields)).map((val, idx) => ({
      text: val.alias,
      key: idx,
      value: val.name
    })),
    selectedFilterField: state.tableSchema.selectedFilterField,
    filterValue: state.tableSchema.filterValue,
    sortFields: ([{name: '', alias: ''}].concat(state.tableSchema.sortFields)).map((val, idx) => ({
      text: val.alias,
      key: idx,
      value: val.name
    })),
    selectedSortField: state.tableSchema.selectedSortField,
    sortOrder: state.tableSchema.sortOrder,
    selectedSummaryFunction: state.tableSchema.summaryFunction,
    schemaName: state.tableSchema.name
  }
}


const mapDispatchToProps = (dispatch) => ({
  onRowLabelsChanged: (e, {value}) => {
    dispatch(rowLabelsChanged(value))
  },
  onColumnLabelsChanged: (e, {value}) => {
    dispatch(columnLabelsChanged(value))
  },
  onPageLabelChanged: (e, {value}) => {
    dispatch(pageLabelChanged(value))
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
  onFilterFieldSelected: (e, {value}) => {
    dispatch(filterFieldSelected(value))
  },
  onFilterValueChanged: (e, {value}) => {
    dispatch(filterValueSelected(value))
  },
  onSortFieldSelected: (e, {value}) => {
    dispatch(sortFieldSelected(value))
  },
  onSortOrderSelected: (e, {value}) => {
    dispatch(sortOrderSelected(value))
  },
  onGeneratePivotTable: (selectedValue, selectedSummaryFunction) => (e) => {
    if (e) {
      e.preventDefault();
    }
    if (selectedValue && selectedSummaryFunction) {
      dispatch(generatePivotTable())
    }
  },
  onAliasChanged: (e, {name, value}) => {
    dispatch(aliasChanged(name, value))
  },
  onSummaryFunctionChanged: (e, {value}) => {
    dispatch(summaryFunctionChanged(value))
  },
  onSchemaNameChanged: (e, {value}) => {
    dispatch(schemaNameChanged(value))
  },
  onSchemaSave: () => {
    dispatch(savePivotTableSchema())
  }
})


export default connect(mapStateToProps, mapDispatchToProps) (PivotTableSchema)
