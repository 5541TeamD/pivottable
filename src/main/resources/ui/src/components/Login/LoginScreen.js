import React, {PropTypes} from 'react'
import {Segment, Grid} from 'semantic-ui-react'
import LoginForm from './LoginForm'
import RegisterForm from './RegisterForm'
import {connect} from 'react-redux'
import {Redirect} from 'react-router-dom'

import {getAuthenticationState, getLoginFormState} from '../../reducers/RootReducer'

const LoginScreen = ({isLoggedIn, loading}) => (
  !isLoggedIn ? (
  <Segment color="blue">
    <h2>Login or create an account if you don't have one</h2>
    <Grid columns={2} stackable>
      <Grid.Column>
        <LoginForm />
      </Grid.Column>
      <Grid.Column>
        <RegisterForm />
      </Grid.Column>
    </Grid>
  </Segment>
  ) :
  <Redirect to="/"/>
)

LoginScreen.propTypes = {
  loading: PropTypes.bool.isRequired,
  isLoggedIn: PropTypes.bool.isRequired,
}

const mapStateToProps = (rootState) => {
  const state = getAuthenticationState(rootState)
  const loginFormState = getLoginFormState(rootState)
  return {
    isLoggedIn: state.loggedInUser !== null,
    loading: state.loading || loginFormState.loading
  }
}

export default connect(mapStateToProps)(LoginScreen)
