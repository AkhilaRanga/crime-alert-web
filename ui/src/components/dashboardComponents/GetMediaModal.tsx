import {
  Card,
  CardContent,
  CardHeader,
  Divider,
  IconButton,
  ImageList,
  Modal,
  makeStyles,
} from "@material-ui/core";
import CloseIcon from "@material-ui/icons/Close";
import React from "react";

interface GetMediaModal {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  postId: string;
  userId: string;
  photos: any;
  videos: any;
  setPhotos: (photos: any) => void;
  setVideos: (videos: any) => void;
  fetchData?: () => void;
}

const useStyles = makeStyles({
  card: {
    maxHeight: "80%",
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

function GetMediaModal(props: GetMediaModal) {
  const classes = useStyles();
  const { openModal, setOpenModal, photos, videos, setPhotos, setVideos } =
    props;

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
                setPhotos("#");
                setVideos("#");
              }}
            >
              <CloseIcon />
            </IconButton>
          }
          title="Show Images and Videos"
        />
        <Divider />
        <CardContent className={classes.cardContent}>
          <ImageList
            style={{ width: 500, height: 450 }}
            cols={3}
            rowHeight={164}
          >
            <img
              style={{ width: 300, height: 300 }}
              src={photos}
              alt="Loading"
            />
            <video src={videos} width="500" height="500" controls></video>
          </ImageList>
        </CardContent>
        <Divider />
      </Card>
    </Modal>
  );
}
export default GetMediaModal;
