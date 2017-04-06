import axios from 'axios'

const buildCustomHeader = (sourceUrl, username, password) => ({
  jdbcUrl: sourceUrl,
  username,
  password
})


const instance = axios.create({
  headers: {'X-Requested-With': 'XMLHttpRequest'},
  timeout: 60000 // 60 seconds
})

export const getTableList = (dataSource, username, password) => {
  return instance.get('/api/tables', {
    headers: buildCustomHeader(dataSource, username, password)
  })
}

export const checkAccess = (sourceName, username, password) => {
  return instance.get('/api/checkaccess', {
    /*params: {
      sourceName,
      username: username,
      password: password
    },*/
    headers: buildCustomHeader(sourceName, username, password)
  })
}

// TODO for next iteration add usename and password to these calls
export const getRawReport = (tableName, dataSource, username, password) => {
  return instance.get('/api/rawreport', {
    params: {
      tablename: tableName
    },
    headers: buildCustomHeader(dataSource, username, password)
  })
}


export const getPivotTable = (schema, dataSource, username, password) => {
  return instance.post('/api/pivottable', schema, {
    headers: buildCustomHeader(dataSource, username, password),
  })
}

export const login = (username, password) => {
  return instance.get('/api/login', {
    params: {
      username,
      password
    }
  })
}

export const logout = () => {
  return instance.get('/api/logout')
}

export const register = (username, password) => {
  return instance.get('/api/register', {
    params: {
      username,
      password
    }
  })
}

export const getUserInfo = () => {
  return instance.get('/api/userinfo')
}
