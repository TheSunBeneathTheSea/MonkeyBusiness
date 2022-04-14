import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link, Outlet } from "react-router-dom";
import styled from "styled-components/macro";

const Competition = ({ backAPI }) => {
  const compAPI = backAPI + "/competition";
  const partAPI = backAPI + "/participant";
  const [competition, setCompetition] = useState([]);
  const userId = "2498cd4b-3124-4231-a008-9ede7c47abb4";

  useEffect(() => {
    let isMounted = true;
    getCompetition(compAPI)
      .then((response) => response[0].data)
      .then((data) => {
        if (isMounted) setCompetition(data);
      });
    return () => {
      isMounted = false;
    };
  }, []);

  const getCompetition = async (request) => {
    let comp = [];
    comp = comp.concat(await axios.get(request));
    return comp;
  };

  const enrollHandler = (event) => {
    event.preventDefault();
    console.log(event);
    let msg =
      "대회에 참가하시면 대회 전용 계좌가 생성되며,\n대회가 종료되면 전용 계좌는 자동 삭제됩니다.\n참가하시겠습니까?";
    if (window.confirm(msg)) {
      enroll(event.target.getAttribute("compid"));
    } else {
      console.log("취소");
    }
  };

  const enroll = async (compid) => {
    const requestDto = {
      userId: userId,
      competitionId: compid,
    };
    await axios
      .post(partAPI, requestDto)
      .then(() => window.alert("성공했습니다"))
      .catch((err) => {
        window.alert(err.response.data.message);
        console.log(err);
      });
  };
  return (
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
      <Container>
        <Table>
          <thead>
            <tr>
              <th>ID</th>
              <th>대회명</th>
              <th>시작일</th>
              <th>종료일</th>
              <th>상태</th>
              <th>랭킹확인</th>
            </tr>
          </thead>
          <tbody>
            {competition &&
              competition.map((comp) => (
                <tr key={comp.id}>
                  <td>{comp.id}</td>
                  <td>{comp.name}</td>
                  <td>{comp.start}</td>
                  <td>{comp.end}</td>
                  <td>
                    {comp.active ? (
                      "진행중"
                    ) : new Date(comp.start) > new Date() ? (
                      <button compid={comp.id} onClick={enrollHandler}>
                        참가가능
                      </button>
                    ) : (
                      "종료됨"
                    )}
                  </td>
                  <td>
                    {comp.active
                      ? "실시간 랭킹"
                      : new Date(comp.start) > new Date()
                      ? ""
                      : "최종 순위"}
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Container>
      <Outlet />
    </>
  );
};

export default Competition;

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
