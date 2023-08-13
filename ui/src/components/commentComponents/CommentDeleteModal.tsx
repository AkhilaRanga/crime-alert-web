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

interface CommentDeleteModalProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  commentId?: string;
  userId?: string;
  fetchCommentData?: () => void;
}

function CommentDeleteModal(props: CommentDeleteModalProps) {
  const classes = useStyles();
  const {
    openModal,
    setOpenModal,
    commentId,
    userId,
    fetchCommentData,
  } = props;
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);

  const handleDelete = () => {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
    };

    fetch(`/services/api/comments/${commentId}/${userId}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setFormMessage("Comment deleted");
        setOpenModal(false);
        fetchCommentData && fetchCommentData();
      });
  };

  return (
    <Modal
      open={openModal}
      onClose={() => setOpenModal(false)}
      aria-labelledby="delete-comment-modal"
      aria-describedby="delete-comment-modal-description"
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
              "Do you want to delete this Comment? If yes, please press Delete"
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

export default CommentDeleteModal;
