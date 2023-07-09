import React from "react";
import Dashboard from "../Dashboard/Dashboard";
import Login from "../Login/Login";

export const homeTestId = "home-test-id";

function Home() {
  const [isUserLoggedIn, setIsUserLoggedIn] = React.useState<boolean>(false);

  return (
    <div data-testid={homeTestId}>
      {isUserLoggedIn ? <Dashboard /> : <Login />}
    </div>
  );
}

export default Home;
