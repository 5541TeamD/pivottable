import React from 'react'
import {Segment} from 'semantic-ui-react'

import ConnectionForm from './ConnectionForm'
import TableDropDown from './TableDropDown'

const PivotTableApp = () => {
  return (
    <Segment color="red">
      <ConnectionForm/>
      <TableDropDown/>
    </Segment>
  )
}

export default PivotTableApp
