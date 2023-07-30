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

interface CrudModalProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  postId: string;
  userId: string;
  fetchData?: () => void;
}

function CrudModal(props: CrudModalProps) {
  const classes = useStyles();
  const { openModal, setOpenModal, postId, userId, fetchData } = props;
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);

  const handleDelete = () => {
    console.log("Call Delete Logic" + postId);
    const requestOptions = {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    };
    fetch(`/services/api/posts/${postId}/${userId}`, requestOptions)
      .then((response) => response.text())
      .then((data) => {
        setFormMessage(data);
        setOpenModal(false);
        fetchData && fetchData();
      });
  };
  return (
    <Modal
      open={openModal}
      onClose={() => setOpenModal(false)}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
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
          title="Delete Confirmation"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <Typography className={classes.typography}>
            <Grid direction="row" container spacing={1}>
              "Do you want to delete the post (Includes photos/videos)? If yes,
              please press Delete"
            </Grid>
          </Typography>
        </CardContent>
        <Divider />
        <CardActions style={{ float: "right" }}>
          <Button autoFocus onClick={handleDelete} color="primary">
            Delete
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

export default CrudModal;
