import React from 'react'
import {Route, Redirect, withRouter} from 'react-router-dom'
import {connect} from 'react-redux'

import {getAuthenticationState} from '../../reducers/RootReducer'


// wrapper function to pass Route props to the component
const renderMergedProps = (component, ...rest) => {
  const finalProps = Object.assign({}, ...rest);
  return (
    React.createElement(component, finalProps)
  );
}

// utility component (as seen in the example of react-router for auth)
// simply redirects to /login if the user is not logged in.
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
