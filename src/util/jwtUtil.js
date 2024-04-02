import axios from "axios"
import { getCookie, setCookie } from "./cookieUtil"
import { API_SERVER_HOST } from "../api/todoApi"

const jwtAxios = axios.create()

const refreshJWT = async (accessToken, refreshToken) => {
  const host = API_SERVER_HOST
  const header = {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  }

  const res = await axios.get(
    `${host}/api/member/refresh?refreshToken=${refreshToken}`,
    header
  )

  console.log("---------------------")
  console.log("res.data: ", res.data)
  return res.data
}

const beforeReq = (config) => {
  console.log("before request ......")
  const memberInfo = getCookie("member")

  if (!memberInfo) {
    console.log("Member Not Found")
    return Promise.reject({
      response: {
        data: {
          error: "REQUEST_LOGIN",
        },
      },
    })
  }

  const { accessToken } = memberInfo

  // Authorization 헤더 처리
  config.headers.Authorization = `Bearer ${accessToken}`

  console.log("config: ", config)
  return config
}

const requestFail = (err) => {
  console.log("request failed ......")

  return Promise.reject(err)
}

// before reurn response
const beforeRes = async (res) => {
  console.log("before return response ......")
  console.log("res: ", res)

  const data = res.data

  if (data && data.error === "ERROR_ACCESS_TOKEN") {
    const memberCookieValue = getCookie("member")

    const result = await refreshJWT(
      memberCookieValue.AccessToken,
      memberCookieValue.RefreshToken
    )
    console.log("refresh JWT result => ", result)

    memberCookieValue.AccessToken = result.accessToken
    memberCookieValue.RefreshToken = result.refreshToken

    setCookie("member", JSON.stringify(memberCookieValue), 1)

    // 재호출
    const originalRequest = res.config
    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`
    return await axios(originalRequest)
  }

  return res
}

// fail response
const responseFail = (err) => {
  console.log("response fail error ......")
  return Promise.reject(err)
}

// axios 요청이나 응답시에 추가적인 작업 설정
jwtAxios.interceptors.request.use(beforeReq, requestFail)
jwtAxios.interceptors.response.use(beforeRes, responseFail)

export default jwtAxios
