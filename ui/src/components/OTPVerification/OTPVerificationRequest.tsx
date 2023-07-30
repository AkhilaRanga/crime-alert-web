import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Snackbar } from "@material-ui/core";
import { isValidEmail } from "../../utils/validationUtils";
import { UserContext } from "../../contexts/UserContext";
import { useNavigate } from "react-router-dom";
import { RouterPath } from "../../constants/routerConstants";

export const otpVerificationRequestTestId = "otp-verification-request-test-id";

const initialState = {
  email: "",
};

function OTPVerificationRequest() {
  const [formValues, setFormValues] = useState(initialState);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [successMessage, setsuccessMessage] = useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const { userProps, setUserProps } = React.useContext(UserContext);
  const navigate = useNavigate();

  const handleEmailChange = (event: any) => {
    if (!isValidEmail(event.target.value)) {
      setEmailError("Email is invalid");
    } else {
      setEmailError(null);
    }
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
      headers: {
        "Content-Type": "application/json",
      },
    };

    fetch(
      `/services/api/auth/requestOtp/${formValues["email"]}`,
      requestOptions
    )
      .then((response) => response.text())
      .then((data) => {
        console.log(data);
        setOpenSnackbar(true);
        setsuccessMessage(data);
        setUserProps({ ...userProps, email: formValues["email"] });
        navigate(RouterPath.VERIFY);
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
    <Grid data-testid={otpVerificationRequestTestId}>
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
              label="Email id"
              id="email"
              placeholder="Enter email id"
              onChange={handleEmailChange}
              fullWidth
              required
              variant="filled"
              error={emailError !== null}
              helperText={(emailError && emailError) || ""}
            />
            <Button type="submit" color="primary" variant="contained" fullWidth>
              Send Verification Code
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

export default OTPVerificationRequest;