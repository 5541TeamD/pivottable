import React from 'react'
import {Route, Redirect, withRouter} from 'react-router-dom'
import {connect} from 'react-redux'

import {getAuthenticationState} from '../../reducers/RootReducer'

const PrivateRoute = ({isLoggedIn, component, ...rest}) => {
  return (
  <Route {...rest} render={props => (
    isLoggedIn ? (
        React.createElement(component,props)
      ) :
      <Redirect to={{
        pathname : '/login'
      }}/>
  )}/>
  )
}

const mapStateToProps = (rootState) => {
  const state = getAuthenticationState(rootState)
  return {
    isLoggedIn: state.loggedInUser !== null
  }
}

export default withRouter(connect(mapStateToProps)(PrivateRoute))
