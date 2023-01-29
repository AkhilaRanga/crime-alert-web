import { render } from "@testing-library/react";
import React from "react";
import Content, { contentTestId } from "./Content";
import { BrowserRouter } from "react-router-dom";

describe("Content", () => {
  const renderContent = () =>
    render(
      <BrowserRouter>
        <Content />
      </BrowserRouter>
    );
  test("renders Content", () => {
    const { getByTestId } = renderContent();
    expect(getByTestId(contentTestId)).toBeDefined();
  });
});
