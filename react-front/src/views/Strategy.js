import React, { useState } from "react";
import axios from "axios";
import styled from "styled-components/macro";

const Strategy = ({ data, getData }) => {
  const back = "http://localhost:8080";
  const dataAPI = back + "/api/v1/trading";
  const [strategy, setStrategy] = useState({});

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setStrategy((values) => ({ ...values, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    await axios.put(dataAPI, strategy).catch((err) => {
      alert(err);
    });
    getData(dataAPI);
  };

  return (
    <Container>
      <h2>전략</h2>
      <br />
      <div>
        <form id="strategy" onSubmit={handleSubmit}>
          <select id="id" name="userId" onChange={handleChange}>
            <option value="">User ID</option>
            {/* User db 연결되면 user 데이터를 받아와서 id를 사용 */}
            {data.map((data) => (
              <option value={data.id} key={data.id}>
                {data.id}
              </option>
            ))}
            ;
          </select>
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
