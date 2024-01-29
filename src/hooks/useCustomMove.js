import React, { useState } from "react";
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom";

const getNum = (param, defaultValue) => {
  if (!param) {
    return defaultValue
  }

  return parseInt(param)
}

const useCustomMove = () => {

  const navigate = useNavigate()
  const [queryParams] = useSearchParams()
  const [refresh, setRefresh] = useState(false)

  const page = getNum(queryParams.get('page'), 1)
  const size = getNum(queryParams.get("size"), 10)

  const queryDefault = createSearchParams({ page, size }).toString() // 새로 추가


  // 게시글 -> 목록
  const moveToList = (pageParam) => {
    let queryStr = ""

    if (pageParam) {
      const pageNum = getNum(pageParam.page, 1)
      const sizeNum = getNum(pageParam.size, 10)
      queryStr = createSearchParams({ page: pageNum, size: sizeNum }).toString()
    } else {
      queryStr = queryDefault;
    }

    setRefresh(!refresh)

    navigate({ pathname: `../list`, search: queryStr })
  }

  // 게시글 -> 수정
  const moveToModify = (num) => {

    navigate({
      pathname: `../modify/${num}`,
      search: queryDefault // 수정시에 기존의 쿼리 스트링 유지를 위해 -> ?
    })

  }

  // 목록 -> 게시글
  const moveToRead = (num) => {
    navigate({
      pathname: `../read/${num}`,
      search: queryDefault
    })
  }

  return { moveToList, moveToModify, moveToRead, page, size, refresh }
};

export default useCustomMove;
