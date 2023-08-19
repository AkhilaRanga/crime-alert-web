import React from "react";
import { CommentModel } from "../../models/commentModel";
import {
  Card,
  CardContent,
  Typography,
  CardActions,
  IconButton,
  Button,
  Snackbar,
  TextField,
  Menu,
  MenuItem,
} from "@material-ui/core";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FlagIcon from "@material-ui/icons/Flag";
import FlagOutlinedIcon from "@material-ui/icons/FlagOutlined";
import ReplyIcon from "@material-ui/icons/Reply";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import { UserContext } from "../../contexts/UserContext";
import CommentDeleteModal from "./CommentDeleteModal";

export const commentCardTestId = "comment-card-test-id";

interface CommentCardProps {
  postId: string;
  className: string;
  comment?: CommentModel;
  fetchCommentData?: () => void;
  setShowReply?: (showReply: boolean) => void;
  isParent?: boolean;
}

function CommentCard(props: CommentCardProps) {
  const {
    comment,
    postId,
    className,
    fetchCommentData,
    setShowReply,
    isParent,
  } = props;

  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);
  const [isEdit, setIsEdit] = React.useState<boolean>(false);
  const [openDeleteModal, setOpenDeleteModal] = React.useState<boolean>(false);
  const [commentField, setCommentField] = React.useState<string | undefined>(
    comment?.comment
  );

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const [openMenu, setOpenMenu] = React.useState(false);
  function handleOpenMenu(event: React.MouseEvent<HTMLButtonElement>) {
    setAnchorEl(event.currentTarget);
    setOpenMenu(true);
  }
  function handleCloseMenu(event: any) {
    setAnchorEl(null);
    setOpenMenu(false);
  }

  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const userId = userProps?.userId;

  const handleCommentInputChange = (event: any) => {
    const { id, value } = event.target;
    setCommentField(value);
  };

  const handleComment = () => {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        comment: commentField,
        userId: userId,
        postId: postId,
      }),
    };

    fetch(`/services/api/comments/${comment?.string_id}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setOpenSnackbar(true);
        setFormMessage("Comment updated");
        setIsEdit(false);
        setCommentField("");
        fetchCommentData && fetchCommentData();
      });
  };

  const handleLikeFlag = (methodName: string) => {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
    };

    fetch(
      `/services/api/comments/${methodName}/${userId}/${comment?.string_id}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        setOpenSnackbar(true);
        setFormMessage(`Comment ${methodName} success`);
        fetchCommentData && fetchCommentData();
      });
  };

  const options: Intl.DateTimeFormatOptions = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  const createdDateTime =
    comment?.timeCreated &&
    new Date(comment.timeCreated.replace("[UTC]", "")).toLocaleDateString(
      "en-US",
      options
    );

  return (
    <Card className={className}>
      <CardContent>
        <Typography variant="body2" component="p">
          {!isEdit || comment?.isDeleted ? (
            <>
              <IconButton
                aria-label="settings"
                style={{ float: "right" }}
                onClick={handleOpenMenu}
              >
                <MoreVertIcon />
              </IconButton>
              <div style={{ fontSize: "smaller", color: "grey" }}>
                {createdDateTime}
              </div>
              <div>
                {comment?.isDeleted
                  ? "*This comment was deleted*"
                  : comment?.isFlagged
                  ? "*This comment was flagged*"
                  : comment?.comment}
              </div>
            </>
          ) : (
            <TextField
              id="comment-edit"
              key={`comment-edit-${comment?.string_id}`}
              label="Comment"
              size="small"
              value={commentField}
              inputProps={{ maxLength: 100 }}
              onChange={handleCommentInputChange}
              fullWidth
            />
          )}
        </Typography>
        <Menu
          id="basic-menu"
          anchorEl={anchorEl}
          open={openMenu}
          onClose={handleCloseMenu}
        >
          <MenuItem
            onClick={() => setIsEdit(true)}
            disabled={comment?.isDeleted || userId !== comment?.userId}
          >
            Edit
          </MenuItem>
          <MenuItem
            onClick={() => setOpenDeleteModal(true)}
            disabled={comment?.isDeleted || userId !== comment?.userId}
          >
            Delete
          </MenuItem>
          <CommentDeleteModal
            openModal={openDeleteModal}
            setOpenModal={setOpenDeleteModal}
            commentId={comment?.string_id}
            userId={userId}
            fetchCommentData={fetchCommentData}
          />
        </Menu>
      </CardContent>
      <CardActions disableSpacing>
        {!isEdit && (
          <>
            <IconButton
              aria-label="like"
              size="small"
              onClick={() => handleLikeFlag("like")}
            >
              <FavoriteBorderIcon color="primary" />
              {comment?.likesCount}
            </IconButton>
            <IconButton
              aria-label="flag"
              size="small"
              onClick={() => handleLikeFlag("flag")}
            >
              <FlagOutlinedIcon color="primary" />
              {comment?.flagsCount}
            </IconButton>
            {isParent && (
              <IconButton
                aria-label="reply"
                onClick={() => setShowReply && setShowReply(true)}
                size="small"
              >
                <ReplyIcon color="primary" />
              </IconButton>
            )}
          </>
        )}
        {isEdit && (
          <div>
            <Button
              autoFocus
              onClick={handleComment}
              color="primary"
              variant="contained"
              size="small"
              style={{ marginRight: "4px" }}
            >
              Update
            </Button>
            <Button
              onClick={() => setIsEdit(false)}
              color="secondary"
              variant="contained"
              size="small"
            >
              Cancel
            </Button>
          </div>
        )}
        <Snackbar
          open={openSnackbar}
          autoHideDuration={6000}
          message={formMessage}
          onClose={() => setOpenSnackbar(false)}
        />
      </CardActions>
    </Card>
  );
}

export default CommentCard;
