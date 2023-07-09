import { render } from "@testing-library/react";
import Dashboard, { dashboardTestId } from "./Dashboard";
import React from "react";

describe("Dashboard", () => {
  test("renders Dashboard", () => {
    const { getByTestId } = render(<Dashboard />);
    expect(getByTestId(dashboardTestId)).toBeDefined();
  });
});
