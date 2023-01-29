import { render } from "@testing-library/react";
import React from "react";
import App, { appTestId } from "./App";

describe("App", () => {
  test("renders Login", () => {
    const { getByTestId } = render(<App />);
    expect(getByTestId(appTestId)).toBeDefined();
  });
});
