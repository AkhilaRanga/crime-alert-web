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
  updateUserProps: (userProps: UserProps) => void;
  logout: () => void;
}

const defaultUserProps: UserProps = {
  email: "",
  userId: "",
  location: "",
  isVerified: false,
  isLoggedIn: false,
  isForgotPassword: false,
};

export const UserContext = React.createContext<UserContextProps | undefined>(
  undefined
);

export const UserProvider: React.FC = ({ children }) => {
  const [userProps, setUserProps] = React.useState<UserProps>(defaultUserProps);

  React.useEffect(() => {
    const storedUserContext = localStorage.getItem("CRIME_ALERT_USER");
    setUserProps(
      storedUserContext ? JSON.parse(storedUserContext) : defaultUserProps
    );
  }, []);

  const updateUserProps = (newUser: UserProps) => {
    setUserProps(newUser);
    localStorage.setItem("CRIME_ALERT_USER", JSON.stringify(newUser));
  };

  const logout = () => {
    setUserProps(defaultUserProps);
    localStorage.removeItem("CRIME_ALERT_USER");
  };

  const contextValue: UserContextProps = {
    userProps,
    updateUserProps,
    logout,
  };

  return (
    <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>
  );
};
