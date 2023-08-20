import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Divider,
  Grid,
  IconButton,
  Modal,
  Snackbar,
  Typography,
} from "@material-ui/core";
import React from "react";
import CloseIcon from "@material-ui/icons/Close";
import { makeStyles } from "@material-ui/core/styles";
import { UserContext } from "../../contexts/UserContext";

const useStyles = makeStyles({
  card: { maxWidth: "80%", left: "30%", top: "25%", position: "fixed" },
  cardHeader: { backgroundColor: "#26a69a", color: "white" },
  cardContent: { maxHeight: 400, overflow: "overlay" },
  typography: {
    paddingLeft: 20,
    paddingRight: 20,
    overflowWrap: "break-word",
  },
});

interface LogoutModalProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  userId?: string;
}

function LogoutModal(props: LogoutModalProps) {
  const classes = useStyles();
  const { openModal, setOpenModal, userId } = props;
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);
  const userContext = React.useContext(UserContext);
  const logout = userContext?.logout;

  const handleLogout = () => {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    };

    fetch(`/services/api/users/logout/${userId}`, requestOptions)
      .then((response) => response.text())
      .then((data) => {
        setFormMessage(data);
        logout && logout();
        setOpenModal(false);
      });
  };

  return (
    <Modal
      open={openModal}
      onClose={() => setOpenModal(false)}
      aria-labelledby="logout-modal"
      aria-describedby="logout-modal-description"
    >
      <Card className={classes.card}>
        <CardHeader
          className={classes.cardHeader}
          action={
            <IconButton
              aria-label="settings"
              onClick={() => setOpenModal(false)}
            >
              <CloseIcon />
            </IconButton>
          }
          title="Logout Confirmation"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <Typography className={classes.typography}>
            <Grid direction="row" container spacing={1}>
              "Do you want to Logout? If yes, please press Confirm"
            </Grid>
          </Typography>
        </CardContent>
        <Divider />
        <CardActions style={{ float: "right" }}>
          <Button autoFocus onClick={handleLogout} color="primary">
            Confirm
          </Button>
          <Snackbar
            open={openSnackbar}
            autoHideDuration={6000}
            message={formMessage}
            onClose={() => setOpenSnackbar(false)}
          />
        </CardActions>
      </Card>
    </Modal>
  );
}

export default LogoutModal;
