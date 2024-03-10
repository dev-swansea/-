import { Suspense, lazy } from "react"
import { Navigate } from "react-router-dom"

const productRouter = () => {
  const Loading = <div>Loading ...</div>
  const ProductList = lazy(() => import("../pages/products/ListPage"))
  const ProductAdd = lazy(() => import("../pages/products/AddPage"))

  // prettier-ignore
  return [
    {
      path: "", 
      element: <Navigate replace to="list" /> 
    },
    {
      path: "list",
      element: <Suspense fallback={Loading}> <ProductList /> </Suspense> 
    },
    {
      path:"add",
      element: <Suspense fallback={Loading}><ProductAdd/></Suspense>
    }
  ]
}

export default productRouter
