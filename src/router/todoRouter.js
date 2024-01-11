import { Suspense, lazy } from "react";

const Loading = <div>Loading ...</div>
const TodoList = lazy(() => import("../pages/todo/ListPage"))

const todoRouter = () => {

  return [
    {
      path: "list",
      element: <Suspense fallback={Loading}><TodoList /></Suspense>
    }
  ]
};

export default todoRouter;
