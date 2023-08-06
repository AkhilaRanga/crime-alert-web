import { CommentModel } from "../models/commentModel";

export const getCommentsMap = (commentsList: CommentModel[]) => {
  const commentsMap: Map<string, CommentModel> = new Map<
    string,
    CommentModel
  >();
  commentsList.forEach((comment: CommentModel) => {
    if (!commentsMap.has(comment.string_id)) {
      commentsMap.set(comment.string_id, comment);
    }
  });
  return commentsMap;
};

export const getCommentsToParentsMap = (commentsList: CommentModel[]) => {
  const commentsToParentsMap: Map<string, string[]> = new Map<
    string,
    string[]
  >();
  commentsList.forEach((comment: CommentModel) => {
    if (!comment.parentId || comment.parentId === null) {
      !commentsToParentsMap.has(comment.string_id) &&
        commentsToParentsMap.set(comment.string_id, []);
    } else {
      const parentId = comment.parentId;
      const commentIdsList: string[] = commentsToParentsMap.get(parentId) || [];
      commentsToParentsMap.set(comment.parentId, [
        ...commentIdsList,
        comment.string_id,
      ]);
    }
  });
  return commentsToParentsMap;
};
