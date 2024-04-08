import { createSearchParams, Navigate, useNavigate } from "react-router-dom"
import { useRecoilState, useResetRecoilState } from "recoil"
import { loginPost } from "../api/memberApi"
import signinState from "../atom/signinState"
import { removeCookie, setCookie } from "../util/cookieUtil"
import { cartState } from "../atom/cartState"

const useCustomLogin = () => {
  const navigate = useNavigate()
  const [loginState, setLoginState] = useRecoilState(signinState)
  const resetState = useResetRecoilState(signinState)

  const resetCartState = useResetRecoilState(cartState)

  const isLogin = loginState.email ? true : false // 로그인 여부

  const doLogin = async (loginParam) => {
    const result = await loginPost(loginParam)
    console.log("recoil login result: ", result)
    saveAsCookie(result)
    return result
  }

  const saveAsCookie = (data) => {
    setCookie("member", JSON.stringify(data), 1)
    setLoginState(data)
  }

  const doLogout = () => {
    removeCookie("member")
    resetState()
    resetCartState()
  }

  const moveToPath = (path) => {
    navigate({ pathname: path }, { replace: true })
  }

  const moveToLogin = () => {
    navigate({ pathname: "/member/login" }, { replace: true })
  }

  const moveToLoginReturn = () => {
    return <Navigate replace to="/member/login" />
  }

  const exceptionHandler = (ex) => {
    console.log("Exception ---------------------")
    console.log("ex:", ex)

    if (ex === "ERROR_ACCESSDENIED") {
      alert("해당 메뉴를 사용할 수 있는 권한이 없습니다.")
      navigate({ pathname: "/member/login", search: "ERROR_ACCESSDENIED" })
      return
    }

    const errorMsg = ex.response.data.error
    const errorStr = createSearchParams({ error: errorMsg }).toString()
    if (errorMsg === "REQUEST_LOGIN") {
      alert("해당 서비스는 로그인이 필요합니다.")
      navigate({ pathname: "/member/login", search: errorStr })
      return
    }
  }

  return {
    loginState,
    isLogin,
    doLogin,
    doLogout,
    moveToPath,
    moveToLogin,
    moveToLoginReturn,
    exceptionHandler,
    saveAsCookie,
  }
}

export default useCustomLogin
