import React, { useState } from "react";
import {
  Box,
  Modal,
  TextField,
  Button,
  Checkbox,
  FormControlLabel,
  Card,
  Snackbar,
} from "@material-ui/core";
import "./Registration.css";
import {
  isValidEmail,
  isValidPassword,
  isValidPhoneNumber,
} from "../../utils/validationUtils";
import { boxStyle } from "../../styles/BoxStyle";

export const registrationTestId = "registration-test-id";

interface RegistrationProps {
  open: boolean;
  setOpen: (open: boolean) => void;
}

const defaultValues = {
  "full-name": "",
  email: "",
  location: "",
  "phone-number": "",
  password: "",
  "enable-notifications": true,
};

function Registration(props: RegistrationProps) {
  const handleClose = () => props.setOpen(false);
  const [formValues, setFormValues] = useState(defaultValues);
  const [formMessage, setFormMessage] = useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
  const [emailError, setEmailError] = useState<string | null>(null);
  const [passwordError, setpasswordError] = useState<string | null>(null);
  const [phoneNumberError, setPhoneNumberError] = useState<string | null>(null);

  const handleInputChange = (event: any) => {
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });
  };

  const handleCheckboxChange = (event: any) => {
    const { id, checked } = event.target;
    setFormValues({
      ...formValues,
      [id]: checked,
    });
  };

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

  const handlePhoneNumberChange = (event: any) => {
    if (!isValidPhoneNumber(event.target.value)) {
      setPhoneNumberError("Phone number is invalid");
    } else {
      setPhoneNumberError(null);
    }
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(formValues);
    // TODO
    // location dropdown
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        fullName: formValues["full-name"],
        email: formValues["email"],
        location: formValues["location"],
        phoneNumber: formValues["phone-number"],
        password: formValues["password"],
        enableNotifications: formValues["enable-notifications"],
      }),
    };

    fetch("/services/api/users/register", requestOptions)
      .then((response) => response.json())
      .then((data) => setFormMessage(data));
  };

  return (
    <>
      <Modal
        open={props.open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
        data-testid={registrationTestId}
      >
        <Box sx={boxStyle}>
          <form className="registration-form" onSubmit={handleSubmit}>
            <h2>Create an Account</h2>
            <TextField
              required
              id="full-name"
              label="Full Name"
              variant="filled"
              onChange={handleInputChange}
              fullWidth
            />
            <TextField
              required
              id="email"
              label="Email"
              variant="filled"
              type="email"
              onChange={handleEmailChange}
              error={emailError !== null}
              helperText={(emailError && emailError) || ""}
              fullWidth
            />
            <TextField
              required
              id="location"
              label="Location"
              variant="filled"
              type="search"
              onChange={handleInputChange}
              fullWidth
            />
            <TextField
              required
              id="phone-number"
              label="Phone number"
              variant="filled"
              type="tel"
              placeholder="999-999-9999"
              onChange={handlePhoneNumberChange}
              error={phoneNumberError !== null}
              helperText={(phoneNumberError && phoneNumberError) || ""}
              fullWidth
            />
            <TextField
              required
              id="password"
              label="Password"
              type="password"
              autoComplete="current-password"
              variant="filled"
              onChange={handlePasswordChange}
              error={passwordError !== null}
              helperText={(passwordError && passwordError) || ""}
              fullWidth
            />
            <FormControlLabel
              control={
                <Checkbox
                  color="primary"
                  id="enable-notifications"
                  defaultChecked
                  onChange={handleCheckboxChange}
                />
              }
              label="Enable notifications"
            />
            <Button variant="contained" color="primary" type="submit" fullWidth>
              Sign up
            </Button>
            <Snackbar
              open={openSnackbar}
              autoHideDuration={6000}
              message={formMessage}
              onClose={() => setOpenSnackbar(false)}
            />
          </form>
        </Box>
      </Modal>
    </>
  );
}

export default Registration;
