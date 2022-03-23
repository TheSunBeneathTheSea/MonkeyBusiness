import React, { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "./components/Navbar";
import Footer from "./components/Footer";
import Account from "./views/Account";

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      {/* <NavBar istrue={istrue}/> */}
      <Routes>
        <Route path="/account/*" element={<Account />} />
      </Routes>

      <Footer />
    </BrowserRouter>
  );
}

export default App;
