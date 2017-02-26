import React, {PropTypes} from 'react'
import {Segment, Input, Button, Label} from 'semantic-ui-react'
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
      {!isConnected ?
        <div>
          <Input type="text" label="Data Source" value={dataSource} onChange={onDataSourceChanged} placeholder="Enter data source"/>
          <Input type="text" label="Username" value={userName} onChange={onUserNameChanged} placeholder="Enter username"/>
          <Input type="password" label="Password" value={password} onChange={onPasswordChanged} placeholder="Enter password"/>
          <Button primary={true} onClick={onCheckConnection(dataSource, userName, password)} disabled={!dataSource || !userName || !password}>
            Check Connection
          </Button>
        </div>
        :
        <div>
          <Label>Connected as {userName}</Label>
          <Button color="red" onClick={onDisconnect} >
            Disconnect
          </Button>
        </div>
      }
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
  console.log(state)
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
