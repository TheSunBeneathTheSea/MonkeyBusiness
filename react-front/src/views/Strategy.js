import React, { useState } from "react";
import axios from "axios";
import styled from "styled-components/macro";

const Strategy = ({ account, getAccount }) => {
  const back = "http://localhost:8080/api/v1/account/";
  const accountAPI = back + account.user_id;
  const [strategy, setStrategy] = useState({});

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setStrategy((values) => ({ ...values, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    let strategyRequest = { ...strategy, user_id: account.user_id };

    await axios
      .put(back, strategyRequest)
      .then((response) => {
        alert("Edit strategy successfully!");
      })
      .catch((err) => {
        alert(err);
      });
    getAccount(accountAPI);
  };

  return (
    <Container>
      <h2>전략</h2>
      <p>현재 익절 시점 : {account.takeProfitPoint}</p>
      <p>현재 손절 시점 : {account.stopLossPoint}</p>
      <br />
      <div>
        <form id="strategy" onSubmit={handleSubmit}>
          <br />
          <label htmlFor="profitPoint">익절 시점(%)</label>
          <input
            type="number"
            name="takeProfitPoint"
            id="profitPoint"
            min="1"
            onChange={handleChange}
          />
          <br />
          <label htmlFor="profitPoint">손절 시점(%)</label>
          <input
            type="number"
            name="stopLossPoint"
            id="LossPoint"
            min="1"
            onChange={handleChange}
          />
          <br />
          <button type="submit" id="btn-save">
            전략 수정
          </button>
        </form>
      </div>
    </Container>
  );
};

export default Strategy;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  align-items: center;
  text-align: center;
  display: flex-box;
`;
