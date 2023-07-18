import React from "react";
import Dashboard from "../Dashboard/Dashboard";
import Login from "../Login/Login";
import { UserContext } from "../../contexts/UserContext";

export const homeTestId = "home-test-id";

function Home() {
  const { userProps } = React.useContext(UserContext);
  const isUserLoggedIn = userProps.isLoggedIn;

  return (
    <div data-testid={homeTestId}>
      {isUserLoggedIn ? <Dashboard /> : <Login />}
    </div>
  );
}

export default Home;
