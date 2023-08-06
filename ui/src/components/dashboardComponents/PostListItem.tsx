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
} from "@material-ui/core";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FlagIcon from "@material-ui/icons/Flag";
import FlagOutlinedIcon from "@material-ui/icons/FlagOutlined";
import CommentIcon from "@material-ui/icons/Comment";
import CrudModal from "./CrudModal";
import { PostModel } from "../../models/postModel";
import EditPost from "../postComponents/CreatePost";
import CommentDrawer from "../commentComponents/CommentDrawer";

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
  const today = new Date();
  const createdDateTime = today.toLocaleDateString("en-US", options);
  const [open, setOpen] = React.useState(false);
  const [showModal, setShowModal] = React.useState(false);
  const [openEditPost, setOpenEditPost] = React.useState(false);
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [commentDrawerOpen, setCommentDrawerOpen] = React.useState(false);

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
            subheader={`${createdDateTime}    ${crimeType} crime`}
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
              <MenuItem> Add photo/Video </MenuItem>
              <MenuItem> Delete photo/video </MenuItem>
            </Menu>
            <Typography variant="body2" component="p">
              {description}
            </Typography>
            <CommentDrawer
              commentDrawerOpen={commentDrawerOpen}
              setCommentDrawerOpen={setCommentDrawerOpen}
              post={post}
            />
          </CardContent>
          <CardActions disableSpacing>
            <IconButton aria-label="like">
              <FavoriteBorderIcon color="primary" />
              {likesCount}
            </IconButton>
            <IconButton aria-label="share">
              {isFlagged ? (
                <FlagIcon color="primary" />
              ) : (
                <FlagOutlinedIcon color="primary" />
              )}
            </IconButton>
            <IconButton>
              <CommentIcon color="primary" onClick={handleCommentDrawerOpen} />
            </IconButton>
          </CardActions>
        </Card>
      </ListItem>
    </div>
  );
}

export default PostListItem;
