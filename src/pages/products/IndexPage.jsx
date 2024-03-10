import { useCallback } from "react"
import { useNavigate, Outlet } from "react-router-dom"
import BasicLayout from "../../layout/BasicLayout"
const IndexPage = () => {
  const navigate = useNavigate()

  const handleClickList = useCallback(() => {
    navigate({ pathname: "list" })
  })

  const handleClickAdd = useCallback(() => {
    navigate({ pathname: "add" })
  })

  return (
    <BasicLayout>
      <div className="text-black font-extrabold -mt-5">Products Menu</div>

      <div className="w-full flex m-2 p-2">
        <div
          className="text-xl m-1 p-2 w-20 font-extrabold text-center underline"
          onClick={handleClickList}
        >
          LIST
        </div>
        <div
          className="text-xl m-1 p-2 w-20 font-extrabold text-center underline"
          onClick={handleClickAdd}
        >
          ADD
        </div>
      </div>

      <div className="flex flex-wrap w-full">
        <Outlet />
      </div>
    </BasicLayout>
  )
}

export default IndexPage
