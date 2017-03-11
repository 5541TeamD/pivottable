import React, {PropTypes} from 'react'
import {Segment, Input, Button, Label, Form} from 'semantic-ui-react'
import {connect} from 'react-redux'

import {dataSourceNameChanged, userNameChanged,
  passwordChanged, disconnect, connectToDataSource} from '../actions/ActionCreators'

const ConnectionForm = (props) => {
  const {loading, dataSource, userName, password,
  onDataSourceChanged, onUserNameChanged, onPasswordChanged,
  onCheckConnection, isConnected, onDisconnect} = props
  return (
    <Segment loading={loading}>
      <Label as="div" attached="top" color="blue">Data Source Connection</Label>
      <Form as="div">
      {!isConnected ?
        <div>
          <Form.Field>
            <label>Data Source</label>
            <Input type="text" value={dataSource} onChange={onDataSourceChanged} placeholder="Enter data source"/>
          </Form.Field>
          <Form.Field>
            <label>Username</label>
            <Input type="text" value={userName} onChange={onUserNameChanged} placeholder="Enter username"/>
          </Form.Field>
          <Form.Field>
            <label>Password</label>
            <Input type="password" value={password} onChange={onPasswordChanged} placeholder="Enter password"/>
          </Form.Field>
          <Form.Field>
          <Button primary={true} onClick={onCheckConnection(dataSource, userName, password)} disabled={!dataSource || !userName }>
            Check Connection
          </Button>
          </Form.Field>
        </div>
        :
        <div>
          <Form.Field>
            <Label>{userName}</Label>
          </Form.Field>
          <Form.Field>
            <Label basic>Connected to {dataSource}</Label>
          </Form.Field>
          <Form.Field>
            <Button color="red" onClick={onDisconnect} >
              Disconnect
            </Button>
          </Form.Field>
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
}

const mapStateToProps = (state) => {
  return {
    loading: state.connectionLoading,
    dataSource: state.sourceName,
    userName: state.username,
    password: state.password,
    isConnected: state.connectedSuccessfully
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
  onCheckConnection: (source, name, pwd) => () => { dispatch(connectToDataSource(source, name, pwd)) }
})

export default connect(mapStateToProps, mapDispatchToProps) (ConnectionForm)
