import { render } from "@testing-library/react";
import React from "react";
import Registration, { registrationTestId } from "./Registration";

describe("Registration", () => {
  test("renders Login", () => {
    const { getByTestId } = render(
      <Registration open={true} setOpen={jest.fn()} />
    );
    expect(getByTestId(registrationTestId)).toBeDefined();
  });
});
