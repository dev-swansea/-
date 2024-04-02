import { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import { modifyMember } from "../../api/memberApi"
import useCustomLogin from "../../hooks/useCustomLogin"
import ResultModal from "../common/ResultModal"

const initState = {
  email: "",
  pw: "",
  nickname: "",
}

const ModifyComponent = () => {
  const loginInfo = useSelector((state) => state.loginSlice)
  const [member, setMember] = useState(initState)
  const [result, setResult] = useState()

  const { moveToLogin } = useCustomLogin()

  const handleClickModify = () => {
    modifyMember(member).then((data) => {
      setResult("Modified")
    })
  }

  const closeModal = () => {
    setResult(null)
    console.log("엄지야 임신시키자")
    moveToLogin()
  }

  useEffect(() => {
    setMember({ ...loginInfo, pw: "ABCD" })
  }, [loginInfo])

  const handleChange = (e) => {
    setMember((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }))
  }

  return (
    <div className="mt-6">
      {/* 수정 후 결과 모달 */}
      {result ? (
        <ResultModal
          title={"회원정보"}
          content={"정보수정완료"}
          callbackFn={closeModal}
        />
      ) : (
        <></>
      )}

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Email</div>
          <input
            type="text"
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="email"
            value={member.email}
            readOnly
          />
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Password</div>
          <input
            type="text"
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="pw"
            value={member.pw}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Nickname</div>
          <input
            type="text"
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="nickname"
            value={member.nickname}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap justify-end">
          <button
            className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
            onClick={handleClickModify}>
            Modify
          </button>
        </div>
      </div>
    </div>
  )
}
export default ModifyComponent
