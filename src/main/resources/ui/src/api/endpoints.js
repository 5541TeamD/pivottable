import axios from 'axios'

const instance = axios.create({
  headers: {'X-Requested-With': 'XMLHttpRequest'},
  timeout: 5000 // 5 seconds
})

export const getTableList = () => {
  return instance.get('/api/tables')
}

export const checkAccess = (sourceName, username, password) => {
  return instance.get('/api/checkaccess', {
    params: {
      sourceName,
      username: username,
      password: password
    }
  })
}

// TODO for next iteration add usename and password to these calls
export const getRawReport = (tableName) => {
  return instance.get('/api/rawreport', {
    params: {
      tablename: tableName
    }
  })
}


export const getPivotTable = (schema) => {
  return instance.post('/api/pivottable', {
    data: schema
  })
}
