import React from "react";

export const otpVerificationTestId = "otp-verification-test-id";

function OTPVerification() {
  return (
    <div data-testid={otpVerificationTestId}>
      OTP verification for Email Verify/Forgot password
    </div>
  );
}

export default OTPVerification;
