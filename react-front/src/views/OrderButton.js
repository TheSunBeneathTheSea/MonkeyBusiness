import React, { useState } from "react";
import Order from "./Order";

const OrderButton = ({
  backAPI,
  isBuying,
  pf,
  ticker,
  compId,
  orderOpened,
  setOrderOpened,
}) => {
  const [openOrder, setOpenOrder] = useState(false);
  const orderHandler = (event) => {
    if (openOrder) {
      setOrderOpened(false);
      setOpenOrder(false);
    } else if (!orderOpened) {
      setOrderOpened(true);
      setOpenOrder(true);
      return;
    } else {
      return;
    }
  };
  return (
    <>
      <button onClick={orderHandler}>{isBuying ? "매수" : "매도"}</button>
      {openOrder && (
        <Order
          isBuying={isBuying}
          pf={pf}
          orderHandler={orderHandler}
          ticker={ticker}
          compId={compId}
          backAPI={backAPI}
        />
      )}
    </>
  );
};

export default OrderButton;
