import React, { useState } from 'react'
import { Grid,Paper, TextField, Button, Typography,Link } from '@material-ui/core'
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Snackbar from '@material-ui/core/Snackbar';

interface LoginProps {
    isButtonDisabled: boolean;
    setisButtonDisabled: (isButtonDisabled: boolean) => void;
  }

const initialState = {
    username: '',
    password: '',
    
  };

  const validPassword = new RegExp('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$');
  const validEmail = new RegExp('^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$');

  function isValidEmail(email : string) {
    return validEmail.test(email);
  }

  function isValidPassword(password : string)
  {
    return validPassword.test(password);
  }

function Login(props: LoginProps){

    const [formValues, setFormValues] = useState(initialState);
    const [userNameError, setuserNameError] = useState<string | null>(null);
    const [passwordError, setpasswordError] = useState<string | null>(null);
    const [successMessage, setsuccessMessage] = useState<string | null>(null);
    const [open, setOpen] = useState<boolean>(false);


  const handleUserChange = (event: any) => {

    if (!isValidEmail(event.target.value)) {
      setuserNameError('Email is invalid');
    } else {
      setuserNameError(null);
    }
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });


    console.log(formValues.username.trim(),formValues.password.trim() )
    if (formValues.username.trim() && formValues.password.trim()) 
        props.setisButtonDisabled(false);
        
  };


  const handlePasswordChange = (event: any) => {

    if (!isValidPassword(event.target.value)) {
      setpasswordError('Password is invalid');
    } else {
      setpasswordError(null);
    }
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });


    console.log(formValues.username.trim(),formValues.password.trim() )
    if (formValues.username.trim() && formValues.password.trim()) 
        props.setisButtonDisabled(false);
        
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(formValues);
    console.log("On submit");
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        email: formValues["username"],
        password: formValues["password"]
      }),
    };

    fetch("/services/api/users/login", requestOptions)
      .then((response) => response.text())
      .then((data) => {console.log(data);
        setOpen(true);
        setsuccessMessage(data)});
        
    //Need to display response message
  };
    const paperStyle={padding :20,height:'70vh',width:280, margin:"20px auto"}
    const btnstyle={margin:'8px 0'}
    return(
        <form className="registration-form" onSubmit={handleSubmit}>
        <Grid>
            <Paper elevation={10} style={paperStyle}>
                <Grid>
                    <h2>Sign In</h2>
                </Grid>
                
                <TextField  label='Username' id="username" placeholder='Enter username' onChange={handleUserChange} fullWidth required/>
                <div>{userNameError && <h4 style={{color: 'red'}}>{userNameError}</h4>}</div>
                
                <TextField  label='Password' id = "password" placeholder='Enter password' onChange={handlePasswordChange} type='password' fullWidth required/>
                <div>{passwordError && <h4 style={{color: 'red'}}>{passwordError}</h4>}</div>

                <FormControlLabel
                    control={
                    <Checkbox
                        name="checkedB"
                        color="primary"
                    />
                    }
                    label="Remember me"
                 />
                <Button type='submit' color='primary' variant="contained" style={btnstyle} disabled={props.isButtonDisabled} fullWidth>Sign in</Button>
                <Snackbar open={open} autoHideDuration={6000} message = {successMessage} onClose={() => setOpen(false)}/>
                <Typography>
                     <Link href="#" >
                        Forgot password ?
                </Link>
                </Typography>
                <Typography > Do you have an account ?
                     <Link href="#" >
                        Sign Up 
                </Link>
                </Typography>
            </Paper>
        </Grid>
        </form>
    )
}

export default Login