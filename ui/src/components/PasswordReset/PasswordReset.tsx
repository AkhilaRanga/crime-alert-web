import React, { useState } from "react";
import { Grid, Paper, TextField, Button, Snackbar } from "@material-ui/core";
import { isValidPassword } from "../../utils/validationUtils";
import { UserContext } from "../../contexts/UserContext";
import { RouterPath } from "../../constants/routerConstants";
import { useNavigate } from "react-router-dom";

export const resetTestId = "reset-test-id";

const initialState = {
  newPassword: "",
  confirmPassword: "",
};

function PasswordReset() {
  const [formValues, setFormValues] = useState(initialState);
  const [passwordError, setpasswordError] = useState<string | null>(null);
  const [confirmPasswordError, setConfirmpasswordError] = useState<
    string | null
  >(null);
  const [successMessage, setsuccessMessage] = useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const navigate = useNavigate();

  const handlePasswordChange = (event: any) => {
    if (event.target.id === "newPassword") {
      if (!isValidPassword(event.target.value)) {
        setpasswordError(
          "Password must contain - 8 characters, one uppercase, one lowercase, one digit, one special character"
        );
      } else {
        setpasswordError(null);
      }
    } else {
      if (formValues["newPassword"] !== event.target.value) {
        setConfirmpasswordError("Password does not match");
      } else {
        setConfirmpasswordError(null);
      }
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
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email: userProps?.email,
        password: formValues["newPassword"],
      }),
    };

    fetch("/services/api/users/forgotPassword", requestOptions)
      .then((response) => {
        if (response.ok) {
          return response.text();
        }
        return Promise.reject(response);
      })
      .then((data) => {
        setOpenSnackbar(true);
        setsuccessMessage(data);
        navigate(RouterPath.HOME);
      })
      .catch((response) => {
        response.text().then((text: any) => {
          setOpenSnackbar(true);
          setsuccessMessage(text);
        });
      });
  };

  const paperStyle = {
    padding: 20,
    height: 250,
    width: 350,
    margin: "20px auto",
  };
  return (
    <Grid data-testid={resetTestId}>
      <Paper elevation={10} style={paperStyle}>
        <form onSubmit={handleSubmit}>
          <Grid
            container
            direction="column"
            justifyContent="flex-start"
            alignItems="center"
            spacing={4}
          >
            <h2>Forgot Password</h2>

            <TextField
              style={{ paddingBottom: "15px" }}
              label="NewPassword"
              id="newPassword"
              placeholder="New Password"
              onChange={handlePasswordChange}
              type="password"
              fullWidth
              required
              variant="filled"
              error={passwordError !== null}
              helperText={(passwordError && passwordError) || ""}
            />
            <TextField
              style={{ paddingBottom: "15px" }}
              label="ConfirmPassword"
              id="confirmPassword"
              placeholder="Confirm Password"
              onChange={handlePasswordChange}
              type="password"
              fullWidth
              required
              variant="filled"
              error={confirmPasswordError !== null}
              helperText={(confirmPasswordError && confirmPasswordError) || ""}
            />

            <Button type="submit" color="primary" variant="contained" fullWidth>
              Reset Password
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

export default PasswordReset;
