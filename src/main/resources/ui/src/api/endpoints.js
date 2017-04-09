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

export const getShareableSchema = (id) => {
  return instance.get(`/api/shareableschema/${id}`)
}

export const postShareableSchema = (shareableSchema) => {
  return instance.post('/api/shareableschema', shareableSchema)
}

export const getAllUserSchemas = () => {
  return instance.get('/api/home_schemas')
}

export const deleteSharedSchemaLink = (id) => {
  return instance.delete(`/api/delete_shared_schema_link/${id}`)
}

export const deleteSchema = (id) => {
  return instance.delete(`/api/delete_schema/${id}`)
}

export const deleteSharingWithUser = (otherUser, id) => {
  return instance.delete(`/api/stop_sharing_schema`, {
    params: {
      id,
      otherUser: otherUser
    }
  })
}

export const getSharedUsers = (schemaId) => {
  return instance.get(`/api/shared_users/${schemaId}`)
}

export const putSharedUserOnSchema = (schemaId, user) => {
  return instance.put('/api/shared_user', null, {
    params: {
      schemaId,
      user
    }
  })
}
