import { render } from "@testing-library/react";
import React from "react";
import Header, { headerTestId } from "./Header";

describe("Header", () => {
  test("renders Header", () => {
    const { getByTestId } = render(<Header />);
    expect(getByTestId(headerTestId)).toBeDefined();
  });
});
