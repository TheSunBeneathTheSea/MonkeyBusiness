import React, { useState, useEffect } from "react";
import { Outlet, useParams } from "react-router-dom";
import axios from "axios";
import styled from "styled-components/macro";
import OrderButton from "./OrderButton";
import useStock from "../utils/useStock";

const Trade = ({ backAPI }) => {
  const compId = useParams().competitionId;

  const { stockInfo, isLoading } = useStock();
  const pfAPI = backAPI + "/account/portfolio";
  const [pfs, setPfs] = useState([]);
  const [pfList, setPfList] = useState([]);

  const [orderOpened, setOrderOpened] = useState(false);

  useEffect(() => {
    let isMounted = true;
    getPfs(pfAPI)
      .then((response) => response[0].data)
      .then((data) => {
        if (isMounted) {
          setPfs(data);
          setPfList(data.map((stock) => stock.stockInfo.ticker));
        }
      });
    return () => {
      isMounted = false;
    };
  }, [stockInfo]);

  const getPfs = async (request) => {
    let pf = [];
    pf = pf.concat(
      await axios.get(request, {
        params: { competitionId: compId },
      })
    );
    return pf;
  };

  const own = stockInfo
    ? stockInfo.data.filter((stock) => pfList.includes(stock.ticker))
    : null;
  const notOwn = stockInfo
    ? stockInfo.data.filter((stock) => !pfList.includes(stock.ticker))
    : null;

  return (
    <>
      <Container>
        <Table>
          <thead>
            <tr>
              <th>단축코드</th>
              <th>회사명</th>
              <th>시가</th>
              <th>현재가</th>
              <th>전일대비</th>
              <th>거래</th>
            </tr>
          </thead>
          <tbody>
            {own &&
              own.map((stock, idx) => (
                <tr key={idx}>
                  <td>{stock.ticker}</td>
                  <td>{stock.companyName}</td>
                  <td>{stock.openPrice}</td>
                  <td>{stock.currentPrice}</td>
                  <td
                    style={{
                      color:
                        stock.openPrice < stock.currentPrice
                          ? "red"
                          : stock.openPrice > stock.currentPrice
                          ? "blue"
                          : "black",
                    }}
                  >
                    {stock.currentPrice - stock.openPrice}
                  </td>
                  <td>
                    {
                      <OrderButton
                        backAPI={backAPI}
                        isBuying={true}
                        pf={null}
                        ticker={stock.ticker}
                        compId={compId}
                        orderOpened={orderOpened}
                        setOrderOpened={setOrderOpened}
                      />
                    }
                    {
                      <OrderButton
                        backAPI={backAPI}
                        isBuying={false}
                        pf={pfs.find(
                          (p) => p.stockInfo.ticker === stock.ticker
                        )}
                        ticker={stock.ticker}
                        compId={compId}
                        orderOpened={orderOpened}
                        setOrderOpened={setOrderOpened}
                      />
                    }
                  </td>
                </tr>
              ))}
            {notOwn &&
              notOwn.map((stock, idx) => (
                <tr key={idx}>
                  <td>{stock.ticker}</td>
                  <td>{stock.companyName}</td>
                  <td>{stock.openPrice}</td>
                  <td>{stock.currentPrice}</td>
                  <td
                    style={{
                      color:
                        stock.openPrice < stock.currentPrice
                          ? "red"
                          : stock.openPrice > stock.currentPrice
                          ? "blue"
                          : "black",
                    }}
                  >
                    {stock.currentPrice - stock.openPrice}
                  </td>
                  <td>
                    {
                      <OrderButton
                        backAPI={backAPI}
                        isBuying={true}
                        pf={null}
                        ticker={stock.ticker}
                        compId={compId}
                        orderOpened={orderOpened}
                        setOrderOpened={setOrderOpened}
                      />
                    }
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Container>
      {/* <Routes>
        <Route path="order" element={<OrderBu backAPI={backAPI} />} />
      </Routes> */}
      <Outlet />
    </>
  );
};

export default Trade;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  display: flex;
  justify-content: center;
`;

const Table = styled.table`
  border: 1px solid;
  td,
  th {
    padding: 5px;
    border: 1px solid;
  }
`;
