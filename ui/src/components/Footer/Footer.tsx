import React from "react";
import "./Footer.css";

export const footerTestId = "footer-test-id";

function Footer() {
  return (
    <div className="footer" data-testid={footerTestId}>
      Copyright © 2023 Crime Alert Web App (Akhila and Bhanu)
    </div>
  );
}

export default Footer;
