import React from 'react';
import 'semantic-ui-css/semantic.css'

import thunk from 'redux-thunk'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import {getCurrentlyLoggedInUser} from './actions/ActionCreators'
import RootReducer from './reducers/RootReducer'

const store = createStore(RootReducer, applyMiddleware(thunk));

import MainApp from './components/MainApp'

const App = () => (
  <Provider store={store}>
    <div className="App">
      <MainApp/>
    </div>
  </Provider>
)

store.dispatch(getCurrentlyLoggedInUser());

export default App;
