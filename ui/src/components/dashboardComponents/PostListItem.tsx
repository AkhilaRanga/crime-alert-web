import React from "react";
import {
  ListItem,
  CardContent,
  Typography,
  makeStyles,
  Card,
  CardHeader,
  IconButton,
  CardActions,
  MenuItem,
  Menu,
  Snackbar,
} from "@material-ui/core";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FlagIcon from "@material-ui/icons/Flag";
import FlagOutlinedIcon from "@material-ui/icons/FlagOutlined";
import CommentIcon from "@material-ui/icons/Comment";
import CrudModal from "./CrudModal";
import UploadMediaModal from "./UploadMediaModal";
import { PostModel } from "../../models/postModel";
import EditPost from "../postComponents/CreatePost";
import CommentDrawer from "../commentComponents/CommentDrawer";
import DeleteMediaModal from "./DeleteMediaModal";
import GetMediaModal from "./GetMediaModal";

export const postListItemTestId = "post-list-item-test-id";

const useStyles = makeStyles({
  root: {
    minWidth: 1000,
  },
});

interface PostListItemProps {
  post: PostModel;
  isActivity: boolean;
  fetchData?: () => void;
}

function PostListItem(props: PostListItemProps) {
  const { post, isActivity, fetchData } = props;
  const {
    userId,
    title,
    description,
    timeCreated,
    likesCount,
    flagsCount,
    isFlagged,
    crimeType,
    string_id,
  } = post;
  const classes = useStyles();
  const options: Intl.DateTimeFormatOptions = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  const createdDateTime = new Date(
    timeCreated.replace("[UTC]", "")
  ).toLocaleDateString("en-US", options);
  const [open, setOpen] = React.useState(false);
  const [showModal, setShowModal] = React.useState(false);
  const [showMediaModal, setShowMediaModal] = React.useState(false);
  const [showDeleteMediaModal, setShowDeleteMediaModal] = React.useState(false);
  const [showGetMediaModal, setShowGetMediaModal] = React.useState(false);
  const [openEditPost, setOpenEditPost] = React.useState(false);
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [commentDrawerOpen, setCommentDrawerOpen] = React.useState(false);
  const [photos, setPhotos] = React.useState<any>();
  const [videos, setVideos] = React.useState<any>();

  const fetchId = async () => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    const response = await fetch(
      `/services/api/posts/getPhotos?postId=${string_id}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        return data[0];
      })
      .catch((err) => {
        console.error("Request failed", err);
      });

    return response;
  };

  const fetchVideoId = async () => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    const response = await fetch(
      `/services/api/posts/getVideos?postId=${string_id}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        return data[0];
      })
      .catch((err) => {
        console.error("Request failed", err);
      });

    return response;
  };

  const fetchMedia = async (photoId: string) => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    const response = await fetch(
      `/services/api/posts/downloadPhoto/${photoId}`,
      requestOptions
    )
      .then((response) => response.blob())
      .then((data) => {
        console.log("Downloaded photo");
        return URL.createObjectURL(data);
      })
      .catch((err) => {
        console.error("Request failed", err);
      });

    return response;
  };

  const fetchVideoMedia = async (videoId: string) => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    const response = await fetch(
      `/services/api/posts/downloadVideo/${videoId}`,
      requestOptions
    )
      .then((response) => response.blob())
      .then((data) => {
        console.log("Downloaded video");
        return URL.createObjectURL(data);
      })
      .catch((err) => {
        console.error("Request failed", err);
      });

    return response;
  };

  const fetchBoth = async () => {
    const photoId = await fetchId();
    const videoId = await fetchVideoId();
    const imageURL = await fetchMedia(photoId);
    const videoURL = await fetchVideoMedia(videoId);
    setPhotos(imageURL);
    setVideos(videoURL);
  };
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);
  const [formMessage, setFormMessage] = React.useState<string | null>(null);

  function handleOpenMenu(event: React.MouseEvent<HTMLButtonElement>) {
    setAnchorEl(event.currentTarget);
    setOpen(true);
  }
  function handleCloseMenu(event: any) {
    setAnchorEl(null);
    setOpen(false);
  }
  const handleCommentDrawerOpen = () => {
    setCommentDrawerOpen(true);
  };

  const handleLikeFlag = (methodName: string) => {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
    };

    fetch(
      `/services/api/posts/${methodName}/${userId}/${string_id}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        setOpenSnackbar(true);
        setFormMessage(`Post ${methodName} success`);
        fetchData && fetchData();
      });
  };

  return (
    <div data-testid={postListItemTestId}>
      <ListItem>
        <Card className={classes.root}>
          <CardHeader
            action={
              isActivity ? (
                <IconButton aria-label="settings" onClick={handleOpenMenu}>
                  <MoreVertIcon />
                </IconButton>
              ) : (
                <></>
              )
            }
            title={title}
            subheader={`${createdDateTime}; ${crimeType} crime`}
          />
          <CardContent>
            <Menu
              id="basic-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleCloseMenu}
            >
              <MenuItem onClick={() => setOpenEditPost(true)}>Edit</MenuItem>
              <EditPost
                openPostModal={openEditPost}
                setOpenPostModal={setOpenEditPost}
                fetchData={fetchData}
                isEditMode={true}
                post={post}
              />
              <MenuItem onClick={() => setShowModal(true)}> Delete </MenuItem>
              <CrudModal
                openModal={showModal}
                setOpenModal={setShowModal}
                postId={string_id}
                userId={userId}
                fetchData={fetchData}
              />
              <MenuItem onClick={() => setShowMediaModal(true)}>
                Add photo/Video
              </MenuItem>
              <UploadMediaModal
                openModal={showMediaModal}
                setOpenModal={setShowMediaModal}
                postId={string_id}
                userId={userId}
                fetchData={fetchData}
              ></UploadMediaModal>
              <MenuItem onClick={() => setShowDeleteMediaModal(true)}>
                Delete photo/video
              </MenuItem>
              <DeleteMediaModal
                openModal={showDeleteMediaModal}
                setOpenModal={setShowDeleteMediaModal}
                postId={string_id}
                userId={userId}
                fetchData={fetchData}
              ></DeleteMediaModal>
              <MenuItem
                onClick={() => {
                  setShowGetMediaModal(true);
                  fetchBoth();
                }}
              >
                Show photos/Videos
              </MenuItem>
              <GetMediaModal
                openModal={showGetMediaModal}
                setOpenModal={setShowGetMediaModal}
                postId={string_id}
                userId={userId}
                fetchData={fetchData}
                photos={photos}
                videos={videos}
                setPhotos={setPhotos}
                setVideos={setVideos}
              ></GetMediaModal>
            </Menu>
            <Typography variant="body2" component="p">
              {isFlagged ? "*This post was flagged*" : description}
            </Typography>
            <CommentDrawer
              commentDrawerOpen={commentDrawerOpen}
              setCommentDrawerOpen={setCommentDrawerOpen}
              post={post}
            />
          </CardContent>
          <CardActions disableSpacing>
            <IconButton aria-label="like">
              <FavoriteBorderIcon
                color="primary"
                onClick={() => handleLikeFlag("like")}
              />
              <span style={{ fontSize: "smaller" }}>{likesCount}</span>
            </IconButton>
            <IconButton
              aria-label="flag"
              onClick={() => handleLikeFlag("flag")}
            >
              <FlagOutlinedIcon color="primary" />
              <span style={{ fontSize: "smaller" }}>{flagsCount}</span>
            </IconButton>
            <IconButton>
              <CommentIcon color="primary" onClick={handleCommentDrawerOpen} />
            </IconButton>
            <Snackbar
              open={openSnackbar}
              autoHideDuration={6000}
              message={formMessage}
              onClose={() => setOpenSnackbar(false)}
            />
          </CardActions>
        </Card>
      </ListItem>
    </div>
  );
}

export default PostListItem;
