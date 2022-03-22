import React, { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "./components/Navbar";
import Footer from "./components/Footer";
import Trading from "./views/Trading";

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      {/* <NavBar istrue={istrue}/> */}
      <Routes>
        <Route path="/trading/" exact={true} element={<Trading />} />
      </Routes>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
