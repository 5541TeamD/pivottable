import {C} from './RootReducer'

const initialState = {
  mySchemas: [], // id, name
  sharedWithMe: [], // id, name, from
  loadingMySchemas: false,
  loadingSharedWithMe: false,
  loadingSharedWithUsers: false,
  selectedSchemaId: '',
  loadingSharedUsers: false,
  sharedWithUsers: [], // users [string]
  userToAdd: '',
  infoMessage : '',
  errorMessage: '',
  importModalOpen: false,
  importLoading: false,
  importFileData: '',
}


const homeScreenReducer = (state = initialState, action) => {
  const {type} = action
  switch (type) {
    case C.FETCH_ALL_USER_SCHEMAS:
      return {...state, loadingMySchemas: true, loadingSharedWithMe: true}
    case C.FETCH_ALL_USER_SCHEMAS_SUCCESS:
      return {
        ...state,
        loadingMySchemas: false,
        loadingSharedWithMe: false,
        mySchemas: action.schemas.mySchemas,
        sharedWithMe: action.schemas.sharedWithMe,
        errorMessage: '',
      }
    case C.FETCH_ALL_USER_SCHEMAS_FAILURE:
      return {
        ...state,
        loadingMySchemas: false,
        loadingSharedWithMe: false,
        mySchemas: [],
        sharedWithMe: [],
        errorMessage: action.message
      }
    case C.REMOVE_SHARED_SCHEMA:
      return {
        ...state,
        loadingSharedWithMe: true,
      }
    case C.REMOVE_SHARED_SCHEMA_SUCCESS:
      return {
        ...state,
        loadingSharedWithMe: false,
        sharedWithMe: state.sharedWithMe.filter (it => it[0] !== action.id),
        errorMessage: '',
      }
    case C.REMOVE_SHARED_SCHEMA_FAILURE:
      return {
        ...state,
        loadingSharedWithMe: false,
        errorMessage: action.message
      }
    case C.DELETE_MY_SCHEMA:
      return {
        ...state,
        loadingMySchemas: true
      }
    case C.DELETE_MY_SCHEMA_SUCCESS:
      return {
        ...state,
        loadingMySchemas: false,
        errorMessage: '',
        mySchemas: state.mySchemas.filter (it => it[0] !== action.id)
      }
    case C.DELETE_MY_SCHEMA_FAILURE:
      return {
        ...state,
        loadingMySchemas: false,
        errorMessage: action.message
      }
    case C.STOP_SHARING_SCHEMA:
      return {
        ...state,
        loadingSharedUsers: true,
      }
    case C.STOP_SHARING_SCHEMA_SUCCESS:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: '',
        sharedWithUsers: state.sharedWithUsers.filter( name => name !== action.otherUser )
      }
    case C.STOP_SHARING_SCHEMA_FAILURE:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: action.message,
      }
    case C.ADD_USER_TO_SHARED_SCHEMA:
      return {
        ...state,
        loadingSharedUsers: true
      }
    case C.ADD_USER_TO_SHARED_SCHEMA_SUCCESS:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: '',
        userToAdd: '',
        sharedWithUsers: [...state.sharedWithUsers, action.user].sort()
      }
    case C.ADD_USER_TO_SHARED_SCHEMA_FAILURE:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: action.message
      }
    case C.LOAD_SHARED_USERS:
      return {
        ...state,
        loadingSharedUsers: true,
        selectedSchemaId: action.id
      }
    case C.LOAD_SHARED_USERS_SUCCESS:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: '',
        sharedWithUsers: action.sharedUsers
      }
    case C.LOAD_SHARED_USERS_FAILURE:
      return {
        ...state,
        loadingSharedUsers: false,
        errorMessage: action.message,
        selectedSchemaId: '',
      }
    case C.CLOSE_SHARED_USERS_MODAL:
      return {
        ...state,
        selectedSchemaId: '',
        loadingSharedUsers: false,
        sharedWithUsers: [],
      }
    case C.NEW_USER_TO_SHARE_VALUE_CHANGED:
      return {
        ...state,
        userToAdd: action.value,
      }
    case C.IMPORT_SCHEMA_MODAL_OPEN:
      return {
        ...state,
        importModalOpen: true
      }
    case C.IMPORT_SCHEMA_MODAL_CLOSE:
      return {
        ...state,
        importModalOpen: false,
      }
    case C.IMPORT_SCHEMA:
      return {
        ...state,
        importLoading: true
      }
    case C.IMPORT_SCHEMA_FAILURE:
      return {
        ...state,
        errorMessage: action.message,
        importLoading: false,
      }
    case C.IMPORT_SCHEMA_SUCCESS:
      return {
        ...state,
        importLoading: false,
        errorMessage: '',
        infoMessage: 'Schema imported successfully!',
        importModalOpen: false,
        importFileData: initialState.importFileData,
      }
    case C.HOME_DISMISS_INFO:
      return {
        ...state,
        infoMessage: ''
      }
    case C.IMPORT_SCHEMA_FILE_SELECTED:
      return {
        ...state,
        importFileData: action.value
      }
    default:
      return state;
  }
}

export default homeScreenReducer;
