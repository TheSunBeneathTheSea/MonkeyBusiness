import React, { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components/macro";
import Strategy from "./Strategy";

const Trading = () => {
  const [data, setData] = useState([]);
  const [logs, setLogs] = useState([]);
  const back = "http://localhost:8080";
  const dataAPI = back + "/api/v1/trading";
  const logsAPI = back + "/api/v1/logs";

  useEffect(() => {
    // 도커에 올릴때 ip 수정
    getData(dataAPI);
    getLogs(logsAPI);
  }, []);

  const getData = async (request) => {
    let data = [];
    data = data.concat(
      await axios.get(request).then((response) => response.data)
    );
    setData(data.concat());
  };

  const getLogs = async (request) => {
    let logs = [];
    logs = logs.concat(
      await axios.get(request).then((response) => response.data)
    );
    setLogs(logs.concat());
  };

  return (
    <>
      <Strategy data={data} getData={getData} />
      <Container>
        <Coulmn>
          {data.map((data) => (
            <DataBox key={data.id}>
              <p>Trading Data</p>
              <Paragraph>id: {data.id}</Paragraph>
              <Paragraph>ticker: {data.ticker}</Paragraph>
              <Paragraph>company name: {data.companyName}</Paragraph>
              <Paragraph>current price: {data.currentPrice}</Paragraph>
              <Paragraph>buying price: {data.buyingPrice}</Paragraph>
              <Paragraph>take profit point: {data.takeProfitPoint}</Paragraph>
              <Paragraph>stop loss point: {data.stopLossPoint}</Paragraph>
              <Paragraph>holding amount: {data.holdingAmount}</Paragraph>
              <Paragraph>cash: {data.cash}</Paragraph>
              <Paragraph>capital: {data.capital}</Paragraph>
            </DataBox>
          ))}
        </Coulmn>
        <Coulmn>
          {logs.map((log) => (
            <LogBox key={log.id}>
              <p>Trading Log</p>
              <Paragraph>id: {log.id}</Paragraph>
              <Paragraph>user id: {log.userId}</Paragraph>
              <Paragraph>buying: {log.buying ? "매수" : "매도"}</Paragraph>
              <Paragraph>ticker: {log.ticker}</Paragraph>
              <Paragraph>buying price: {log.buyingPrice}</Paragraph>
              <Paragraph>selling price: {log.sellingPrice}</Paragraph>
              <Paragraph>amount: {log.amount}</Paragraph>
              <Paragraph>profit: {log.profit}</Paragraph>
              <Paragraph>created time: {log.createdTime}</Paragraph>
            </LogBox>
          ))}
        </Coulmn>
      </Container>
    </>
  );
};

export default Trading;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  display: flex;
`;

const Coulmn = styled.div`
  width: 50%;
  display: flex;
  flex-wrap: wrap;
`;

const DataBox = styled.div`
  width: 49%;
  text-align: left;
  border-style: outset;
  border-color: blue;
  pading: 0px;
  margin: 5px 1px;
  box-sizing: border-box;
`;

const LogBox = styled.div`
  width: 49%;
  text-align: left;
  border-style: outset;
  border-color: red;
  pading: 0px;
  margin: 5px 1px;
  box-sizing: border-box;
`;

const Paragraph = styled.p`
  margin: 3px;
`;
