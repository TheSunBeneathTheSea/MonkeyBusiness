import React, { useState, useEffect } from "react";
import axios from "axios";
import { Routes, Route, Link, Outlet } from "react-router-dom";
import styled from "styled-components/macro";
import AccountInfo from "./AccountInfo";
import TradeLogs from "./TradeLogs";
import Portfolio from "./Portfolio";

const Account = ({ backAPI }) => {
  const [account, setAccount] = useState([]);
  const [competition, setCompetition] = useState([]);
  const accountAPI = backAPI + "/account";
  const competitionAPI = backAPI + "/competition";
  const userId = "2498cd4b-3124-4231-a008-9ede7c47abb4";

  useEffect(() => {
    // 도커에 올릴때 ip 수정
    let isMounted = true;
    getAccount(accountAPI)
      .then((response) => response.data)
      .then((data) => {
        if (isMounted) {
          setAccount(data);
        }
      });
    getCompetition(competitionAPI)
      .then((response) => response.data)
      .then((data) => {
        if (isMounted) {
          setCompetition(data);
        }
      });
    return () => {
      isMounted = false;
    };
  }, []);

  const getAccount = async (request) => {
    let account = [];
    account = await axios.get(request, { params: { userId: userId } });
    return account;
  };

  const getCompetition = async (request) => {
    let comp = [];
    comp = await axios.get(request);
    return comp;
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
                <Item to="/account">투자 현황</Item>
              </ItemBox>
              <ItemBox>
                <Item to="/competition">대회</Item>
              </ItemBox>
            </SubList>
          </Container>
          <Routes>
            <Route
              path="/"
              element={
                <AccountInfo account={account} competition={competition} />
              }
            />
            <Route path="logs">
              <Route
                path=":competitionId"
                element={<TradeLogs userId={userId} backAPI={backAPI} />}
              ></Route>
            </Route>
            <Route path="portfolio">
              <Route
                path=":competitionId"
                element={
                  <Portfolio userId={account[0].userId} backAPI={backAPI} />
                }
              />
            </Route>
          </Routes>
          <Outlet />
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
