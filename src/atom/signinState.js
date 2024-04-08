import { atom } from "recoil"
import { getCookie } from "../util/cookieUtil"

const initState = {
  email: "",
  nickname: "",
  social: false,
  accessToken: "",
  refreshToken: "",
}

const loadMemberFromCookie = () => {
  const memberInfo = getCookie("member")

  // 닉네임 처리
  if (memberInfo && memberInfo.nickname) {
    memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
  }
  return memberInfo
}

const signinState = atom({
  key: "signinState",
  default: loadMemberFromCookie() || initState,
})

export default signinState
