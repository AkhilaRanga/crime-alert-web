import { render } from "@testing-library/react";
import React from "react";
import Activity, { activityTestId } from "./Activity";

describe("Activity", () => {
  test("renders Activity", () => {
    const { getByTestId } = render(<Activity />);
    expect(getByTestId(activityTestId)).toBeDefined();
  });
});
