import { useEffect } from "react"
import { useSearchParams } from "react-router-dom"
import { getAccessToken, getMemberWithAccessToken } from "../../api/kakaoApi"
import useCustomLogin from "../../hooks/useCustomLogin"

const KakaoRedirectPage = () => {
  const [searchParams] = useSearchParams()
  const authCode = searchParams.get("code")
  const { moveToPath, saveAsCookie } = useCustomLogin()

  useEffect(() => {
    getAccessToken(authCode).then((accessToken) => {
      getMemberWithAccessToken(accessToken).then((memberInfo) => {
        console.log("-------------------------")
        console.log("kakao memberInfo: ", memberInfo)

        saveAsCookie(memberInfo)

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
