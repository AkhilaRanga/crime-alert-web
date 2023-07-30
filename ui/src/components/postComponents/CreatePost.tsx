import React from "react";
import {
  Modal,
  Box,
  TextField,
  Select,
  InputLabel,
  MenuItem,
  Button,
  Snackbar,
} from "@material-ui/core";
import { boxStyle } from "../../styles/BoxStyle";
import { PostModel } from "../../models/postModel";
import { UserContext } from "../../contexts/UserContext";
import "./CreatePost.css";

export const createPostTestId = "create-post-test-id";

interface CreatePostProps {
  openPostModal: boolean;
  setOpenPostModal: (isOpen: boolean) => void;
  fetchData?: () => void;
  isEditMode?: boolean;
  post?: PostModel;
}

function CreatePost(props: CreatePostProps) {
  const {
    openPostModal,
    setOpenPostModal,
    fetchData,
    isEditMode,
    post,
  } = props;
  const { userProps } = React.useContext(UserContext);
  const userId = userProps.userId;

  const handleClose = () => setOpenPostModal(false);

  const defaultValues = React.useMemo(
    () => ({
      title: post?.title,
      description: post?.description,
      location: post?.location,
    }),
    [post]
  );
  const [formValues, setFormValues] = React.useState(defaultValues);

  const [formMessage, setFormMessage] = React.useState<string | null>(null);
  const [openSnackbar, setOpenSnackbar] = React.useState<boolean>(false);
  const [crimeType, setCrimeType] = React.useState(post?.crimeType || "LOW");

  const handleCrimeTypeChange = (event: any) => {
    setCrimeType(event.target.value);
  };

  const handleInputChange = (event: any) => {
    const { id, value } = event.target;
    setFormValues({
      ...formValues,
      [id]: value,
    });
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(formValues);
    const apiMethodType = !isEditMode ? "POST" : "PUT";
    const apiMethodName = !isEditMode
      ? "/services/api/posts"
      : `/services/api/posts/${post?.string_id}`;

    const requestOptions = {
      method: apiMethodType,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId: userId,
        title: formValues["title"],
        description: formValues["description"],
        location: formValues["location"],
        crimeType: crimeType,
      }),
    };

    fetch(apiMethodName, requestOptions)
      .then((response) => response.text())
      .then((data) => {
        setOpenSnackbar(true);
        setFormMessage(data);
        setOpenPostModal(false);
        setFormValues({
          title: undefined,
          description: undefined,
          location: undefined,
        });
        fetchData && fetchData();
      });
  };

  return (
    <Modal
      open={openPostModal}
      onClose={handleClose}
      aria-labelledby="create post"
      data-testid={createPostTestId}
    >
      <Box sx={boxStyle}>
        <form className="post-form" onSubmit={handleSubmit}>
          <h2>{!isEditMode ? "Create Post" : "Update Post"}</h2>
          <TextField
            required
            id="title"
            label="Title"
            variant="filled"
            inputProps={{ maxLength: 100 }}
            onChange={handleInputChange}
            defaultValue={formValues.title}
            fullWidth
          />
          <TextField
            required
            id="description"
            label="Description"
            variant="filled"
            multiline={true}
            maxRows={4}
            inputProps={{ maxLength: 400 }}
            onChange={handleInputChange}
            defaultValue={formValues.description}
            fullWidth
          />
          <InputLabel id="crime-type">Crime Type</InputLabel>
          <div>
            <Select
              labelId="crime-type"
              id="crime-type-select"
              label="Crime Type"
              variant="filled"
              value={crimeType}
              onChange={handleCrimeTypeChange}
              fullWidth
            >
              <MenuItem value={"LOW"}>Low</MenuItem>
              <MenuItem value={"HIGH"}>High</MenuItem>
            </Select>
          </div>
          <TextField
            required
            id="location"
            label="Location"
            variant="filled"
            type="search"
            onChange={handleInputChange}
            defaultValue={formValues.location}
            fullWidth
          />
          <Button variant="contained" color="primary" type="submit" fullWidth>
            {!isEditMode ? "Create Post" : "Update Post"}
          </Button>
          <Snackbar
            open={openSnackbar}
            autoHideDuration={6000}
            message={formMessage}
            onClose={() => setOpenSnackbar(false)}
          />
        </form>
      </Box>
    </Modal>
  );
}

export default CreatePost;
