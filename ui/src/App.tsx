import React from "react";
import "./App.css";
import { Button } from "@material-ui/core";
import Registration from "./pages/Registration/Registration";

function App() {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);

  return (
    <>
      <Button onClick={handleOpen} variant="contained" color="primary">
        Create a new account
      </Button>
      <Registration open={open} setOpen={setOpen} />
    </>
  );
}

export default App;
