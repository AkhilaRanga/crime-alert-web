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
  card: { maxWidth: "80%", left: "15%", top: "10%", position: "fixed" },
  cardHeader: { backgroundColor: "#0288d1", color: "white" },
  cardContent: { maxHeight: 400, overflow: "overlay" },
  typography: {
    paddingLeft: 20,
    paddingRight: 20,
    overflowWrap: "break-word",
  },
});

function CrudModal(props: any) {
  const classes = useStyles();
  const { openModal, postId, userId } = props;
  const [open, setOpen] = React.useState(false);
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);

  React.useEffect(() => {
    setOpen(openModal);
  }, [openModal]);

  const handleDelete = () => {
    console.log("Call Delete Logic" + postId);
    const requestOptions = {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
    };
    fetch(`/services/api/posts/${postId}/${userId}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setFormMessage(data);
        setOpen(false);
        window.location.reload();
      });
  };
  return (
    <Modal
      open={open}
      onClose={() => setOpen(false)}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Card className={classes.card}>
        <CardHeader
          className={classes.cardHeader}
          action={
            <IconButton aria-label="settings" onClick={() => setOpen(false)}>
              <CloseIcon />
            </IconButton>
          }
          title="Delete Confirmation"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <Typography className={classes.typography}>
            <Grid direction="row" container spacing={1}>
              "Do you want to delete the post? If yes, please press Delete"
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
