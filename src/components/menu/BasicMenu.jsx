import { useSelector } from "react-redux"
import { Link } from "react-router-dom"

const BasicMenu = () => {
  const loginState = useSelector((state) => state.loginSlice)

  return (
    <nav id="navbar" className="flex bg-blue-300">
      <div className="w-4/5 bg-gray-500">
        <ul className="flex p-4 text-white font-bold">
          <li className="pr-6 text-2xl">
            <Link to={"/"}>Main</Link>
          </li>
          <li className="pr-6 text-2xl">
            <Link to={"/about/"}>About</Link>
          </li>

          {/* 로그인 사용자에게만 노출 */}
          {loginState.email ? (
            <>
              <li className="pr-6 text-2xl">
                <Link to={"/todos/"}>Todo</Link>
              </li>
              <li className="pr-6 text-2xl">
                <Link to={"/products/"}>Products</Link>
              </li>
            </>
          ) : (
            <></>
          )}
        </ul>
      </div>

      {!loginState.email ? (
        <div className="w-1/5 flex justify-end bg-orange-300 p-4 font-medium">
          <div className="text-white text-sm m-1 rounded">
            <Link to={"/member/login"}>Login</Link>
          </div>
        </div>
      ) : (
        <div className="w-1/5 flex justify-end bg-purple-300 p-4 font-medium">
          <div className="text-white text-sm m-1 rounded">
            <Link to={"/member/logout"}>Logout</Link>
          </div>
        </div>
      )}
    </nav>
  )
}

export default BasicMenu
