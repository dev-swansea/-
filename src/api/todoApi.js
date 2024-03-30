import jwtAxios from "../util/jwtUtil"

export const API_SERVER_HOST = "http://localhost:8080"

const prefix = `${API_SERVER_HOST}/api/todo`

export const getOne = async (tno) => {
  const res = await jwtAxios.get(`${prefix}/${tno}`)
  return res.data
}

export const getList = async (pageParam) => {
  const { page, size } = pageParam

  const res = await jwtAxios.get(`${prefix}/list`, { params: { page, size } })
  return res.data
}

export const todoAdd = async (todoObj) => {
  const res = await jwtAxios.post(`${prefix}/`, todoObj)
  return res.data
}

export const modifyOne = async (todo) => {
  const res = await jwtAxios.put(`${prefix}/${todo.tno}`, todo)
  return res.data
}

export const deleteOne = async (tno) => {
  const res = await jwtAxios.delete(`${prefix}/${tno}`)
  return res.data
}
