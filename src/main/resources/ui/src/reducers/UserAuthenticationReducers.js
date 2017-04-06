import {C} from './RootReducer'

export const loginReducer = (state = {
  loggedInUser: null,
  loadingUserInfo: false
}, action) => {
  const {type} = action;
  switch (type) {
    case C.FETCH_USER_INFO:
      return {...state, loadingUserInfo: true}
    case C.FETCH_USER_INFO_SUCCESS:
    case C.LOGIN_SUBMIT_SUCCESS:
      return {...state, loadingUserInfo: false, loggedInUser: action.user}
    case C.FETCH_USER_INFO_FAILURE:
    case C.LOGIN_SUBMIT_FAILURE:
    case C.LOGOUT_SUCCESS:
      return {...state, loadingUserInfo: false, loggedInUser: null}
    default:
      return state;
  }
}

export const loginFormReducer = (state = {
  username: '',
  password: '',
  loading: false,
  errorMessage: '',
}, action) => {
  const {type} = action;
  switch (type) {
    case C.LOGIN_USERNAME_CHANGED:
      return {...state, username: action.value}
    case C.LOGIN_PASSWORD_CHANGED:
      return {...state, password: action.value}
    case C.LOGIN_SUBMIT:
      return {...state, loading: true}
    case C.LOGIN_SUBMIT_FAILURE:
      return {...state, loading: false, errorMessage: action.message}
    case C.LOGIN_SUBMIT_SUCCESS:
    case C.FETCH_USER_INFO_SUCCESS:
      return {...state, errorMessage: '', password: '', loading: false}
    default:
      return state;
  }
}

function computePwdErrorMessage(pwA, pwB) {
  if (pwB.length > 0 && pwB !== pwA) {
    return "Passwords don't match";
  }
  return '';
}

export const registerFormReducer = (state = {
  username: '',
  password1: '',
  password2: '',
  loading: false,
  errorMessage: '',
  infoMsg: '',
}, action) => {
  const {type} = action
  switch (type) {
    case C.REGISTER_USERNAME_CHANGED:
      return {...state, username: action.value};
    case C.REGISTER_PASSWORD1_CHANGED:
      return {...state,
        password1: action.value,
        errorMessage: computePwdErrorMessage(action.value, state.password2)
      };
    case C.REGISTER_PASSWORD2_CHANGED:
      return {
        ...state,
        password2: action.value,
        errorMessage: computePwdErrorMessage(state.password1, action.value)
      };
    case C.REGISTER_SUBMIT:
      return {...state, loading: true}
    case C.REGISTER_SUBMIT_FAILURE:
      return {...state, loading: false, errorMessage: action.message, infoMsg: ''};
    case C.REGISTER_SUBMIT_SUCCESS:
      return {...state,
        infoMsg: 'Registered Successfully. You may now login.',
        errorMessage: '',
        password1: '',
        password2: '',
        loading: false,
      }
    case C.LOGIN_SUBMIT_SUCCESS:
      return registerFormReducer(undefined, {type: '----irrelevant---'})
    default:
      return state;
  }
}
