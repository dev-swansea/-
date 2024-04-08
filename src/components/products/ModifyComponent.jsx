import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { useEffect, useRef, useState } from "react"
import { deleteOne, getOne, putOne } from "../../api/productsApi"
import { API_SERVER_HOST } from "../../api/todoApi"
import useCustomMove from "../../hooks/useCustomMove"
import FetchingModal from "../../components/common/FetchingModal"
import ResultModal from "../../components/common/ResultModal"

const initState = {
  pno: 0,
  pname: "",
  pdesc: "",
  price: 0,
  delFlag: false,
  uploadFileNames: [],
}

const host = API_SERVER_HOST
const ModifyComponent = ({ pno }) => {
  const [product, setProduct] = useState(initState)
  const { moveToRead, moveToList } = useCustomMove()
  const uploadRef = useRef()
  console.log("product: ", product)

  const delMutation = useMutation({
    mutationFn: (pno) => deleteOne(pno),
  })
  const modMutaion = useMutation({
    mutationFn: (product) => putOne(pno, product),
  })

  const queryClient = useQueryClient()

  const query = useQuery({
    queryKey: ["products", pno],
    queryFn: () => getOne(pno),
    staleTime: Infinity,
  })

  useEffect(() => {
    if (query.isSuccess) {
      setProduct(query.data)
    }
  }, [pno, query.data, query.isSuccess])

  const handleChangeProduct = (e) => {
    setProduct({ ...product, [e.target.name]: e.target.value })
  }

  const handleClickDelete = () => {
    delMutation.mutate(pno)
    console.log("delMutation: ", delMutation)
  }

  const handleClickModify = () => {
    const files = uploadRef.current.files
    const formData = new FormData()

    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i])
    }

    // other data
    formData.append("pname", product.pname)
    formData.append("pdesc", product.pdesc)
    formData.append("price", product.price)
    formData.append("delFlag", product.delFlag)

    for (let i = 0; i < product.uploadFileNames.length; i++) {
      formData.append("uploadFileNames", product.uploadFileNames[i])
    }

    for (const [key, value] of formData) {
      console.log(key, value)
    }
    console.log(product)
    modMutaion.mutate(formData)
  }

  const closeModal = () => {
    if (delMutation.isSuccess) {
      queryClient.invalidateQueries(["products", pno])
      queryClient.invalidateQueries(["products/list"])
      moveToList()
      return
    }

    if (modMutaion.isSuccess) {
      queryClient.invalidateQueries(["products", pno])
      queryClient.invalidateQueries(["products/list"])
      moveToRead(pno)
    }
  }

  const deleteOldImages = (imageName) => {
    const resultFileNames = product.uploadFileNames.filter((filename) => filename !== imageName)
    product.uploadFileNames = resultFileNames
    setProduct({ ...product })
  }

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {query.isFetching || delMutation.isPending || modMutaion.isPending ? (
        <FetchingModal />
      ) : (
        <></>
      )}
      {delMutation.isSuccess || modMutaion.isSuccess ? (
        <ResultModal
          title={`${query.data.pno}번`}
          content={"정상적으로 처리되었습니다."}
          callbackFn={closeModal}
        />
      ) : (
        <></>
      )}

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Name</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="pname"
            type="text"
            onChange={handleChangeProduct}
            value={product.pname}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Desc</div>
          <textarea
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y"
            row="4"
            onChange={handleChangeProduct}
            value={product.pdesc}
            name="pdesc">
            {product.pdesc}
          </textarea>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Price</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="price"
            type="number"
            onChange={handleChangeProduct}
            value={product.price}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Delete</div>
          <select
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="delFlag"
            value={product.defFlag}
            onChange={handleChangeProduct}>
            <option value={false}>사용</option>
            <option value={true}>삭제</option>
          </select>
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Files</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            ref={uploadRef}
            type="file"
            multiple={true}
          />
        </div>
      </div>

      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">Images</div>
          <div className="w-4/5 justify-center flex flex-wrap items-start">
            {product.uploadFileNames.map((imgFile, i) => (
              <div className="flex justify-center flex-col w-1/3 m-1 align-baseline" key={i}>
                <button
                  className="bg-blue-500 text-3xl text-white"
                  onClick={() => deleteOldImages(imgFile)}>
                  DELETE
                </button>

                <img src={`${host}/api/products/view/s_${imgFile}`} alt="기존이미지" />
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="flex justify-end p-4">
        <button
          className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
          onClick={handleClickDelete}>
          Delete
        </button>
        <button
          className="rounded p-4 m-2 text-xl w-32 text-white bg-orange-500"
          onClick={handleClickModify}>
          Modify
        </button>
        <button
          className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500"
          onClick={() => moveToList()}>
          List
        </button>
      </div>
    </div>
  )
}

export default ModifyComponent
