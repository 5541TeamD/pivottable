
// mock a delay of 800ms for an ajax request
const delay = (obj) => {
  return new Promise( (resolve) => {
    setTimeout(() => {
      resolve(obj)
    }, 800)
  })
}

export const getTableList = () => {
  console.log('mock fetching table list')
  return delay({
    status: 200,
    data: [
      'Table A',
      'Table B',
      'Table C'
    ]
  })
}

export const checkAccess = (sourceName, username, password) => {
  console.log('mock checking access', sourceName, username, password)
  return delay({
    status: 200,
    data: {
      message: 'success'
    }
  })
}

export const getRawReport = (tableName) => {
  console.log('mock getting raw report', tableName)
  return delay({
    status: 200,
    data: {
      columns: [
        {name: 'age', type: 'TYPE_NUMERIC', alias: 'Age'},
        {name: 'name', type: 'TYPE_STRING', alias: 'Name'},
        {name: 'id', type: 'TYPE_STRING', alias: 'Student ID'},
        {name: 'team', type: 'TYPE_STRING', alias: 'Team'}
      ],
      rows: [
        [18, 'John Titor', '42632051', 'Edmonton Oilers'],
        [24, 'Ben Bishop', '40000000', 'Los Angeles Kings'],
        [22, 'PK Subban', '40000010', 'Montreal Canadiens'],
        [23, 'Alex Gally', '40010010', 'Montreal Canadiens']
      ]
    }
  })
}
