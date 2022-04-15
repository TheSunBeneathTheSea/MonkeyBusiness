import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "./components/Navbar";
import Footer from "./components/Footer";
import Account from "./views/Account";
import Competition from "./views/Competition";
import Trade from "./views/Trade";
import Order from "./views/Order";

function App() {
  const backAPI = "http://localhost:8080/api/v1";
  return (
    <BrowserRouter>
      <NavBar />
      {/* <NavBar istrue={istrue}/> */}
      <Routes>
        <Route path="/account/*" element={<Account backAPI={backAPI} />} />
        <Route
          path="/competition/*"
          element={<Competition backAPI={backAPI} />}
        />
        <Route
          path="/trade/:competitionId/*"
          element={<Trade backAPI={backAPI} />}
        />
        <Route path="/trade/:competitionId/order" element={<Order />} />
      </Routes>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
