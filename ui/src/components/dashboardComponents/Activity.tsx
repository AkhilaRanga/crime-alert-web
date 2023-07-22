import React from "react";
import { PostModel } from "../../models/postModel";
import { UserContext } from "../../contexts/UserContext";
import { List, makeStyles } from "@material-ui/core";
import PostListItem from "./PostListItem";

export const activityTestId = "activity-test-id";

function Activity() {
  const [postsList, setPostsList] = React.useState<PostModel[]>();
  const { userProps } = React.useContext(UserContext);
  const userId = userProps.userId;

  React.useEffect(() => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    fetch(`/services/api/posts?userId=${userId}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setPostsList(data);
      })
      .catch((err) => console.error(err));
  }, [userId]);

  const useStyles = makeStyles((theme) => ({
    root: {
      width: "100%",
      maxWidth: 360,
      backgroundColor: theme.palette.background.paper,
    },
  }));

  return (
    <div
      data-testid={activityTestId}
      style={{ height: "80vh", overflow: "scroll" }}
    >
      <List>
        {(postsList && postsList.map((post) => <PostListItem {...post} />)) || (
          <></>
        )}
      </List>
    </div>
  );
}

export default Activity;
