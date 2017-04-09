import React, {PropTypes} from 'react'
import {Segment, Input, Button, Label, Form, Message} from 'semantic-ui-react'
import {connect} from 'react-redux'
import {getPivotTableState} from '../reducers/RootReducer'
import {dataSourceNameChanged, userNameChanged,
  passwordChanged, disconnect, connectToDataSource} from '../actions/ActionCreators'

const ConnectionForm = (props) => {
  const {loading, dataSource, userName, password,
  onDataSourceChanged, onUserNameChanged, onPasswordChanged,
  onCheckConnection, isConnected, onDisconnect,
    errorMessage, infoMessage, isReadOnly} = props

  const errorBox = errorMessage.length > 0 ?
    (<Message negative>
      <Message.Header>{errorMessage}</Message.Header>
    </Message>) : null

  const infoBox = infoMessage.length > 0 ? (
      <Message info>
        <Message.Header>{infoMessage}</Message.Header>
      </Message>) : null

  return (
    <Segment loading={loading}>
      <Label as="div" attached="top" color="blue">Data Source Connection</Label>
      {errorBox}
      {infoBox}
      <Form as="form" onSubmit={onCheckConnection}>
      {!isConnected ?
        <div>
          <Form.Field>
            <label>Data Source</label>
            <Input disabled={isReadOnly} type="text" value={dataSource} onChange={onDataSourceChanged} placeholder="Enter data source"/>
          </Form.Field>
          <Form.Field>
            <label>Username</label>
            <Input type="text" disabled={isReadOnly} value={userName} onChange={onUserNameChanged} placeholder="Enter username"/>
          </Form.Field>
          <Form.Field>
            <label>Password</label>
            <Input type="password" disabled={isReadOnly} value={password} onChange={onPasswordChanged} placeholder="Enter password"/>
          </Form.Field>
          {isReadOnly ? null : (
          <Form.Field>
          <Button primary={true} onClick={onCheckConnection(dataSource, userName, password)} disabled={!dataSource || !userName }>
            Check Connection
          </Button>
          </Form.Field>)
          }
        </div>
        :
        <div>
          <Form.Field>
            <Label>{userName}</Label>
          </Form.Field>
          <Form.Field>
            <Label basic>Using data source: {dataSource}</Label>
          </Form.Field>
          {isReadOnly ? null : (
          <Form.Field>
            <Button color="red" onClick={onDisconnect} >
              Reset
            </Button>
          </Form.Field>
          )}
        </div>
      }
      </Form>
    </Segment>
  )
}

ConnectionForm.propTypes = {
  loading: PropTypes.bool.isRequired,
  dataSource: PropTypes.string.isRequired,
  userName: PropTypes.string.isRequired,
  password: PropTypes.string.isRequired,
  isConnected: PropTypes.bool.isRequired,
  onDataSourceChanged: PropTypes.func.isRequired,
  onUserNameChanged: PropTypes.func.isRequired,
  onPasswordChanged: PropTypes.func.isRequired,
  onCheckConnection: PropTypes.func.isRequired,
  onDisconnect: PropTypes.func.isRequired,
  errorMessage: PropTypes.string.isRequired,
  infoMessage: PropTypes.string.isRequired,
  isReadOnly: PropTypes.bool.isRequired,
}

const mapStateToProps = (rootState) => {
  const state = getPivotTableState(rootState);
  return {
    loading: state.connectionLoading,
    dataSource: state.sourceName,
    userName: state.username,
    password: state.password,
    isConnected: state.connectedSuccessfully,
    errorMessage: state.errorMessage,
    infoMessage: state.infoMessage,
  }
}

const mapDispatchToProps = (dispatch) => ({
  onDataSourceChanged: (e, {value}) => {
    dispatch(dataSourceNameChanged(value))
  },
  onUserNameChanged: (e, {value}) => {
    dispatch(userNameChanged(value))
  },
  onPasswordChanged: (e, {value}) => {
    dispatch(passwordChanged(value))
  },
  onDisconnect: () => { dispatch(disconnect()) },
  onCheckConnection: (source, name, pwd) => (e) => {
    if (e) {
      e.preventDefault()
    }
    if (source && name) {
      dispatch(connectToDataSource(source, name, pwd))
    }
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (ConnectionForm)
