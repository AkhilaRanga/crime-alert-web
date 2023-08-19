import React from "react";
import Dashboard from "../Dashboard/Dashboard";
import Login from "../Login/Login";
import { UserContext } from "../../contexts/UserContext";
import OTPVerificationRequest from "../OTPVerification/OTPVerificationRequest";
import { makeStyles } from "@material-ui/core";

export const homeTestId = "home-test-id";

function Home() {
  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const isUserLoggedIn = userProps?.isLoggedIn;
  const isVerified = userProps?.isVerified;
  const useStyles = makeStyles((theme) => ({
    appDescription: {
      float: "left",
      width: "40%",
      padding: "10%",
      fontSize: "larger",
      color: "#26a69a",
    },
    loginForm: {
      float: "left",
    },
  }));
  const classes = useStyles();

  return (
    <div data-testid={homeTestId}>
      {isUserLoggedIn ? (
        isVerified ? (
          <Dashboard />
        ) : (
          <OTPVerificationRequest />
        )
      ) : (
        <>
          <div className={classes.appDescription}>
            <h3>Stay Safe with Crime Alerts</h3>
            <p>
              Crime Alertness is a social network application to connect users
              of a locality to be able to report crimes, view crimes, get
              dangerous, and close crime alerts.
            </p>
          </div>
          <div className={classes.loginForm}>
            <Login />
          </div>
        </>
      )}
    </div>
  );
}

export default Home;
