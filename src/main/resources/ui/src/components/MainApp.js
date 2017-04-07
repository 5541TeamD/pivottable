import React from 'react'
import { Route, withRouter} from 'react-router-dom';
//import createBrowserHistory from 'history/createBrowserHistory';
import PrivateRoute from './Utils/PrivateRoute'

import {Segment, Dimmer, Loader, Image} from 'semantic-ui-react'
import Home from './Home'
import AppHeader from './AppHeader'
import Footer from './Footer'
import LoginScreen from './Login/LoginScreen'
import PivotTableApp from './PivotTableApp'
import {getAuthenticationState} from '../reducers/RootReducer'

import {connect} from 'react-redux'
import {trainTracks} from '../images/train_tracks.jpg'


const MainApp = ({isAppLoading}) => {
  console.log(location.pathname)
  return (
    isAppLoading ? (
        <Segment>
          <Dimmer active>
            <Loader size='massive'>Loading</Loader>
          </Dimmer>
          <Image src={trainTracks} />
        </Segment>
      )
      : (
          <div>
            <AppHeader/>
            <Image src={trainTracks} />
            <PrivateRoute exact={true} path="/" component={Home} />
            <Route exact={true} path="/login" component={LoginScreen}/>
            <PrivateRoute exact={true} path="/create" component={PivotTableApp} />
            <Footer/>
          </div>
        )
  )
}

const mapStateToProps = (rootState) => ({
  isAppLoading: getAuthenticationState(rootState).loggedInUser === undefined
})

export default withRouter(connect(mapStateToProps)(MainApp));
