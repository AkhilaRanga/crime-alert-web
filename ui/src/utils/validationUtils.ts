const validPassword = new RegExp(
  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
);
const validEmail = new RegExp("^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$");
const validPhoneNumber = new RegExp(
  "^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$"
);

export function isValidEmail(email: string) {
  return validEmail.test(email);
}

export function isValidPassword(password: string) {
  return validPassword.test(password);
}

export function isValidPhoneNumber(phoneNumber: string) {
  return validPhoneNumber.test(phoneNumber);
}
