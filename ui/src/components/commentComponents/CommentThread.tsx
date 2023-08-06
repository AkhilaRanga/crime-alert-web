import React from "react";
import { CommentModel } from "../../models/commentModel";
import { makeStyles } from "@material-ui/core";
import CommentCard from "./CommentCard";
import CommentCreate from "./CommentCreate";

export const commentThreadTestId = "comment-thread-test-id";

interface CommentThreadProps {
  commentId: string;
  childrenIds: string[];
  commentsMap: Map<string, CommentModel>;
  postId: string;
  fetchCommentData?: () => void;
}

function CommentThread(props: CommentThreadProps) {
  const {
    commentId,
    childrenIds,
    commentsMap,
    postId,
    fetchCommentData,
  } = props;
  const useStyles = makeStyles({
    parentComment: { margin: "5px", minHeight: "120px" },
    childComment: { margin: "5px 5px 5px 20px", minHeight: "120px" },
  });
  const classes = useStyles();
  const [showReply, setShowReply] = React.useState<boolean>(false);

  const parentComment = commentsMap.get(commentId);

  return (
    <>
      <CommentCard
        comment={parentComment}
        postId={postId}
        className={classes.parentComment}
        fetchCommentData={fetchCommentData}
        setShowReply={setShowReply}
        isParent={true}
      />
      <div>
        {childrenIds.map((childCommentId: string) => (
          <CommentCard
            comment={commentsMap.get(childCommentId)}
            postId={postId}
            className={classes.childComment}
            fetchCommentData={fetchCommentData}
            setShowReply={setShowReply}
          />
        ))}
      </div>
      {showReply && (
        <CommentCreate
          postId={postId}
          label="Reply"
          parentId={commentId}
          fetchCommentData={fetchCommentData}
          className={classes.childComment}
          showReply={showReply}
          setShowReply={setShowReply}
        />
      )}
    </>
  );
}

export default CommentThread;
