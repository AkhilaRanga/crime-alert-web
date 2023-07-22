export interface PostModel {
  _id: any;
  userId: string;
  title: string;
  description: string;
  crimeType: string;
  isFlagged: boolean;
  likesCount: number;
  timeCreated: Date;
  timeUpdated: Date;
}
