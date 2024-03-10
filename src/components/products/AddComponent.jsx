import { useRef, useState } from "react"
import { postAdd } from "../../api/productsApi"
import FetchingModal from "../common/FetchingModal"
import ResultModal from "../common/ResultModal"

const initState = {
  pname: "",
  pdesc: "",
  price: 0,
  files: [],
}

const AddComponent = () => {
  // 객체를 복사해서 넣으니 product의 값이 변경되어도 initState의 값은 변경되지 않는다.
  const [product, setProduct] = useState({ ...initState })
  const uploadRef = useRef()

  const [fetching, setFeching] = useState(false)
  const [result, setResult] = useState(null)

  const handleChangeProduct = (e) => {
    product[e.target.name] = e.target.value
    setProduct({ ...product })
  }

  const handleClickAdd = (e) => {
    const files = uploadRef.current.files
    const formData = new FormData()

    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i])
    }

    // other data
    formData.append("pname", product.pname)
    formData.append("pdesc", product.pdesc)
    formData.append("price", product.price)

    for (const pair of formData.entries()) {
      console.log([pair[0], pair[1]])
    }

    setFeching(true)
    postAdd(formData).then((data) => {
      setFeching(false)
      setResult(data.data.result)
    })
  }

  const closeModal = () => {
    // resultModal 종료
    setResult(null)
  }

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {/* 요청, 결과 모달 */}
      {fetching ? <FetchingModal /> : <></>}
      {result ? (
        <ResultModal
          title={"Product Add Result"}
          content={`${result}번 등록 완료`}
          callbackFn={closeModal}
        />
      ) : (
        <></>
      )}

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
          <input
            type="text"
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="pname"
            value={product.pname} // 필요한가?
            onChange={handleChangeProduct}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Desc</div>
          <textarea
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
            name="pdesc"
            rows="4"
            onChange={handleChangeProduct}
            value={product.pdesc}>
            {product.pdesc}
          </textarea>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Price</div>
          <input
            type="number"
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="price"
            value={product.price}
            onChange={handleChangeProduct}
          />
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Files</div>
          <input
            ref={uploadRef}
            type="file"
            multiple={true}
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <button
            className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
            onClick={handleClickAdd}>
            ADD
          </button>
        </div>
      </div>
    </div>
  )
}

export default AddComponent
