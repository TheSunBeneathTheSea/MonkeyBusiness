import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components/macro";

const AccountInfo = ({ account, competition }) => {
  return (
    <>
      <Container>
        <Table>
          <thead>
            <tr>
              <th>대회 ID</th>
              <th>대회 이름</th>
              <th>계좌 평가 금액</th>
              <th>계좌 보유 현금</th>
              <th>거래 이력</th>
              <th>계좌 포트폴리오</th>
              <th>계좌 거래</th>
            </tr>
          </thead>
          <tbody>
            {competition &&
              account.map((account, idx) => (
                <tr key={idx}>
                  <td>{account.competitionId}</td>
                  <td>
                    {account.competitionId === 0
                      ? "기본 계좌"
                      : competition.find((c) => c.id === account.competitionId)
                          .name}
                  </td>
                  <td>{account.capital}</td>
                  <td>{account.points}</td>
                  <td>
                    <Link to={`logs/${account.competitionId}`}>이력 확인</Link>
                  </td>
                  <td>
                    <Link to={`portfolio/${account.competitionId}`}>
                      포트폴리오 확인
                    </Link>
                  </td>
                  <td>
                    <Link to={`/competition/${account.competitionId}`}>
                      거래하기
                    </Link>
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Container>
    </>
  );
};

export default AccountInfo;

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
