import React, {PropTypes} from 'react'
import {Modal, Table, Button, Segment, Header, Input} from 'semantic-ui-react'

import {connect} from 'react-redux'

import {getHomeScreenState} from '../../reducers/RootReducer'

import {
  userToAddValueChanged,
  addUserToShared,
  stopSharingSchema,
  closeSharedUsersModal
} from '../../actions/ActionCreators'

const ShareWithUsersModal = (props) => {
  const {isOpen,
    onCloseHandler,
    users,
    userToAdd,
    onAddUser,
    onUserToAddChanged,
    onRemoveUser,
    loading,
    schemaName,
    schemaId,
    errorBox
  } = props

  return (
    <Modal open={isOpen}
           onClose={onCloseHandler}
    >
      <Header icon="users" content={`Share schema ${schemaName}`}/>
      <Modal.Content loading={loading}>
        <Segment>
          {errorBox}
        <Table collapsing={false} unstackable={true} compact={true} size="small">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>Username</Table.HeaderCell>
              <Table.HeaderCell>Action</Table.HeaderCell>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            <Table.Row>
              <Table.Cell>
                <Input type="text"
                       value={userToAdd}
                       onChange={onUserToAddChanged}
                       placeholder="Enter username to share with"
                />
              </Table.Cell>
              <Table.Cell>
                <Button icon="add user" onClick={onAddUser(schemaId,userToAdd)}/>
              </Table.Cell>
            </Table.Row>
            {users.map (user => (
              <Table.Row>
                <Table.Cell>{user}</Table.Cell>
                <Table.Cell>
                  <Button onClick={onRemoveUser(schemaId,user)} icon="remove user"/>
                </Table.Cell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table>
        </Segment>
      </Modal.Content>
      <Modal.Actions>
        <Button color='green' onClick={onCloseHandler} inverted>
          Close
        </Button>
      </Modal.Actions>
    </Modal>
  )
}

ShareWithUsersModal.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  onCloseHandler: PropTypes.func.isRequired,
  users: PropTypes.arrayOf(PropTypes.string).isRequired,
  userToAdd: PropTypes.string.isRequired,
  onAddUser: PropTypes.func.isRequired,
  onUserToAddChanged: PropTypes.func.isRequired,
  onRemoveUser: PropTypes.func.isRequired,
  loading: PropTypes.bool.isRequired,
  schemaName: PropTypes.string.isRequired,
  schemaId: PropTypes.string.isRequired,
  errorBox: PropTypes.object,
}

const mapStateToProps = (rootstate) => {
  const state = getHomeScreenState(rootstate)
  const selectedSchema = state.mySchemas.find((it => it[0] === state.selectedSchemaId))
  const selectedSchemaName = selectedSchema ? selectedSchema[1] : ''
  return {
    isOpen: state.selectedSchemaId !== '',
    users: state.sharedWithUsers,
    userToAdd: state.userToAdd,
    loading: state.loadingSharedUsers,
    schemaName: selectedSchemaName,
    schemaId: state.selectedSchemaId
  }
}

const mapDispatchToProps = (dispatch) => ({
  onCloseHandler: () => {
    dispatch(closeSharedUsersModal())
  },
  onAddUser: (id, user) => () => {
    dispatch(addUserToShared(id, user))
  },
  onRemoveUser: (id, user) => () => {
    dispatch(stopSharingSchema(id, user))
  },
  onUserToAddChanged: (e, {value}) => {
    dispatch(userToAddValueChanged(value))
  }
})

export default connect(mapStateToProps, mapDispatchToProps) (ShareWithUsersModal)
