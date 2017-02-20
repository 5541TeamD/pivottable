import React, {Component} from 'react'
import axios from 'axios'
import {Segment, Button, Label, Grid, Table } from 'semantic-ui-react'
import 'semantic-ui-css/semantic.css'

export default class TableList extends Component {

  constructor(props) {
    super(props)
    this.state = {
      tables: [],
      loading: false
    }
    this.fetchTables = this.fetchTables.bind(this)
  }

  fetchTables() {
    this.setState({...this.state, loading: true})
    axios.get('/api/tables').then( (resp) => {
       this.setState({...this.state, tables: resp.data, loading: false})
    } )
    .catch((e) => {
      console.log('Oops', e)
      this.setState({...this.state, tables: [], loading: false});
    })
  }

  render() {
    return (
    <Grid.Column>
      <Segment raised loading={this.state.loading}>
        <Label as="a" color="blue" onClick={this.fetchTables}>Fetch Tables</Label>
        <h2>Tables found</h2>
        <Table celled>
          <Table.Header>
            <Table.Row>
              {this.state.tables.map ((it) => <Table.HeaderCell>{it}</Table.HeaderCell>)}
            </Table.Row>
          </Table.Header>
        </Table>
      </Segment>
    </Grid.Column>
    )
  }

}
