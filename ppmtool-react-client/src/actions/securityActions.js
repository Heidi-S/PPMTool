import axios from "axios";
import setJWTToken from "../securityUtils/setJWTToken";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async (dispatch) => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const login = (LoginRequest) => async (dispatch) => {
  try {
    //post login request
    const res = await axios.post("/api/users/login", LoginRequest);

    //extract token from res.data
    const { token } = res.data;
    //store the token in the local storage
    localStorage.setItem("jwtToken", token);
    //set our token in the header
    setJWTToken(token);
    //decode the toke on React
    const decoded = jwt_decode(token);
    //distpatch to our securityReducer
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded,
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const logout = () => (dispatch) => {
  localStorage.removeItem("jwtToken");
  setJWTToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {},
  });
};
