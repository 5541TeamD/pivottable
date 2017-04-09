import React from 'react';
import { Link } from 'react-router-dom';
import { Segment, Button, Modal, Header, Icon, Grid, Message } from 'semantic-ui-react';
import MySchemas from './MySchemas';
import SharedSchemas from './SharedSchemas'
import ShareWithUsersModal from './ShareWithUsersModal'
import {connect} from 'react-redux';
import {getHomeScreenState} from '../../reducers/RootReducer'
import {removeSharedSchemaLink, removeMySchema, fetchAllUserSchemas, loadSharedUsers} from '../../actions/ActionCreators'

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
      infoMessage } = this.props

    const errorBox = errorMessage.length > 0 ? (
        <Message negative>
          <Message.Header>{errorMessage}</Message.Header>
        </Message>) : null

    const infoBox = infoMessage.length > 0 ? (
        <Message info>
          <Message.Header>{infoMessage}</Message.Header>
        </Message>) : null

    return (
      <Segment>
        <h2>Welcome Home User!</h2>
        {infoBox}
        {errorBox}
        <ul>
          <li>
            <Link to="/create">Create a new schema</Link>
          </li>
          <li>
            Import it &nbsp;
            <Modal
              trigger={<Button icon={true}><Icon name="upload"/></Button>}
              basic
              size='small'
            >
              <Header icon='browser' content='Upload a schema policy'/>
              <Modal.Content>
                <h3>You may upload a schema... TODO</h3>
              </Modal.Content>
              <Modal.Actions>
                <Button color='green' inverted>
                  <Icon name='checkmark'/> Got it
                </Button>
              </Modal.Actions>
            </Modal>
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
      </Segment>
    )
  }
}

const mapStateToProps = (rootState) => {
  const state = getHomeScreenState(rootState)
  return {
    loadingMine: state.loadingMySchemas,
    loadingShared: state.loadingSharedWithMe,
    mySchemas: state.mySchemas,
    sharedSchemas: state.sharedWithMe,
    errorMessage: state.errorMessage,
    infoMessage: state.infoMessage,
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
  }

})


export default connect(mapStateToProps, mapDispatchToProps)(Home);
