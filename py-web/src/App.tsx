import React from 'react';
import Jumbotron from 'react-bootstrap/Jumbotron'
import Container from 'react-bootstrap/Container'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import './App.scss';

class App extends React.Component {

  render() {
    return (
      <Container>
        <Jumbotron>
          <h1 className="header">Welcome To React-Bootstrap Typescript Example</h1>
        </Jumbotron>
      </Container>
    );
  }
}

export default App;
