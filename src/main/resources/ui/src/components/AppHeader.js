import React  from 'react';
import { Link } from 'react-router-dom';

import { Menu, Header, Segment } from 'semantic-ui-react';
import { Item } from 'semantic-ui-react';
import {connect} from 'react-redux';
import {doLogout} from '../actions/ActionCreators'
import {getAuthenticationState} from '../reducers/RootReducer'

const AppHeader = ({onLogout, isLoggedIn}) => {
  return (
    <Segment.Group>
      <Menu secondary attached="top">
        {isLoggedIn ? (
        <Menu.Menu position="right">
            <Menu.Item name="home"><Link to="/">Home</Link></Menu.Item>
            <Menu.Item name="logout" onClick={onLogout}>Logout</Menu.Item>
        </Menu.Menu>)
         :
        (<Menu.Menu position="right">
          <Menu.Item name="login"><Link to="/login">Login</Link></Menu.Item>
        </Menu.Menu>)
        }
      </Menu>
      <Header as="h1" block>
        <Item.Group>
          <Item>
            <Item.Content verticalAlign="middle">
              <Item.Header>D Pivot Table App</Item.Header>
            </Item.Content>
          </Item>
        </Item.Group>
      </Header>
    </Segment.Group>
  );
}

const mapStateToProps = (rootstate) => {
  const state = getAuthenticationState(rootstate)
  return {
    isLoggedIn: state.loggedInUser !== null
  }
}

const mapDispatchToProps = (dispatch) => ({
  onLogout: () => {dispatch(doLogout())}
})

export default connect(mapStateToProps, mapDispatchToProps)(AppHeader);
