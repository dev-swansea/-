import React, {useCallback} from "react";
import {Outlet, useNavigate} from "react-router-dom";
import BasicLayout from "../../layout/BasicLayout";

const IndexPage = () => {
  const navigate = useNavigate();

  const handleClickList = useCallback(() => {
    navigate({pathname: "list"});
  });

  const handleClickAdd = useCallback(() => {
    navigate({pathname: "add"});
  });

  return (
    <BasicLayout>
      <div className="w-full flex m-2 p-2">
        <div
          className="text-xl m-1 p-2 w-20 font-extrabold text-center underline cursor-pointer"
          onClick={handleClickList}
        >
          LIST
        </div>
        <div
          className="text-xl m-1 p-2 w-20 font-extrabold text-center underline cursor-pointer"
          onClick={handleClickAdd}
        >
          ADD
        </div>
        <Outlet />
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
