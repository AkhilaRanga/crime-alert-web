import React, { useState } from "react";
import {
  Grid,
  Paper,
  TextField,
  FormControlLabel,
  Checkbox,
  Button,
} from "@material-ui/core";
import "./Profile.css";
import {
  isValidPassword,
  isValidPhoneNumber,
} from "../../utils/validationUtils";

export const profileTestId = "profile-test-id";

function Profile() {
  // @TODO Fetch User data
  const fullName = "John Doe";
  const email = "john.doe@gmail.com";
  const location = "RIC";
  const phoneNumber = "999-999-9999";
  const password = "mock-pwd";
  const enableNotifications = true;

  const [passwordError, setpasswordError] = useState<string | null>(null);
  const [phoneNumberError, setPhoneNumberError] = useState<string | null>(null);

  const defaultValues = {
    "full-name": fullName,
    email: email,
    location: location,
    "phone-number": phoneNumber,
    password: password,
    "confirm-password": password,
    "enable-notifications": enableNotifications,
  };
  const [formValues, setFormValues] = useState(defaultValues);

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

  const handleConfirmPasswordChange = (event: any) => {
    const { id, value } = event.target;

    if (formValues["password"] !== value) {
      setpasswordError("Passwords do not match");
    } else {
      setpasswordError(null);
    }
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(formValues);
    // TODO
    // Validate phone number and password if required
    // Should location be searchable
    // send data to api
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

    // fetch("/services/api/users/register", requestOptions)
    //   .then((response) => response.json())
    //   .then((data) => console.log(data));
  };

  const paperStyle = {
    padding: "30 10",
    height: "75vh",
    width: 500,
    margin: "20px auto",
  };

  return (
    <Grid data-testid={profileTestId}>
      <Paper elevation={10} style={paperStyle}>
        <form className="profile-form" onSubmit={handleSubmit}>
          <h2>Profile</h2>
          <TextField
            required
            id="email"
            label="Email"
            variant="filled"
            type="email"
            onChange={handleInputChange}
            disabled={true}
            defaultValue={formValues.email}
            fullWidth
          />
          <TextField
            required
            id="full-name"
            label="Full Name"
            variant="filled"
            onChange={handleInputChange}
            defaultValue={formValues["full-name"]}
            fullWidth
          />
          <TextField
            required
            id="location"
            label="Location"
            variant="filled"
            type="search"
            onChange={handleInputChange}
            defaultValue={formValues.location}
            fullWidth
          />
          <TextField
            required
            id="phone-number"
            label="Phone number"
            variant="filled"
            type="tel"
            placeholder="999-999-9999"
            defaultValue={formValues["phone-number"]}
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
            defaultValue={formValues.password}
            onChange={handlePasswordChange}
            error={passwordError !== null}
            helperText={(passwordError && passwordError) || ""}
            fullWidth
          />
          <TextField
            required
            id="confirm-password"
            label="Confirm Password"
            type="password"
            autoComplete="current-password"
            variant="filled"
            onChange={handleConfirmPasswordChange}
            defaultValue={formValues["confirm-password"]}
            fullWidth
          />
          <FormControlLabel
            control={
              <Checkbox
                color="primary"
                id="enable-notifications"
                checked={formValues["enable-notifications"]}
                onChange={handleCheckboxChange}
              />
            }
            label="Enable notifications"
          />
          <Button variant="contained" color="secondary">
            Deactivate/Delete
          </Button>
          <Button variant="contained" color="primary" type="submit" fullWidth>
            Update
          </Button>
        </form>
      </Paper>
    </Grid>
  );
}

export default Profile;
