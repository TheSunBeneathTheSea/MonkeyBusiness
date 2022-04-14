import React, { useState, useEffect } from "react";
import axios from "axios";
import { Routes, Route, Link, useParams } from "react-router-dom";
import styled from "styled-components/macro";
import Order from "./Order";

const Trade = ({ backAPI }) => {
  const userId = "2498cd4b-3124-4231-a008-9ede7c47abb4";

  const stockAPI = backAPI + "/stock";
  const [stockInfo, setStockInfo] = useState([]);
  let params = useParams();
  const compId = params.competitionId;
  console.log(compId);

  useEffect(() => {
    let isMounted = true;
    getStockInfo(stockAPI)
      .then((response) => response[0].data)
      .then((data) => {
        if (isMounted) setStockInfo(data);
      });
    return () => {
      isMounted = false;
    };
  }, []);

  const getStockInfo = async (request) => {
    let stockInfo = [];
    stockInfo = stockInfo.concat(await axios.get(request));
    return stockInfo;
  };
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
            {stockInfo &&
              stockInfo.map((stock, idx) => (
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
                    <Link to="order">거래하기</Link>
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Container>
      {/* <Routes>
        <Route
          path=":competitionId"
          element={<Order userId={userId} backAPI={backAPI} />}
        />
      </Routes> */}
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
