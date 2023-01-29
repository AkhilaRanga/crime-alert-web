import React from "react";
import "./App.css";
import Header from "./components/Header/Header";
import Content from "./components/Content/Content";
import Footer from "./components/Footer/Footer";
import { BrowserRouter } from "react-router-dom";

export const appTestId = "app-test-id";

function App() {
  return (
    <div className="App" data-testid={appTestId}>
      <Header />
      <BrowserRouter>
        <Content />
      </BrowserRouter>
      <Footer />
    </div>
  );
}

export default App;
