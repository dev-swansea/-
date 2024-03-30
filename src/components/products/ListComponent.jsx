import { useEffect, useState } from "react"
import useCustomMove from "../../hooks/useCustomMove"
import { getList } from "../../api/productsApi"
import FetchingModal from "../common/FetchingModal"
import { API_SERVER_HOST } from "../../api/todoApi"
import PageComponent from "../common/PageComponent"
import useCustomLogin from "../../hooks/useCustomLogin"

const initState = {
  dtoList: [],
  pageNumList: [],
  pageRequestDTO: null,
  prev: false,
  next: false,
  totalCount: 0,
  prevPage: 0,
  nextPage: 0,
  totalPage: 0,
  current: 0,
}

const ListComponent = () => {
  const { page, size, refresh, moveToList, moveToRead } = useCustomMove()
  const [serverData, setServerData] = useState(initState)
  const [fetching, setFetching] = useState(false)

  const { exceptionHandler } = useCustomLogin()

  const host = API_SERVER_HOST

  useEffect(() => {
    setFetching(true)
    getList({ page, size })
      .then((data) => {
        setServerData(data)
        setFetching(false)
      })
      .catch((err) => exceptionHandler(err))
  }, [page, size, refresh])

  return (
    <div className="border-2 border-blue-100 mt-10 mx-2">
      {fetching ? <FetchingModal /> : <></>}
      <div className="flex flex-wrap mx-auto p-6">
        {serverData.dtoList.map((product) => (
          <div
            key={product.pno}
            className="w-1/2 p-1 rounded shadow-md border-2"
            onClick={() => moveToRead(product.pno)}>
            <div className="flex flex-col h-full">
              {product.pno}
              <div className="text-1xl m-1 p-2 w-full flex flex-col">
                <div className="w-full overflow-hidden">
                  <img
                    className="m-auto rounded-md w-60"
                    src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`}
                    alt="대표이미지"
                  />
                </div>
                <div className="bottom-0 font-extrabold bg-white">
                  <div className="text-center p-1">이름: {product.pname}</div>
                </div>
                <div className="text-center p-1">가격: {product.price}</div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <PageComponent
        serverData={serverData}
        movePage={moveToList}></PageComponent>
    </div>
  )
}

export default ListComponent
