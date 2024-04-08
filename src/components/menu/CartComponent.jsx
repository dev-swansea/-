import { useEffect, useMemo } from "react"
import useCustomCart from "../../hooks/useCustomCart"
import useCustomLogin from "../../hooks/useCustomLogin"
import CartItemComponent from "../cart/CartItemComponent"
import { useRecoilState } from "recoil"
import { cartTotalState } from "../../atom/cartState"

const CartComponent = () => {
  const { isLogin, loginState } = useCustomLogin()
  const { cartItems, changeCart } = useCustomCart()

  const totalValue = useRecoilState(cartTotalState)

  return (
    <div className="w-full">
      {isLogin ? (
        <div className="flex flex-col">
          <div className="m-2 font-extrabold">{loginState.nickname}'s Cart</div>
          <div className="bg-orange-600 w-9 text-center text-white font-bold rounded-full m-2">
            {cartItems.length}
          </div>

          <div>
            <ul>
              {cartItems.map((item) => (
                <CartItemComponent
                  {...item}
                  key={item.cino}
                  changeCart={changeCart}
                  email={loginState.email}
                />
              ))}
            </ul>
          </div>

          <div>
            <div className="text-2xl text-right font-extrabold">TOTAL: {totalValue}</div>
          </div>
        </div>
      ) : (
        <></>
      )}
    </div>
  )
}
export default CartComponent
