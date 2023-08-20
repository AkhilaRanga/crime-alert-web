import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Snackbar } from "@material-ui/core";
import { UserContext } from "../../contexts/UserContext";
import { useNavigate } from "react-router-dom";
import { RouterPath } from "../../constants/routerConstants";

export const otpVerifyTestId = "otp-verify-test-id";

const initialState = {
  otp: "",
};

function OTPVerify() {
  const [formValues, setFormValues] = useState(initialState);
  const [successMessage, setsuccessMessage] = useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const updateUserProps = userContext?.updateUserProps;

  const navigate = useNavigate();

  const handleOtpChange = (event: any) => {
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });
  };
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        oneTimeToken: formValues["otp"],
        email: userProps?.email,
      }),
    };

    fetch("/services/api/auth/verifyOtp", requestOptions)
      .then((response) => response.text())
      .then((data) => {
        console.log(data);
        setOpenSnackbar(true);
        setsuccessMessage(data);
        updateUserProps && updateUserProps({ ...userProps, isVerified: true });

        navigate(
          userProps?.isForgotPassword
            ? RouterPath.RESET_PASSWORD
            : RouterPath.HOME
        );
      });

    //Need to display response message
  };
  const paperStyle = {
    padding: 20,
    height: 250,
    width: 350,
    margin: "20px auto",
  };
  return (
    <Grid data-testid={otpVerifyTestId}>
      <Paper elevation={10} style={paperStyle}>
        <form onSubmit={handleSubmit}>
          <Grid
            container
            direction="column"
            justifyContent="flex-start"
            alignItems="center"
            spacing={4}
          >
            <h2>Email Verification</h2>

            <TextField
              style={{ paddingBottom: "15px" }}
              label="One Time Password"
              id="otp"
              placeholder="Enter One Time Password"
              onChange={handleOtpChange}
              fullWidth
              required
              variant="filled"
            />
            <Button type="submit" color="primary" variant="contained" fullWidth>
              Verify OTP
            </Button>
            <Snackbar
              open={openSnackbar}
              autoHideDuration={6000}
              message={successMessage}
              onClose={() => setOpenSnackbar(false)}
            />
          </Grid>
        </form>
      </Paper>
    </Grid>
  );
}

export default OTPVerify;
