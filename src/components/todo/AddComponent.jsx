import React, {useState} from "react";
import {todoAdd} from "../../api/todoApi";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";

const initState = {
  title: "",
  writer: "",
  dueDate: "",
};

const AddComponent = () => {
  const [todo, setTodo] = useState({...initState});
  // 결과 데이터가 있는 경우 ResultModal을 보여준다.
  const [result, setResult] = useState(null);

  const {moveToList} = useCustomMove();

  const handleChangeTodo = e => {
    todo[e.target.name] = e.target.value;
    setTodo({...todo});
  };

  const handleClickAdd = () => {
    todoAdd(todo)
      .then(result => {
        setResult(result.tno);
        setTodo({...initState});
      })
      .catch(e => console.error(e));
  };

  const closeModal = () => {
    setResult(null);
    moveToList();
  };

  return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            type={"text"}
            name="title"
            value={todo.title}
            onChange={handleChangeTodo}
          />
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            type={"text"}
            name="writer"
            value={todo.writer}
            onChange={handleChangeTodo}
          />
        </div>
      </div>
      <div className="flex justify-center">
        <div className="relative mb-4 flex w-full flex-wrap items-stretch">
          <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
          <input
            className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
            type={"date"}
            name="dueDate"
            value={todo.dueDate}
            onChange={handleChangeTodo}
          />
        </div>
      </div>

      {/* add button */}
      <div className="flex justify-end">
        <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
          <button className="rounded p-4 w-36 bg-blue-500 text-xl text-white" onClick={handleClickAdd}>
            ADD
          </button>
        </div>
      </div>

      {/* modal */}
      {result ? <ResultModal title={"Add Result"} content={`New ${result} Added`} callbackFn={closeModal} /> : <></>}
    </div>
  );
};

export default AddComponent;
