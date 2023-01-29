import { render } from "@testing-library/react";
import React from "react";
import Login, { loginTestId } from "./Login";

describe("Login", () => {
  test("renders Login", () => {
    const { getByTestId } = render(<Login />);
    expect(getByTestId(loginTestId)).toBeDefined();
  });
});
