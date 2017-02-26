import React, { Component } from 'react';
import PivotTableApp from './components/PivotTableApp'
import 'semantic-ui-css/semantic.css'

import thunk from 'redux-thunk'
import { Provider } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'

import RootReducer from './reducers/RootReducer'

const store = createStore(RootReducer, applyMiddleware(thunk));

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <div className="App">
          <PivotTableApp/>
        </div>
      </Provider>
    );
  }
}

export default App;
