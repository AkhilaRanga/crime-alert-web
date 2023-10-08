import {
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  Divider,
  FormControl,
  FormControlLabel,
  FormLabel,
  IconButton,
  Modal,
  Radio,
  RadioGroup,
  Snackbar,
  makeStyles,
} from "@material-ui/core";
import CloseIcon from "@material-ui/icons/Close";
import React from "react";

interface DeleteMediaModal {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  postId: string;
  userId: string;
  fetchData?: () => void;
}

const useStyles = makeStyles({
  card: {
    maxHeight: "50%",
    maxWidth: "70%",
    left: "30%",
    top: "35%",
    position: "absolute",
  },
  cardHeader: { backgroundColor: "#26a69a", color: "white" },
  cardContent: {
    maxHeight: "100%",
    maxWidth: "100%",
    overflow: "overlay",
    justifyContent: "space-between",
  },
});

function DeleteMediaModal(props: DeleteMediaModal) {
  const classes = useStyles();
  const { openModal, setOpenModal, postId, fetchData } = props;
  const [loading, setLoading] = React.useState<boolean>(false);
  const [media, setMedia] = React.useState("Image");
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);

  const handleDeleteMedia = () => {
    setLoading(true);
    if (media === "Image") {
      console.log("Images Delete");
      const requestOptions = {
        method: "DELETE",
      };

      fetch(`/services/api/posts/deletePhoto/${postId}`, requestOptions)
        .then((response) => response.text())
        .then((data) => {
          console.log(data);
          setFormMessage(data);
          setOpenSnackbar(true);
          setOpenModal(false);
          setLoading(false);
          fetchData && fetchData();
        });
    } else {
      console.log("Videos Delete");
      const requestOptions = {
        method: "DELETE",
      };

      fetch(`/services/api/posts/deleteVideo/${postId}`, requestOptions)
        .then((response) => response.text())
        .then((data) => {
          setFormMessage(data);
          setOpenSnackbar(true);
          setOpenModal(false);
          setLoading(false);
          fetchData && fetchData();
        });
    }
  };
  return (
    <Modal
      open={openModal}
      onClose={() => {
        setOpenModal(false);
      }}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Card className={classes.card}>
        <CardHeader
          className={classes.cardHeader}
          action={
            <IconButton
              aria-label="settings"
              onClick={() => {
                setOpenModal(false);
              }}
            >
              <CloseIcon />
            </IconButton>
          }
          title="Delete Images and Videos"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <FormControl>
            <FormLabel id="demo-radio-buttons-group-label">Media</FormLabel>
            <RadioGroup
              aria-labelledby="demo-radio-buttons-group-label"
              defaultValue="image"
              name="radio-buttons-group"
            >
              <FormControlLabel
                value="image"
                control={<Radio onClick={() => setMedia("Image")} />}
                label="Image"
              />
              <FormControlLabel
                value="video"
                control={<Radio onClick={() => setMedia("Video")} />}
                label="Video"
              />
            </RadioGroup>
            <Button
              className="btn-upload"
              color="primary"
              variant="contained"
              component="span"
              disabled={loading}
              onClick={handleDeleteMedia}
            >
              Delete
            </Button>
          </FormControl>
          <Snackbar
            open={openSnackbar}
            autoHideDuration={6000}
            message={formMessage}
            onClose={() => setOpenSnackbar(false)}
          />
          {loading && <CircularProgress color="secondary" />}
        </CardContent>
        <Divider />
      </Card>
    </Modal>
  );
}
export default DeleteMediaModal;
