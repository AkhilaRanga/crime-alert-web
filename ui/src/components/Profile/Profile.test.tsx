import { render } from "@testing-library/react";
import React from "react";
import Profile, { profileTestId } from "./Profile";

describe("Profile", () => {
  const renderProfile = () => render(<Profile />);
  test("renders Login", () => {
    const { getByTestId } = renderProfile();
    expect(getByTestId(profileTestId)).toBeDefined();
  });
});
