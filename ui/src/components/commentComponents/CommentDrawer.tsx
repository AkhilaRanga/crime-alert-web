import React from "react";
import { Drawer, IconButton, Divider, makeStyles } from "@material-ui/core";
import CloseIcon from "@material-ui/icons/Close";
import { PostModel } from "../../models/postModel";
import { CommentModel } from "../../models/commentModel";
import {
  getCommentsMap,
  getCommentsToParentsMap,
} from "../../utils/commentHelper";
import CommentThread from "./CommentThread";
import CommentCreate from "./CommentCreate";

export const commentDrawerTestId = "comment-drawer-test-id";

interface CommentDrawerProps {
  commentDrawerOpen: boolean;
  setCommentDrawerOpen: (commentDrawerOpen: boolean) => void;
  post: PostModel;
}

function CommentDrawer(props: CommentDrawerProps) {
  const { commentDrawerOpen, setCommentDrawerOpen, post } = props;
  const handleDrawerClose = () => {
    setCommentDrawerOpen(false);
  };

  const drawerWidth = 500;
  const useStyles = makeStyles((theme) => ({
    drawer: {
      width: drawerWidth,
      flexShrink: 0,
    },
    drawerHeader: {
      padding: "12px",
      display: "inline-block",
      margin: 0,
    },
    drawerPaper: {
      width: drawerWidth,
    },
  }));
  const classes = useStyles();

  const [commentsList, setCommentsList] = React.useState<CommentModel[]>([]);
  // Fetch comment data
  const fetchCommentData = React.useCallback(() => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    fetch(
      `/services/api/comments/query?postId=${post.string_id}`,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setCommentsList(data);
      })
      .catch((err) => console.error(err));
  }, [post.string_id]);

  React.useEffect(() => {
    commentDrawerOpen && fetchCommentData();
  }, [fetchCommentData, commentDrawerOpen]);

  // get comment maps
  const commentsMap: Map<string, CommentModel> = getCommentsMap(commentsList);
  const commentsToParentsMap: Map<string, string[]> = getCommentsToParentsMap(
    commentsList
  );

  return (
    <Drawer
      className={classes.drawer}
      variant="persistent"
      anchor="right"
      open={commentDrawerOpen}
      classes={{
        paper: classes.drawerPaper,
      }}
    >
      <div>
        <h3 className={classes.drawerHeader}>{`${post.title} - Comments`}</h3>
        <IconButton onClick={handleDrawerClose} style={{ float: "right" }}>
          <CloseIcon />
        </IconButton>
      </div>
      <Divider />
      <CommentCreate
        postId={post.string_id}
        label="Comment on post"
        parentId={null}
        fetchCommentData={fetchCommentData}
      />
      <Divider />
      {Array.from(commentsToParentsMap.keys()).map((parentId: string) => (
        <CommentThread
          commentId={parentId}
          childrenIds={commentsToParentsMap.get(parentId) || []}
          commentsMap={commentsMap}
          postId={post.string_id}
          fetchCommentData={fetchCommentData}
          key={`comment-thread-${parentId}`}
        />
      ))}
    </Drawer>
  );
}

export default CommentDrawer;
