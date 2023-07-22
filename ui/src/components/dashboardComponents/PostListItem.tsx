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
} from "@material-ui/core";
import { PostModel } from "../../models/postModel";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import FavoriteBorderIcon from "@material-ui/icons/FavoriteBorder";
import FlagIcon from "@material-ui/icons/Flag";
import FlagOutlinedIcon from "@material-ui/icons/FlagOutlined";
import CommentIcon from "@material-ui/icons/Comment";

export const postListItemTestId = "post-list-item-test-id";

const useStyles = makeStyles({
  root: {
    minWidth: 1000,
  },
});

function PostListItem(props: PostModel) {
  const {
    title,
    description,
    timeCreated,
    likesCount,
    isFlagged,
    crimeType,
  } = props;
  const classes = useStyles();
  const options: Intl.DateTimeFormatOptions = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  const today = new Date();
  const createdDateTime = today.toLocaleDateString("en-US", options);

  return (
    <div data-testid={postListItemTestId}>
      <ListItem>
        <Card className={classes.root}>
          <CardHeader
            action={
              <IconButton aria-label="settings">
                <MoreVertIcon />
              </IconButton>
            }
            title={title}
            subheader={`${createdDateTime}    ${crimeType} crime`}
          />
          <CardContent>
            <Typography variant="body2" component="p">
              {description}
            </Typography>
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
              <CommentIcon color="primary" />
            </IconButton>
          </CardActions>
        </Card>
      </ListItem>
    </div>
  );
}

export default PostListItem;
