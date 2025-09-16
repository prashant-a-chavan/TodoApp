import axios from "axios";

const REST_API_BASE_URL = 'http://localhost:8080/api/tasks'

export const todoData = () => axios.get(REST_API_BASE_URL + '/fetch');