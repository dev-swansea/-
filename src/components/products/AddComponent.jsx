import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useRef, useState } from "react"
import { postAdd } from "../../api/productsApi"
import useCustomMove from "../../hooks/useCustomMove"
import ResultModal from "../../components/common/ResultModal"

const initState = {
  pname: "",
  pdesc: "",
  price: 0,
  files: [],
}

const AddComponent = () => {
  const [product, setProduct] = useState({ ...initState })
  const uploadRef = useRef()
  const queryClient = useQueryClient()

  const { moveToList } = useCustomMove()

  const handleChangeProduct = (e) => {
    product[e.target.name] = e.target.value
    setProduct({ ...product })
  }

  const addMutation = useMutation({
    mutationFn: (product) => postAdd(product),
  })

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

    addMutation.mutate(formData)
  }

  const closeModal = () => {
    queryClient.invalidateQueries("products/list")
    moveToList({ page: 1 })
  }

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {addMutation.isPending ? <ResultModal /> : <></>}
      {addMutation.isSuccess ? (
        <ResultModal
          title={"Product Add Result"}
          content={`${addMutation.data.result}번 등록 완료`}
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
            value={product.pname}
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
