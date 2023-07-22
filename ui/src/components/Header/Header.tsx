import React from "react";
import "./Header.css";
import crimeAlertLogo from "../../assets/logo.png";

export const headerTestId = "header-test-id";

function Header() {
  return (
    <div className="header" data-testid={headerTestId}>
      <img src={crimeAlertLogo} alt="Crime Alertness" className="logo" />
      <h1 className="title">Crime Alertness</h1>
    </div>
  );
}

export default Header;
