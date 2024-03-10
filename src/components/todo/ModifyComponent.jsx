import {useEffect, useState} from "react"
import {deleteOne, getOne, modifyOne} from "../../api/todoApi"
import ResultModal from "../../components/common/ResultModal"
import useCustomMove from "../../hooks/useCustomMove"

const initState = {
  tno: 0,
  title: "",
  writer: "",
  dueDate: null,
  complete: false,
}

const ModifyComponent = ({tno, moveList, moveRead}) => {
  const [todo, setTodo] = useState({...initState})
  const [result, setResult] = useState(null)
  const {moveToRead, moveToList} = useCustomMove()

  useEffect(() => {
    getOne(tno).then(data => setTodo(data))
  }, [tno])

  const handleChangeTodo = e => {
    todo[e.target.name] = e.target.value // e.target.name은 title이 되겠음, inpurt 속성의 name값을 가져오는것
    setTodo({...todo})
  }

  const handleChangeTodoComplete = e => {
    const value = e.target.value
    console.log(value)
    todo.complete = value === "Y"
    setTodo({...todo})
  }

  // 버튼 클릭시 사용
  const handleClickModify = () => {
    modifyOne(todo).then(data => {
      setResult("Modify")
    })
  }

  const handleClickDelete = () => {
    deleteOne(tno).then(data => {
      setResult("Delete")
    })
  }

  const closeModal = () => {
    // 이게.. 모달을 닫고 난 후의 행위가 이루어지는데 함수이름이 이게 맞나
    if (result === "Delete") moveToList()
    else moveToRead(tno)
  }

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      {/* modal */}
      {result ? <ResultModal title={"처리결과"} content={result} callbackFn={closeModal}></ResultModal> : <></>}

      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TNO</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md bg-gray-100">{todo.tno}</div>
        </div>
      </div>
      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
          <div className="w-4/5 p-6 rounded-r border border-solid shadow-md bg-gray-100">{todo.writer}</div>
        </div>
      </div>
      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="title"
            type={"text"}
            value={todo.title}
            onChange={handleChangeTodo}
          />
        </div>
      </div>
      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
            name="dueDate"
            type={"date"}
            value={todo.dueDate}
            onChange={handleChangeTodo}
          />
        </div>
      </div>
      <div className="flex justify-center mt-10">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">COMPLETE</div>
          <select
            className="border-solid border-2 rounded m-1 p2"
            name="status"
            onChange={handleChangeTodoComplete}
            value={todo.complete ? "Y" : "N"}
          >
            <option value="Y">Complete</option>
            <option value="N">Not Yet</option>
          </select>
        </div>
      </div>

      <div className="flex justify-end p-4">
        <button className="rounded p-4 m-2 text-xl w-32 text-white bg-red-500" onClick={handleClickDelete}>
          DELETE
        </button>
        <button className="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500" onClick={handleClickModify}>
          MODIFY
        </button>
      </div>
    </div>
  )
}

export default ModifyComponent
