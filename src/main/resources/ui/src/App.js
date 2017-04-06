import React, { Component } from 'react';
import 'semantic-ui-css/semantic.css'

import thunk from 'redux-thunk'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'

import RootReducer from './reducers/RootReducer'

const store = createStore(RootReducer, applyMiddleware(thunk));

import MainApp from './components/MainApp'

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <div className="App">
          <MainApp/>
        </div>
      </Provider>
    );
  }
}

export default App;
