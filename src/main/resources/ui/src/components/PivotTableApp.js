import React from 'react'
import {Segment} from 'semantic-ui-react'

import ConnectionForm from './ConnectionForm'
import TableDropDown from './TableDropDown'
import RawDataReport from './RawDataReport'

const PivotTableApp = () => {
  return (
    <Segment color="red">
      <ConnectionForm/>
      <TableDropDown/>
      <RawDataReport/>
    </Segment>
  )
}

export default PivotTableApp
