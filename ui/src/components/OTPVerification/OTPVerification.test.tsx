import { render } from "@testing-library/react";
import React from "react";
import OTPVerification, { otpVerificationTestId } from "./OTPVerification";

describe("OTPVerification", () => {
  test("renders OTPVerification", () => {
    const { getByTestId } = render(<OTPVerification />);
    expect(getByTestId(otpVerificationTestId)).toBeDefined();
  });
});
