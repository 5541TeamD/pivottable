import React, {PropTypes} from 'react'
import {Segment, Table, Label, Icon, Button} from 'semantic-ui-react'
import {Link} from 'react-router-dom'

const MySchemas = (props) => {
  const {schemas, onShareClicked, loading, onRemoveClicked} = props
  return (
    <Segment loading={loading}>
      <Label attached="top" color="orange">My Schemas</Label>
      <Table size="small"
             unstackable={true}
             compact={true}
             collapsing={false}>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Name</Table.HeaderCell>
            <Table.HeaderCell>Share</Table.HeaderCell>
            <Table.HeaderCell>Export</Table.HeaderCell>
            <Table.HeaderCell>Remove</Table.HeaderCell>
          </Table.Row>
        </Table.Header>
        <Table.Body>
          {schemas.map ( (item, idx) => ( // item[0] -> id, item[1] -> schema name
            <Table.Row key={idx}>
              <Table.Cell>
                <Link to={`/edit/${item[0]}`} >{item[1]}</Link>
              </Table.Cell>
              <Table.Cell>
                <Button icon={true} onClick={onShareClicked(item[0])}>
                  <Icon name="external share"/>
                </Button>
              </Table.Cell>
              <Table.Cell>
                <a href={`/export_schema/${item[0]}`}>Export</a>
              </Table.Cell>
              <Table.Cell>
                <Button icon={true} onClick={onRemoveClicked(item[0])}>
                  <Icon name="remove"/>
                </Button>
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </Segment>
  )
}

MySchemas.propTypes = {
  schemas: PropTypes.array.isRequired,
  onShareClicked: PropTypes.func.isRequired,
  loading: PropTypes.bool.isRequired,
  onRemoveClicked: PropTypes.func.isRequired,
}


export default MySchemas;
