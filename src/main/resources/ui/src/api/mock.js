
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
        {name: 'age', type: 'numeric', alias: 'Age'},
        {name: 'name', type: 'string', alias: 'Name'},
        {name: 'id', type: 'string', alias: 'Student ID'}
      ],
      rows: [
        [18, 'John Titor', '42632051'],
        [24, 'Ben Bishop', '40000000'],
        [22, 'PK Subban', '40000010'],
        [23, 'Alex Gally', '40010010']
      ]
    }
  })
}
