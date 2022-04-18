import React, { useState } from "react";
import axios from "axios";
import styled from "styled-components/macro";

const NewCompetition = ({ backAPI }) => {
  const compAPI = backAPI + "/competition";
  const [input, setInput] = useState({
    name: "",
    start: null,
    end: null,
  });

  const inputHandler = (event) => {
    event.preventDefault();
    const changes = { [event.target.id]: event.target.value };
    setInput({ ...input, ...changes });
  };

  const formHandler = async (event) => {
    event.preventDefault();
    if (
      new Date(input.start) >= new Date(input.end) ||
      new Date() >= new Date(input.start)
    ) {
      window.alert("시작과 종료 날짜를 다시 확인해주세요");
      return;
    }
    const tradeOrderRequestDto = {
      name: input.name,
      start: input.start,
      end: input.end,
    };

    await axios
      .post(compAPI, tradeOrderRequestDto)
      .then(() => {
        window.alert("성공");
      })
      .catch((err) => window.alert(err.response.data.message));
    window.location.replace(window.location.href);
  };

  return (
    <>
      <Container>
        <br />
        <h2>대회 개최</h2>
        <br />
        <form onSubmit={formHandler}>
          <input type="text" id="name" onChange={inputHandler} />
          <input type="date" id="start" onChange={inputHandler} />
          <input type="date" id="end" onChange={inputHandler} />
          <button onClick={formHandler}>확인</button>
        </form>
      </Container>
    </>
  );
};

export default NewCompetition;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  display: block;
  justify-content: center;
`;
