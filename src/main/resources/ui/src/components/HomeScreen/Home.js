import React from 'react';
import { Link } from 'react-router-dom';
import { Segment, Button, Modal, Header, Icon, Grid, Message, Form } from 'semantic-ui-react';
import MySchemas from './MySchemas';
import SharedSchemas from './SharedSchemas'
import ShareWithUsersModal from './ShareWithUsersModal'
import {connect} from 'react-redux';
import {getHomeScreenState, getAuthenticationState} from '../../reducers/RootReducer'
import {
  removeSharedSchemaLink,
  removeMySchema,
  fetchAllUserSchemas,
  loadSharedUsers,
  openImportModal,
  closeImportModal,
  importSchema,
  dismissHomeInfo,
  importFileSelected,
  dismissHomeError,
} from '../../actions/ActionCreators'

class Home extends React.Component {

  componentWillMount() {
    this.props.fetchAllSchemas()
  }

  render() {
    const {loadingMine,
      loadingShared,
      mySchemas,
      sharedSchemas,
      onRemoveShared,
      onRemoveSchema,
      onShareButtonClicked,
      errorMessage,
      infoMessage,
      currentUser,
      fetchAllSchemas,
      importLoading,
      onFileUpload,
      importModalOpen,
      onOpenImportModal,
      onCloseImportModal,
      onImportFileChanged,
      onDismissHomeInfo,
      importFileData,
      onDismissError
    } = this.props

     const errorBox = errorMessage.length > 0 ? (
        <Message negative onDismiss={onDismissError}>
          <Message.Header>{errorMessage}</Message.Header>
        </Message>) : null

    const infoBox = infoMessage.length > 0 ? (
        <Message info onDismiss={onDismissHomeInfo}>
          <Message.Header>{infoMessage}</Message.Header>
        </Message>) : null

    const importModal = (
      <Modal open={importModalOpen}
             onClose={onCloseImportModal}
             basic
             size='small'>
        <Header icon='browser' content='Import a schema file'/>
        <Modal.Content>
          {errorBox}
          <h3>You may upload a schema...</h3>
          <Form action="post" id="myForm" onSubmit={onFileUpload(importFileData)}>
            <Form.Input type="file"
                        name="uploaded_file"
                        accept=".json"
                        onChange={onImportFileChanged}
            />
            <Button disabled={importFileData === ''}
                    type="submit" color="green"
                    loading={importLoading}>Submit
            </Button>
          </Form>
        </Modal.Content>
        <Modal.Actions>
          <Button color='green' inverted onClick={onCloseImportModal}>
            <Icon name='checkmark'/> Got it
          </Button>
        </Modal.Actions>
      </Modal>
    )

    return (
      <Segment raised={true}>
        <Button onClick={fetchAllSchemas}
               color="teal" icon={true} floated="right">
          <Icon name="refresh"/>
        </Button>
        <h2>Hello, {currentUser}!</h2>
        {infoBox}
        {errorBox}
        <ul>
          <li>
            <Link to="/create">Create a new schema</Link>
          </li>
          <li>
            Import it &nbsp;
            <Button icon={true} onClick={onOpenImportModal}>
              <Icon name="upload"/>
            </Button>
          </li>
        </ul>
        <ShareWithUsersModal errorBox={errorBox}/>
        <Grid stackable={true} columns={2}>
          <Grid.Column>
            <MySchemas schemas={mySchemas}
                     onShareClicked={onShareButtonClicked}
                     loading={loadingMine}
                     onRemoveClicked={onRemoveSchema}/>
          </Grid.Column>
          <Grid.Column>
            <SharedSchemas loading={loadingShared}
                           onRemoveShared={onRemoveShared}
                           sharedSchemas={sharedSchemas}/>
          </Grid.Column>
        </Grid>
        {importModal}
      </Segment>
    )
  }
}

const mapStateToProps = (rootState) => {
  const state = getHomeScreenState(rootState)
  const currentUser = getAuthenticationState(rootState).loggedInUser
  return {
    loadingMine: state.loadingMySchemas,
    loadingShared: state.loadingSharedWithMe,
    mySchemas: state.mySchemas,
    sharedSchemas: state.sharedWithMe,
    errorMessage: state.errorMessage,
    infoMessage: state.infoMessage,
    currentUser,
    importLoading: state.importLoading,
    importModalOpen: state.importModalOpen,
    importFileData: state.importFileData,
  }
}

const mapDispatchToProps = (dispatch) => ({
  onRemoveShared: (id) => () => {
    dispatch(removeSharedSchemaLink(id))
  },
  fetchAllSchemas: () => {
    dispatch(fetchAllUserSchemas())
  },
  onRemoveSchema: (id) => () => {
    dispatch(removeMySchema(id))
  },
  onShareButtonClicked: (id) => () => {
    dispatch(loadSharedUsers(id))
  },
  onFileUpload: (formData) => (e) => {
    //console.log('Submitting form!', e);
    if (e) {
      e.preventDefault()
    }
    if (formData)
      dispatch(importSchema(formData));
  },
  onCloseImportModal: () => {
    dispatch(closeImportModal())
  },
  onOpenImportModal: () => {
    dispatch(openImportModal())
  },
  onImportFileChanged: (e, data) => {
    let file = e.target.files[0];
    //console.log(data, file)
    let formData = new FormData()
    formData.append(data.name, file, data.value)
    dispatch(importFileSelected(formData))
  },
  onDismissHomeInfo: () => {
    dispatch(dismissHomeInfo())
  },
  onDismissError: () => {
    dispatch(dismissHomeError())
  }

})


export default connect(mapStateToProps, mapDispatchToProps)(Home);
