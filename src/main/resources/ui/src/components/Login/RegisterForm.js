import React, {PropTypes} from 'react'
import {Segment, Input, Button, Label, Form, Message} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {getRegisterFormState} from '../../reducers/RootReducer'
import {registerFormUsernameChanged,
  registerFormPassword1Changed,
  registerFormPassword2Changed,
  doRegister,
} from '../../actions/ActionCreators'

const RegisterForm = (props) => {
  const {loading, userName, password1, password2,
    onUserNameChanged, onPassword1Changed, onPassword2Changed,
    errorMessage, onRegister, infoMessage} = props

  const errorBox = errorMessage.length > 0 ? (
    <Message negative>
      <Message.Header>{errorMessage}</Message.Header>
    </Message>) : null

  const infoBox = infoMessage.length > 0 ? (
    <Message info>
      <Message.Header>{infoMessage}</Message.Header>
    </Message>) : null

  return  (
      <Segment loading={loading}>
        <Label as="div" attached="top" color="green">Register</Label>
        {errorBox}
        {infoBox}
        <Form as="div">
          <div>
            <Form.Field>
              <label>Username</label>
              <Input type="text" value={userName} onChange={onUserNameChanged} placeholder="Enter username"/>
            </Form.Field>
            <Form.Field>
              <label>Password</label>
              <Input type="password" value={password1} onChange={onPassword1Changed} placeholder="Enter password"/>
            </Form.Field>
            <Form.Field>
              <label>Confirm your password</label>
              <Input type="password" value={password2} onChange={onPassword2Changed} placeholder="Re-enter password"/>
            </Form.Field>
            <Form.Field>
              <Button primary={true} onClick={onRegister(userName, password1)} disabled={password1.length < 3 || !userName || password1 !== password2 }>
                Register
              </Button>
            </Form.Field>
          </div>
        </Form>
      </Segment>
    )
}

RegisterForm.propTypes = {
  loading: PropTypes.bool.isRequired,
  userName: PropTypes.string.isRequired,
  password1: PropTypes.string.isRequired,
  password2: PropTypes.string.isRequired,
  onUserNameChanged: PropTypes.func.isRequired,
  onPassword1Changed: PropTypes.func.isRequired,
  onPassword2Changed: PropTypes.func.isRequired,
  onRegister: PropTypes.func.isRequired,
  errorMessage: PropTypes.string.isRequired,
  infoMessage: PropTypes.string.isRequired,
}

const mapStateToProps = (rootState) => {
  const state = getRegisterFormState(rootState);
  return {
    loading: state.loading,
    userName: state.username,
    password1: state.password1,
    password2: state.password2,
    errorMessage: state.errorMessage,
    infoMessage: state.infoMsg,
  }
}

const mapDispatchToProps = (dispatch) => ({
  onUserNameChanged: (e, {value}) => {
    dispatch(registerFormUsernameChanged(value))
  },
  onPassword1Changed: (e, {value}) => {
    dispatch(registerFormPassword1Changed(value))
  },
  onPassword2Changed: (e, {value}) => {
    dispatch(registerFormPassword2Changed(value))
  },
  onRegister: (username, pwd) => () => {
    dispatch(doRegister(username, pwd))
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(RegisterForm)
