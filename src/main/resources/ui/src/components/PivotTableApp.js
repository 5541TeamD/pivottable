import React from 'react'
import {Segment} from 'semantic-ui-react'

import ConnectionForm from './ConnectionForm'
import TableDropDown from './TableDropDown'
import RawDataReport from './RawDataReport'
import PivotTableSchema from './PivotTableSchema'
import PivotTable from './PivotTable'

const PivotTableApp = () => {
  return (
    <Segment color="red">
      <ConnectionForm />
      <TableDropDown />
      <RawDataReport />
      <PivotTableSchema />
      <PivotTable />
    </Segment>
  )
}

export default PivotTableApp
