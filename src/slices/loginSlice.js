import { createSlice, createAsyncThunk } from "@reduxjs/toolkit"
import { loginPost } from "../api/memberApi"
import { getCookie, removeCookie, setCookie } from "../util/cookieUtil"

const initState = {
  email: "",
}

const loadMemberCookie = () => {
  // 쿠키에서 로그인 정보 로딩
  const memberInfo = getCookie("member")

  if (memberInfo && memberInfo.nickname) {
    memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
  }
  return memberInfo
}

export const loginPostAsync = createAsyncThunk("loginPostAsync", (param) => {
  return loginPost(param)
})

const loginSlice = createSlice({
  name: "LoginSlice",
  initialState: loadMemberCookie() || initState, // 쿠키가 없다면 초기값 사용
  reducers: {
    login: (state, action) => {
      console.log("login...", action)

      // 소셜로그인 회원이 사용
      const payload = action.payload
      setCookie("member", JSON.stringify(payload), 1)

      return payload
    },
    logout: (state, action) => {
      console.log("logout...")

      removeCookie("member")

      return { ...initState }
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        console.log("fulfilled")
        const payload = action.payload

        if (!payload.error) {
          setCookie("member", JSON.stringify(payload), 1) // 쿠키 저장 기간은 1일
        }
        return payload
      })
      .addCase(loginPostAsync.pending, (state, action) => {
        console.log("pending")
      })
      .addCase(loginPostAsync.rejected, (state, action) => {
        console.log("rejected")
      })
  },
})

export const { login, logout } = loginSlice.actions
export default loginSlice.reducer
