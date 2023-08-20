import React from "react";
import { PostModel } from "../../models/postModel";
import { UserContext } from "../../contexts/UserContext";
import { List, Button } from "@material-ui/core";
import PostListItem from "./PostListItem";
import CreatePost from "../postComponents/CreatePost";

export const activityTestId = "activity-test-id";

function Activity() {
  const [postsList, setPostsList] = React.useState<PostModel[]>();
  const userContext = React.useContext(UserContext);
  const userProps = userContext?.userProps;
  const userId = userProps?.userId;
  const [openCreatePost, setOpenCreatePost] = React.useState(false);

  const fetchData = React.useCallback(() => {
    const requestOptions = {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    };

    fetch(`/services/api/posts?userId=${userId}`, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setPostsList(data);
      })
      .catch((err) => console.error(err));
  }, [userId]);

  React.useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div
      data-testid={activityTestId}
      style={{ height: "80vh", overflow: "scroll" }}
    >
      <Button
        onClick={() => setOpenCreatePost(true)}
        variant="contained"
        color="primary"
      >
        Create Post
      </Button>
      <CreatePost
        openPostModal={openCreatePost}
        setOpenPostModal={setOpenCreatePost}
        fetchData={fetchData}
      />
      <List>
        {(postsList &&
          postsList.length > 0 &&
          postsList.map((post) => (
            <PostListItem
              post={post}
              isActivity={true}
              fetchData={fetchData}
              key={post.string_id}
            />
          ))) || <>No posts created. Create one using Create Post</>}
      </List>
    </div>
  );
}

export default Activity;
