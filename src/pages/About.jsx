import BasicLayout from "../layout/BasicLayout"
import useCustomLogin from "../hooks/useCustomLogin"

const About = () => {
  const { isLogin, moveToLoginReturn } = useCustomLogin()

  if (!isLogin) {
    return moveToLoginReturn()
  }

  return (
    <div>
      <BasicLayout />
    </div>
  )
}

export default About
