import { Suspense, lazy } from "react"
import { createBrowserRouter } from "react-router-dom"
import todoRouter from "./todoRouter"
import productRouter from "./productRouter"
import memberRouter from "./memberRouter"

const Loading = <div>Loading...</div>

const Main = lazy(() => import("../pages/MainPage"))
const About = lazy(() => import("../pages/About"))
const TodoIndex = lazy(() => import("../pages/todo/IndexPage"))
const ProductIndex = lazy(() => import("../pages/products/IndexPage"))

// prettier-ignore
const root = createBrowserRouter([
  {
    path: "",
    element: ( <Suspense fallback={Loading}> <Main /> </Suspense> ),
  },
  {
    path: "about",
    element: ( <Suspense fallback={Loading}> <About /> </Suspense> ),
  },
  {
    path: "todos",
    element: ( <Suspense fallback={Loading}> <TodoIndex /> </Suspense> ),
    children: todoRouter(),
  },
  {
    path: "products",
    element: ( <Suspense fallback={Loading}> <ProductIndex /> </Suspense> ), 
    children: productRouter(),
  },
  {
    path: "member",
    children: memberRouter()
  }
])

export default root
