export interface PostModel {
  _id: any;
  userId: string;
  title: string;
  description: string;
  location: string;
  crimeType: string;
  isFlagged: boolean;
  flagsCount: number;
  likesCount: number;
  timeCreated: string;
  timeUpdated: string;
  string_id: string;
}
