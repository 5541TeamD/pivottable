import React, {Component} from 'react'
import {Segment} from 'semantic-ui-react'

import ConnectionForm from './ConnectionForm'
import TableDropDown from './TableDropDown'
import RawDataReport from './RawDataReport'
import PivotTableSchema from './PivotTableSchema'
import PivotTableSegment from './PivotTableSegment'
import PrintablePivotTables from './PrintablePivotTables'
import {clearPivotTableStore, fetchShareableSchema} from '../actions/ActionCreators'
import {getPivotTableState} from '../reducers/RootReducer'
import {connect} from 'react-redux'

import {withRouter} from 'react-router-dom'

class PivotTableApp extends Component {

  componentDidMount() {
    const path = this.props.match.path;
    //console.log(this.props)
    if (!path.indexOf('/create') >= 0) {
      const id = this.props.match.params.id
      if (id !== undefined) {
        this.props.getShareableSchema(id)
      }
    }
  }

  componentWillUnmount() {
    this.props.clearStore()
  }

  render() {
    const readOnly = !!this.props.isReadOnly
    return ( this.props.isPrintableView ?
        <PrintablePivotTables/> :
        <Segment loading={this.props.loading} color="red">
          <ConnectionForm isReadOnly={readOnly} />
          <TableDropDown isReadOnly={readOnly} />
          <RawDataReport isReadOnly={readOnly}/>
          <PivotTableSchema isReadOnly={readOnly}/>
          <PivotTableSegment />
        </Segment>
    )
  }
}



const mapStateToProps = (state) => {
  const pivotTableState = getPivotTableState(state)
  return {
    isPrintableView: pivotTableState.printableView,
    loading: pivotTableState.fetchLoading
  }
}

const mapDispatchToProps = (dispatch) => ({
  clearStore: () => {dispatch(clearPivotTableStore())},
  getShareableSchema: (id) => {dispatch(fetchShareableSchema(id))}
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(PivotTableApp))
