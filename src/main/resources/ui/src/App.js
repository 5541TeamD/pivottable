import React, { Component } from 'react';
import './App.css';
import TableList from './components/TableList'
import 'semantic-ui-css/semantic.css'

class App extends Component {
  render() {
    return (
      <div className="App">
        <TableList/>
      </div>
    );
  }
}

export default App;
