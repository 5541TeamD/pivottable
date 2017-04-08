import React from 'react'
import {Route, Redirect, withRouter} from 'react-router-dom'
import {connect} from 'react-redux'

import {getAuthenticationState} from '../../reducers/RootReducer'

const renderMergedProps = (component, ...rest) => {
  const finalProps = Object.assign({}, ...rest);
  return (
    React.createElement(component, finalProps)
  );
}

const PrivateRoute = ({isLoggedIn, component, ...rest}) => {
  return (
  <Route {...rest} render={props => (
    isLoggedIn ? (
        renderMergedProps(component,props, rest)
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
