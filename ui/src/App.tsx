import React from "react";
import "./App.css";
import { Button } from "@material-ui/core";
import Registration from "./pages/Registration/Registration";
import Login from "./pages/Login/Login";

function App() {
  const [isButtonDisabled, setisButtonDisabled] = React.useState(true);
  //const [open, setOpen] = React.useState(false);
  //const handleOpen = () => setOpen(true);

  /*return (
    <>
      <Button onClick={handleOpen} variant="contained" color="primary">
        Create a new account
      </Button>
      <Registration open={open} setOpen={setOpen} />
    </>
  );*/
  return <Login isButtonDisabled={isButtonDisabled} setisButtonDisabled = {setisButtonDisabled}/>
}

export default App;
