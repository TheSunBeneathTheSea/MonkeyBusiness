import useSWR from "swr";
import axios from "axios";

const fetcher = async (url) => await axios.get(url);

export default function useStock() {
  const { data, error } = useSWR(
    `http://localhost:8080/api/v1/stock`,
    fetcher,
    { refreshInterval: 1000 }
  );

  return {
    stockInfo: data,
    isLoading: !error && !data,
    isError: error,
  };
}
