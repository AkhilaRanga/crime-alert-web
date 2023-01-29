import { render } from "@testing-library/react";
import Footer, { footerTestId } from "./Footer";
import React from "react";

describe("Footer", () => {
  test("renders Footer!", () => {
    const { getByTestId } = render(<Footer />);
    expect(getByTestId(footerTestId)).toBeDefined();
  });
});
