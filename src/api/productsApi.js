import jwtAxios from "../util/jwtUtil"
import { API_SERVER_HOST } from "./todoApi"

const host = `${API_SERVER_HOST}/api/products`

export const postAdd = async (product) => {
  const header = {
    headers: {
      "Content-type": "multipart/form-data",
    },
  }

  // 경로 뒤 '/' 주의
  return await jwtAxios.post(`${host}`, product, header)
}

export const getList = async (pageParam) => {
  const { page, size } = pageParam

  const res = await jwtAxios.get(`${host}/list`, { params: { page, size } })
  console.log("res.data: ", res.data)
  return res.data
}

export const getOne = async (pno) => {
  const res = await jwtAxios.get(`${host}/${pno}`)
  return res.data
}

export const putOne = async (pno, product) => {
  const header = {
    headers: {
      "Content-Type": "multipart/for-data",
    },
  }
  const res = await jwtAxios.put(`${host}/${pno}`, product, header)
  return res.data
}

export const deleteOne = async (pno) => {
  const res = await jwtAxios.delete(`${host}/${pno}`)
  return res.data
}
