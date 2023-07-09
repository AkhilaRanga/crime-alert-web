import { render } from "@testing-library/react";
import React from "react";
import PasswordReset, { resetTestId } from "./PasswordReset";

describe("PasswordReset", () => {
  test("renders PasswordReset", () => {
    const { getByTestId } = render(<PasswordReset />);
    expect(getByTestId(resetTestId)).toBeDefined();
  });
});
