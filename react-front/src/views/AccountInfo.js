import React from "react";
import styled from "styled-components/macro";

const AccountInfo = ({ account, getAccount }) => {
  return (
    <Container>
      <DataBox key={account.id}>
        <p>Account</p>
        <Paragraph>user_id: {account.user_id}</Paragraph>
        <Paragraph>ticker: {account.ticker}</Paragraph>
        <Paragraph>company name: {account.companyName}</Paragraph>
        <Paragraph>current price: {account.currentPrice}</Paragraph>
        <Paragraph>buying price: {account.buyingPrice}</Paragraph>
        <Paragraph>take profit point: {account.takeProfitPoint}</Paragraph>
        <Paragraph>stop loss point: {account.stopLossPoint}</Paragraph>
        <Paragraph>holding amount: {account.holdingAmount}</Paragraph>
        <Paragraph>points: {account.points}</Paragraph>
        <Paragraph>capital: {account.capital}</Paragraph>
      </DataBox>
    </Container>
  );
};

export default AccountInfo;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  display: flex;
  justify-content: center;
`;

const DataBox = styled.div`
  width: 90%;
  text-align: left;
  border-style: outset;
  border-color: blue;
  pading: 0px;
  margin: 5px 1px;
  box-sizing: border-box;
`;

const Paragraph = styled.p`
  margin: 3px;
`;
