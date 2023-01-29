import React from "react";
import "./Header.css";

export const headerTestId = "header-test-id";

function Header() {
  return (
    <div className="header" data-testid={headerTestId}>
      <h1>Crime Alertness</h1>
    </div>
  );
}

export default Header;
