import React, {PropTypes} from 'react'
import {Segment, Input, Button, Label, Form, Message} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {getLoginFormState} from '../../reducers/RootReducer'
import {loginFormUsernameChanged,
  doLogin,
  loginFormPasswordChanged} from '../../actions/ActionCreators'

const LoginForm = (props) => {
  const {loading, userName, password,
    onUserNameChanged, onPasswordChanged,
    errorMessage, onLogin} = props

  const errorBox = errorMessage.length > 0 ?
    (<Message negative>
      <Message.Header>{errorMessage}</Message.Header>
    </Message>) : null

  return (
    <Segment loading={loading}>
      <Label as="div" attached="top" color="red">App Login</Label>
      {errorBox}
        <Form as="div">
          <div>
            <Form.Field>
              <label>Username</label>
              <Input type="text" value={userName} onChange={onUserNameChanged} placeholder="Enter username"/>
            </Form.Field>
            <Form.Field>
              <label>Password</label>
              <Input type="password" value={password} onChange={onPasswordChanged} placeholder="Enter password"/>
            </Form.Field>
            <Form.Field>
              <Button primary={true} onClick={onLogin(userName, password)} disabled={!password || !userName }>
                Login
              </Button>
            </Form.Field>
          </div>
        </Form>
    </Segment>
  )
}

LoginForm.propTypes = {
  loading: PropTypes.bool.isRequired,
  userName: PropTypes.string.isRequired,
  password: PropTypes.string.isRequired,
  onUserNameChanged: PropTypes.func.isRequired,
  onPasswordChanged: PropTypes.func.isRequired,
  onLogin: PropTypes.func.isRequired,
  errorMessage: PropTypes.string.isRequired,
}

const mapStateToProps = (rootState) => {
  const state = getLoginFormState(rootState);
  return {
    loading: state.loading,
    userName: state.username,
    password: state.password,
    errorMessage: state.errorMessage,
  }
}

const mapDispatchToProps = (dispatch) => ({
  onUserNameChanged: (e, {value}) => {
    dispatch(loginFormUsernameChanged(value))
  },
  onPasswordChanged: (e, {value}) => {
    dispatch(loginFormPasswordChanged(value))
  },
  onLogin: (username, pwd) => () => {
    dispatch(doLogin(username, pwd))
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm)
