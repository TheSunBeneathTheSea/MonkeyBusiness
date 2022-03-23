import React, { useState, useEffect } from "react";
import axios from "axios";
import { Routes, Route, Link } from "react-router-dom";
import styled from "styled-components/macro";
import Strategy from "./Strategy";
import AccountInfo from "./AccountInfo";
import TradeLogs from "./TradeLogs";

const Account = () => {
  const [account, setAccount] = useState({});
  const [logs, setLogs] = useState([]);
  const back = "http://localhost:8080";
  const accountAPI = back + "/api/v1/account/";
  const logsAPI = back + "/api/v1/logs/";
  const user_id = "2498cd4b-3124-4231-a008-9ede7c47abb4";

  useEffect(() => {
    // 도커에 올릴때 ip 수정
    getAccount(accountAPI + user_id);
    getLogs(logsAPI + user_id);
  }, []);

  const getAccount = async (request) => {
    let account = {};
    account = await axios.get(request).then((response) => response.data);
    setAccount(account);
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
      {account.length === 0 ? (
        <p>가입된 계정이 없습니다</p>
      ) : (
        <>
          <Container>
            <SubList>
              <ItemBox>
                <Item to="">투자 정보</Item>
              </ItemBox>
              <ItemBox>
                <Item to="strategy">투자 전략</Item>
              </ItemBox>
              <ItemBox>
                <Item to="logs">투자 기록</Item>
              </ItemBox>
              <ItemBox>
                <Item to="">랭킹</Item>
              </ItemBox>
            </SubList>
          </Container>
          <Routes>
            <Route
              path="/"
              element={
                <AccountInfo account={account} getAccount={getAccount} />
              }
            />
            <Route
              path="strategy"
              element={<Strategy account={account} getAccount={getAccount} />}
            />
            <Route
              path="logs"
              element={<TradeLogs logs={logs} getAccount={getAccount} />}
            />
          </Routes>
        </>
      )}
    </>
  );
};

export default Account;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  height: auto;
  display: flex;
`;

const SubList = styled.ul`
  display: inline-block;
  margin: auto;
  padding: 0px;
`;

const ItemBox = styled.div`
  display: inline;
  margin: 5px;
  font-size: 24px;
`;

const Item = styled(Link)`
  color: white;
  background-color: #0078ff;
  text-align: center;
  text-decoration: none;
  padding: 14px 25px;
  display: inline-block;
`;
