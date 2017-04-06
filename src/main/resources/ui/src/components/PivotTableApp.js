import React from 'react'
import {Segment} from 'semantic-ui-react'

import ConnectionForm from './ConnectionForm'
import TableDropDown from './TableDropDown'
import RawDataReport from './RawDataReport'
import PivotTableSchema from './PivotTableSchema'
import PivotTableSegment from './PivotTableSegment'
import PrintablePivotTables from './PrintablePivotTables'

import {connect} from 'react-redux'

const PivotTableApp = ({isPrintableView}) => {
  return ( isPrintableView ?
      <PrintablePivotTables/> :
    <Segment color="red">
      <ConnectionForm />
      <TableDropDown />
      <RawDataReport />
      <PivotTableSchema />
      <PivotTableSegment />
    </Segment>
  )
}

const mapStateToProps = (state) => ({
  isPrintableView: state.pivotTableReducer.printableView,
})

export default connect(mapStateToProps)(PivotTableApp)
