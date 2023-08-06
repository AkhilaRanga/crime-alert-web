export interface CommentModel {
  _id: any;
  userId: string;
  postId: string;
  comment: string;
  parentId: string;
  isFlagged: boolean;
  likesCount: number;
  timeCreated: string;
  timeUpdated: string;
  isDeleted: boolean;
  string_id: string;
}
