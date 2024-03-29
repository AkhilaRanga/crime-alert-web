import React from "react";
import { Routes, Route } from "react-router-dom";
import Profile from "../Profile/Profile";
import "./Content.css";
import { RouterPath } from "../../constants/routerConstants";
import Home from "../Home/Home";
import OTPVerification from "../OTPVerification/OTPVerification";
import PasswordReset from "../PasswordReset/PasswordReset";
import OTPVerificationRequest from "../OTPVerification/OTPVerificationRequest";
import OTPVerify from "../OTPVerification/OTPVerify";

export const contentTestId = "content-test-id";

function Content() {
  return (
    <div className="content" data-testid={contentTestId}>
      <Routes>
        <Route>
          <Route path={RouterPath.HOME} element={<Home />} />
          <Route path={RouterPath.VERIFY} element={<OTPVerify />} />
          <Route
            path={RouterPath.REQUEST_OTP}
            element={<OTPVerificationRequest />}
          />
          <Route path={RouterPath.RESET_PASSWORD} element={<PasswordReset />} />
        </Route>
      </Routes>
    </div>
  );
}

export default Content;
