import React from "react";

export interface UserProps {
  email?: string;
  isVerified?: boolean;
  isLoggedIn?: boolean;
}

export interface UserContextProps {
  userProps: UserProps;
  setUserProps: (userProps: UserProps) => void;
}

const defaultUserProps: UserProps = {
  email: "",
  isVerified: false,
  isLoggedIn: false,
};

export const UserContext = React.createContext<UserContextProps>({
  userProps: defaultUserProps,
  setUserProps: () => {},
});
