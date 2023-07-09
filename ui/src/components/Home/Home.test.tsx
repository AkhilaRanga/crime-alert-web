import { render } from "@testing-library/react";
import React from "react";
import Home, { homeTestId } from "./Home";

describe("Home", () => {
  test("renders Dashboard", () => {
    const { getByTestId } = render(<Home />);
    expect(getByTestId(homeTestId)).toBeDefined();
  });
});
