import React from "react";
import Dashboard from "../Dashboard/Dashboard";
import Login from "../Login/Login";
import { UserContext } from "../../contexts/UserContext";
import OTPVerificationRequest from "../OTPVerification/OTPVerificationRequest";

export const homeTestId = "home-test-id";

function Home() {
  const { userProps } = React.useContext(UserContext);
  const isUserLoggedIn = userProps.isLoggedIn;
  const isVerified = userProps.isVerified;

  return (
    <div data-testid={homeTestId}>
      {isUserLoggedIn ? (
        isVerified ? (
          <Dashboard />
        ) : (
          <OTPVerificationRequest />
        )
      ) : (
        <Login />
      )}
    </div>
  );
}

export default Home;
