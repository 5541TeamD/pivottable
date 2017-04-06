import React from 'react'
import { BrowserRouter as Router, Route} from 'react-router-dom';
//import createBrowserHistory from 'history/createBrowserHistory';
import PrivateRoute from './Utils/PrivateRoute'

import Home from './Home'
import AppHeader from './AppHeader'
import Footer from './Footer'
import LoginScreen from './Login/LoginScreen'
import PivotTableApp from './PivotTableApp'

//const history = createBrowserHistory();

const MainApp = () => (
  <Router>
    <div>
      <AppHeader/>
      <PrivateRoute path="/" component={Home} />
      <Route exact path="/login" component={LoginScreen}/>
      <Route path="/create" component={PivotTableApp} />
      <Footer/>
    </div>
  </Router>
)

export default MainApp;
