import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import styled from "styled-components/macro";

const Ranking = ({ backAPI, competition }) => {
  const [ranking, setRanking] = useState([]);
  const compId = useParams().competitionId;
  const rankAPI = backAPI + "/ranking/" + compId;
  const targetComp = competition.find((c) => c.id == compId);

  useEffect(() => {
    let isMounted = true;
    getRanking(rankAPI)
      .then((response) => response[0].data)
      .then((data) => {
        if (isMounted) setRanking(data);
      });
    return () => {
      isMounted = false;
    };
  }, []);

  const getRanking = async (request) => {
    let ranking = [];
    ranking = ranking.concat(await axios.get(request));
    return ranking;
  };

  return (
    <>
      <Container>
        <h2>대회명 : {targetComp.name}</h2>
        <br />
        <h3>
          진행기간 : {targetComp.start} ~ {targetComp.end}
        </h3>
        <Table>
          <thead>
            <tr>
              <th>순위</th>
              <th>닉네임</th>
              <th>자본가치</th>
            </tr>
          </thead>
          <tbody>
            {ranking &&
              ranking.map((rank, idx) => (
                <tr key={idx}>
                  <td>{rank.rank}</td>
                  <td>{rank.nickname}</td>
                  <td>{rank.totalCapital}</td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Container>
    </>
  );
};

export default Ranking;

const Container = styled.div`
  margin: 1rem auto;
  width: 80%;
  display: block;
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
