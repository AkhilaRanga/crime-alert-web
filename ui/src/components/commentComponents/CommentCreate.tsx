import React from "react";
import {
  Card,
  CardContent,
  Typography,
  makeStyles,
  CardActions,
  Button,
  Snackbar,
  TextField,
} from "@material-ui/core";
import { UserContext } from "../../contexts/UserContext";

export const commentCreateTestId = "comment-create-test-id";

interface CommentCreateProps {
  postId: string;
  label: string;
  parentId: string | null;
  fetchCommentData?: () => void;
  className?: string;
  showReply?: boolean;
  setShowReply?: (showReply: boolean) => void;
}

function CommentCreate(props: CommentCreateProps) {
  const {
    postId,
    label,
    parentId,
    fetchCommentData,
    className,
    setShowReply,
    showReply,
  } = props;
  const useStyles = makeStyles({
    comment: { margin: "5px", minHeight: "120px" },
  });
  const classes = useStyles();

  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);
  const [commentField, setCommentField] = React.useState<string | undefined>();
  const { userProps } = React.useContext(UserContext);
  const userId = userProps.userId;

  const handleCommentInputChange = (event: any) => {
    const { id, value } = event.target;
    setCommentField(value);
  };

  const handleComment = () => {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        comment: commentField,
        userId: userId,
        parentId: parentId,
        postId: postId,
      }),
    };

    fetch(`/services/api/comments`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setFormMessage("Comment added");
        setCommentField("");
        fetchCommentData && fetchCommentData();
      });
  };

  return (
    <Card className={className || classes.comment}>
      <CardContent>
        <Typography variant="body2" component="p">
          <TextField
            id="comment"
            label={`${label}...`}
            size="small"
            inputProps={{ maxLength: 100 }}
            value={commentField}
            onChange={handleCommentInputChange}
            fullWidth
          />
        </Typography>
      </CardContent>
      <CardActions disableSpacing>
        <Button
          autoFocus
          onClick={handleComment}
          color="primary"
          variant="contained"
          size="small"
          style={{ marginRight: "4px" }}
        >
          {label}
        </Button>
        {showReply && (
          <Button
            onClick={() => setShowReply && setShowReply(false)}
            color="secondary"
            variant="contained"
            size="small"
          >
            Cancel
          </Button>
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

export default CommentCreate;
