import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../Login/Login";
import Profile from "../Profile/Profile";
import "./Content.css";

export const contentTestId = "content-test-id";

function Content() {
  // @TODO Fetch User data and pass to Profile
  const fullName = "John Doe";
  const email = "john.doe@gmail.com";
  const location = "RIC";
  const phoneNumber = "999-999-9999";
  const password = "mock-pwd";
  const enableNotifications = true;

  return (
    <div className="content" data-testid={contentTestId}>
      <Routes>
        <Route>
          <Route path="/" element={<Login />} />
          <Route
            path="/profile"
            element={
              <Profile
                fullName={fullName}
                email={email}
                location={location}
                password={password}
                phoneNumber={phoneNumber}
                enableNotifications={enableNotifications}
              />
            }
          />
        </Route>
      </Routes>
    </div>
  );
}

export default Content;
