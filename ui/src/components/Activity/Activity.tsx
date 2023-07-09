import React from "react";

export const activityTestId = "activity-test-id";

function Activity() {
  return (
    <div data-testid={activityTestId}>
      OTP verification for Email Verify/Forgot password
    </div>
  );
}

export default Activity;
