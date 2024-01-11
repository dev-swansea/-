import React from "react";
import BasicLayout from "../../layout/BasicLayout";
import {Link, Outlet} from "react-router-dom";

const IndexPage = () => {
  return (
    <BasicLayout>
      <div className="w-full flex m-2 p-2">
        <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline">
          <Link to="list">LIST</Link>
        </div>
        <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline">ADD</div>
        <div className="flex flex-wrap w-full"></div>
        <Outlet />
      </div>
    </BasicLayout>
  );
};

export default IndexPage;
