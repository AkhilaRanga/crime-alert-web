import { render } from "@testing-library/react";
import React from "react";
import Profile, { profileTestId } from "./Profile";

describe("Profile", () => {
  const renderProfile = (
    fullName: string,
    email: string,
    location: string,
    phoneNumber: string,
    password: string,
    enableNotifications: boolean
  ) =>
    render(
      <Profile
        fullName={fullName}
        email={email}
        location={location}
        phoneNumber={phoneNumber}
        password={password}
        enableNotifications={enableNotifications}
      />
    );
  test("renders Login", () => {
    const { getByTestId } = renderProfile(
      "Name",
      "email@test.com",
      "state",
      "999-999-9999",
      "hell@12",
      true
    );
    expect(getByTestId(profileTestId)).toBeDefined();
  });
});
