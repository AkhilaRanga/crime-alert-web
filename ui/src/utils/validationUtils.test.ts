import {
  isValidEmail,
  isValidPassword,
  isValidPhoneNumber,
} from "./validationUtils";

describe("ValidationUtils", () => {
  test("isValidEmail", () => {
    expect(isValidEmail("email")).toEqual(false);
    expect(isValidEmail("email.com")).toEqual(false);
    expect(isValidEmail("email@test.com")).toEqual(true);
  });

  test("isValidPassword", () => {
    expect(isValidPassword("Hello")).toEqual(false);
    expect(isValidPassword("Hello@")).toEqual(false);
    expect(isValidPassword("hello@123")).toEqual(false);
    expect(isValidPassword("Hello@123")).toEqual(true);
  });

  test("isValidPhoneNumber", () => {
    expect(isValidPhoneNumber("999")).toEqual(false);
    expect(isValidPhoneNumber("999-999")).toEqual(false);
    expect(isValidPhoneNumber("9999999999")).toEqual(false);
    expect(isValidPhoneNumber("999-999-9999")).toEqual(true);
  });
});
