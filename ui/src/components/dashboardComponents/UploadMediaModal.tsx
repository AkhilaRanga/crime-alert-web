import {
  Button,
  Card,
  CardContent,
  CardHeader,
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
import CircularProgress from "@material-ui/core/CircularProgress";
import React from "react";

interface UploadMediaModal {
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

function UploadMediaModal(props: UploadMediaModal) {
  const classes = useStyles();
  const { openModal, setOpenModal, postId, fetchData } = props;
  const [loading, setLoading] = React.useState<boolean>(false);
  const [files, setFiles] = React.useState<Blob | any>();
  const [fileName, setFileName] = React.useState<string>("");
  const [media, setMedia] = React.useState("Image");
  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);

  const handleChange = (event: any) => {
    console.log(`Selected file - ${event.target.files[0].name}`);
    setFiles(event.target.files[0]);
    setFileName(event.target.files[0].name);
  };

  const handleUploadMedia = () => {
    setLoading(true);
    if (media === "Image") {
      console.log("Image Uploading");
      const formData = new FormData();
      formData.append("file", files);
      formData.append("postId", postId);
      const requestOptions = {
        method: "POST",
        body: formData,
      };

      fetch(`/services/api/posts/uploadPhoto`, requestOptions)
        .then((response) => {
          if (response.ok) {
            return response.text();
          }
          return Promise.reject(response);
        })
        .then((data) => {
          console.log(data);
          setFormMessage(data);
          setOpenSnackbar(true);
          setFileName("");
          setFiles(undefined);
          setOpenModal(false);
          setLoading(false);
          fetchData && fetchData();
        })
        .catch((response) => {
          response.text().then((text: any) => {
            setOpenSnackbar(true);
            setFormMessage(text);
            setLoading(false);
          });
        });
    } else {
      console.log("Video Uploading");
      const formData = new FormData();
      formData.append("video", files);
      formData.append("videoName", fileName);
      formData.append("postId", postId);
      const requestOptions = {
        method: "POST",
        body: formData,
      };

      fetch(`/services/api/posts/uploadVideo`, requestOptions)
        .then((response) => {
          if (response.ok) {
            return response.text();
          }
          return Promise.reject(response);
        })
        .then((data) => {
          setFormMessage(data);
          setOpenSnackbar(true);
          setOpenModal(false);
          setLoading(false);
          fetchData && fetchData();
        })
        .catch((response) => {
          response.text().then((text: any) => {
            setOpenSnackbar(true);
            setFormMessage(text);
            setLoading(false);
          });
        });
    }
  };
  return (
    <Modal
      open={openModal}
      onClose={() => {
        setFormMessage("");
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
                setFileName("");
              }}
            >
              <CloseIcon />
            </IconButton>
          }
          title="Add Images and Videos"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <FormControl>
            <label htmlFor="btn-upload">
              <input
                id="btn-upload"
                name="btn-upload"
                style={{ display: "none" }}
                type="file"
                accept="image/*"
                onChange={handleChange}
              />
              <Button
                className="btn-upload"
                color="primary"
                variant="contained"
                component="span"
                disabled={Boolean(fileName) || loading}
              >
                Choose Image/Video
              </Button>
              <div className="file-name">{fileName ? fileName : <br></br>}</div>
            </label>

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
              disabled={!fileName || loading}
              onClick={handleUploadMedia}
            >
              Upload
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
export default UploadMediaModal;
