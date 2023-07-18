import React from "react";
import "./App.css";
import Header from "./components/Header/Header";
import Content from "./components/Content/Content";
import Footer from "./components/Footer/Footer";
import { BrowserRouter } from "react-router-dom";
import { UserContext, UserProps } from "./contexts/UserContext";

export const appTestId = "app-test-id";

function App() {
  const [userProps, setUserProps] = React.useState<UserProps>({});
  const value = { userProps, setUserProps };

  return (
    <div className="App" data-testid={appTestId}>
      <UserContext.Provider value={value}>
        <Header />
        <BrowserRouter>
          <Content />
        </BrowserRouter>
        <Footer />
      </UserContext.Provider>
    </div>
  );
}

export default App;
