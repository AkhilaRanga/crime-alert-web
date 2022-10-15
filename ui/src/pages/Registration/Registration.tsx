import React, { useState } from "react";
import {
  Box,
  Modal,
  TextField,
  Button,
  Checkbox,
  FormControlLabel,
} from "@material-ui/core";
import "./Registration.css";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

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

    fetch("/services/api/users/register", requestOptions)
      .then((response) => response.json())
      .then((data) => console.log(data));
  };

  return (
    <>
      <Modal
        open={props.open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <form className="registration-form" onSubmit={handleSubmit}>
            <h2>Create an Account</h2>
            <div>
              <TextField
                required
                id="full-name"
                label="Full Name"
                variant="filled"
                onChange={handleInputChange}
              />
            </div>
            <div>
              <TextField
                required
                id="email"
                label="Email"
                variant="filled"
                type="email"
                onChange={handleInputChange}
              />
            </div>
            <div>
              <TextField
                required
                id="location"
                label="Location"
                variant="filled"
                type="search"
                onChange={handleInputChange}
              />
            </div>
            <div>
              <TextField
                required
                id="phone-number"
                label="Phone number"
                variant="filled"
                type="tel"
                onChange={handleInputChange}
              />
            </div>
            <div>
              <TextField
                required
                id="password"
                label="Password"
                type="password"
                autoComplete="current-password"
                variant="filled"
                onChange={handleInputChange}
              />
            </div>
            <div>
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
            </div>
            <div>
              <Button variant="contained" color="primary" type="submit">
                Sign up
              </Button>
            </div>
          </form>
        </Box>
      </Modal>
    </>
  );
}

export default Registration;
