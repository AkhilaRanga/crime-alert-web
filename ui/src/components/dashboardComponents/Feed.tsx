import React from "react";
import { PostModel } from "../../models/postModel";
import { UserContext } from "../../contexts/UserContext";
import { List } from "@material-ui/core";
import PostListItem from "./PostListItem";

export const feedTestId = "feed-test-id";

function Feed() {
  const [postsList, setPostsList] = React.useState<PostModel[]>();
  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const userLocation = userProps?.location;
  const fetchData = React.useCallback(() => {
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

  React.useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div
      data-testid={feedTestId}
      style={{ height: "80vh", overflow: "scroll" }}
    >
      <List>
        {(postsList &&
          postsList.length > 0 &&
          postsList.map((post) => (
            <PostListItem
              post={post}
              isActivity={false}
              fetchData={fetchData}
              key={post.string_id}
            />
          ))) || <>No posts available in your location yet</>}
      </List>
    </div>
  );
}

export default Feed;
