import React from "react";

export interface UserProps {
  email?: string;
  userId?: string;
  location?: string;
  isVerified?: boolean;
  isLoggedIn?: boolean;
  isForgotPassword?: boolean;
}

export interface UserContextProps {
  userProps: UserProps;
  setUserProps: (userProps: UserProps) => void;
}

const defaultUserProps: UserProps = {
  email: "",
  userId: "",
  location: "",
  isVerified: false,
  isLoggedIn: false,
  isForgotPassword: false,
};

export const UserContext = React.createContext<UserContextProps>({
  userProps: defaultUserProps,
  setUserProps: () => {},
});
