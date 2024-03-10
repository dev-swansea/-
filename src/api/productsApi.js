import axios from "axios"
import { API_SERVER_HOST } from "./todoApi"

const host = `${API_SERVER_HOST}/api/products`

export const postAdd = async (product) => {
  const header = {
    headers: {
      "Content-type": "multipart/form-data",
    },
  }

  // 경로 뒤 '/' 주의
  const res = await axios.post(`${host}`, product, header)
  return res
}
