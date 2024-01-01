import React from "react";
interface PasswordRulesProps {
  passwordState: {
    validLength: boolean;
    hasUppercase: boolean;
    hasLowercase: boolean;
    hasNumber: boolean;
    hasSpecialSymbol: boolean;
  };
}
function PasswordRules({ passwordState }: PasswordRulesProps) {
  return (
    <ul className="text-sm">
      <li
        className={
          passwordState.validLength ? "text-lime-500" : "text-gray-500"
        }
      >
        Minimum 8 characters
      </li>
      <li
        className={
          passwordState.hasUppercase ? "text-lime-500" : "text-gray-500"
        }
      >
        At least one uppercase letter
      </li>
      <li
        className={
          passwordState.hasLowercase ? "text-lime-500" : "text-gray-500"
        }
      >
        At least one lowercase letter
      </li>
      <li
        className={passwordState.hasNumber ? "text-lime-500" : "text-gray-500"}
      >
        At least one number
      </li>
      <li
        className={
          passwordState.hasSpecialSymbol ? "text-lime-500" : "text-gray-500"
        }
      >
        At least one special character
      </li>
    </ul>
  );
}

export default PasswordRules;
