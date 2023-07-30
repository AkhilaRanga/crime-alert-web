export interface PostModel {
  _id: any;
  userId: string;
  title: string;
  description: string;
  location: string;
  crimeType: string;
  isFlagged: boolean;
  likesCount: number;
  timeCreated: Date;
  timeUpdated: Date;
  string_id: string;
}
