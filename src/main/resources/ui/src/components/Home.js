import React from 'react';
import { Link } from 'react-router-dom';
import { Segment } from 'semantic-ui-react';

const Home = () => (
  <Segment>
    <h2>Welcome Home User!</h2>
    <ul>
      <li>
        <Link to="/create">Create a new schema</Link>
      </li>
    </ul>
  </Segment>
)


export default Home;
