import React from "react";
import { PostModel } from "../../models/postModel";
import { UserContext } from "../../contexts/UserContext";
import { List } from "@material-ui/core";
import PostListItem from "./PostListItem";

export const feedTestId = "feed-test-id";

function Feed() {
  const [postsList, setPostsList] = React.useState<PostModel[]>();
  const { userProps } = React.useContext(UserContext);
  const userLocation = userProps.location;

  React.useEffect(() => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    fetch(`/services/api/posts?location=${userLocation}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        setPostsList(data);
      })
      .catch((err) => console.error(err));
  }, [userLocation]);

  return (
    <div
      data-testid={feedTestId}
      style={{ height: "80vh", overflow: "scroll" }}
    >
      <List>
        {(postsList &&
          postsList.map((post) => (
            <PostListItem post={post} isActivity={false} />
          ))) || <></>}
      </List>
    </div>
  );
}

export default Feed;
