import { useEffect } from "react"
import { useSearchParams } from "react-router-dom"
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi"
import { useDispatch } from "react-redux"
import { login } from "../../slices/loginSlice"
import useCustomLogin from "../../hooks/useCustomLogin"

const KakaoRedirectPage = () => {
  const [searchParams] = useSearchParams()
  const authCode = searchParams.get("code")
  const dispatch = useDispatch()
  const { moveToPath } = useCustomLogin()

  useEffect(() => {
    getAccessToken(authCode).then((data) => {
      getMemberWithAccessToken(data).then((memberInfo) => {
        dispatch(login(memberInfo))

        // 소셜 회원이 아니라면
        if (memberInfo && !memberInfo.social) {
          moveToPath("/")
        } else {
          moveToPath("/member/modify")
        }
      })
    })
  }, [authCode])

  return (
    <div>
      <div>KakaoRedirectPage</div>
      <div>{authCode}</div>
    </div>
  )
}

export default KakaoRedirectPage
