import React, {PropTypes} from 'react'
import {Link} from 'react-router-dom'
import {Segment, Table, Label, Icon, Button} from 'semantic-ui-react'

const SharedSchemas = (props) => {

  const {sharedSchemas, loading, onRemoveShared} = props;

  return (
    <Segment loading={loading}>
      <Label attached="top" color="violet" content="Shared with me"/>
      <Table collapsing={false}
             compact={true}
             size="small"
             unstackable={true}
      >
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell>Name</Table.HeaderCell>
            <Table.HeaderCell>From</Table.HeaderCell>
            <Table.HeaderCell>Remove</Table.HeaderCell>
          </Table.Row>
        </Table.Header>
        <Table.Body>
          {sharedSchemas.map( (item, idx) => ( // 0 -> id, 1 -> name, 2 -> owner
            <Table.Row key={idx}>
              <Table.Cell><Link to={`/view/${item[0]}`}>{item[1]}</Link></Table.Cell>
              <Table.Cell>{item[2]}</Table.Cell>
              <Table.Cell>
                <Button icon={true} onClick={onRemoveShared(item[0])}>
                  <Icon name="remove"/>
                </Button>
              </Table.Cell>
            </Table.Row>
          ) )}
        </Table.Body>
      </Table>
    </Segment>
  )

}

SharedSchemas.propTypes = {
  loading: PropTypes.bool.isRequired,
  onRemoveShared: PropTypes.func.isRequired,
  sharedSchemas: PropTypes.array.isRequired,
}

export default SharedSchemas
