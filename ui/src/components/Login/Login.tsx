import React, { useState } from "react";
import {
  Grid,
  Paper,
  TextField,
  Button,
  Typography,
  Link,
  Divider,
} from "@material-ui/core";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Snackbar from "@material-ui/core/Snackbar";
import Registration from "../Registration/Registration";
import "./Login.css";
import { isValidEmail, isValidPassword } from "../../utils/validationUtils";
import { UserContext } from "../../contexts/UserContext";
import { useNavigate } from "react-router-dom";
import { RouterPath } from "../../constants/routerConstants";

export const loginTestId = "login-test-id";

const initialState = {
  email: "",
  password: "",
};

function Login() {
  const [formValues, setFormValues] = useState(initialState);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setpasswordError] = useState<string | null>(null);
  const [successMessage, setsuccessMessage] = useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);

  const [openSignUp, setOpenSignUp] = React.useState(false);
  const handleSignUpOpen = () => setOpenSignUp(true);

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

  const handleForgotPassword = (event: any) => {
    setUserProps({ ...userProps, isForgotPassword: true });
    navigate(RouterPath.REQUEST_OTP);
  };

  const handlePasswordChange = (event: any) => {
    if (!isValidPassword(event.target.value)) {
      setpasswordError(
        "Password must contain - 8 characters, one uppercase, one lowercase, one digit, one special character"
      );
    } else {
      setpasswordError(null);
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
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email: formValues["email"],
        password: formValues["password"],
      }),
    };

    fetch("/services/api/users/login", requestOptions)
      .then((response) => response.text())
      .then((data) => {
        const validateResponse = data.split(",");
        setOpenSnackbar(true);
        setsuccessMessage(validateResponse[0]);
        setUserProps({
          email: formValues["email"],
          isLoggedIn: validateResponse[0] === "User login successful",
          userId: validateResponse[1],
          isVerified: validateResponse[2] === "true",
          location: validateResponse[3],
        });
      });
  };

  const paperStyle = {
    padding: 20,
    height: "50vh",
    width: 350,
    margin: "20px auto",
  };

  return (
    <Grid data-testid={loginTestId}>
      <Paper elevation={10} style={paperStyle}>
        <form className="login-form" onSubmit={handleSubmit}>
          <Grid>
            <h2>Sign In</h2>
          </Grid>

          <TextField
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

          <TextField
            label="Password"
            id="password"
            placeholder="Enter password"
            onChange={handlePasswordChange}
            type="password"
            fullWidth
            required
            variant="filled"
            error={passwordError !== null}
            helperText={(passwordError && passwordError) || ""}
          />

          <div>
            <FormControlLabel
              control={<Checkbox name="checkedB" color="primary" />}
              label="Remember me"
            />
          </div>

          <Button type="submit" color="primary" variant="contained" fullWidth>
            Sign in
          </Button>

          <Snackbar
            open={openSnackbar}
            autoHideDuration={6000}
            message={successMessage}
            onClose={() => setOpenSnackbar(false)}
          />

          <Typography>
            <Link onClick={handleForgotPassword}>Forgot password</Link>
          </Typography>

          <Divider className="divider" />

          <Button
            onClick={handleSignUpOpen}
            variant="contained"
            color="primary"
            fullWidth
            style={{ marginTop: "5%" }}
          >
            Create a new account
          </Button>
          <Registration open={openSignUp} setOpen={setOpenSignUp} />
        </form>
      </Paper>
    </Grid>
  );
}

export default Login;
