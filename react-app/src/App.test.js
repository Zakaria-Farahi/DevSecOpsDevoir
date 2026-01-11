import { render, screen } from "@testing-library/react";
import App from "./App";

jest.mock("axios", () => ({
  get: jest.fn(),
  post: jest.fn(),
}));

test("renders app header", () => {
  const keycloak = {
    idTokenParsed: {
      preferred_username: "admin1",
      email: "admin1@microservices.com",
    },
    tokenParsed: {
      realm_access: {
        roles: ["MS_ADMIN"],
      },
    },
    logout: jest.fn(),
  };

  render(<App keycloak={keycloak} />);
  const header = screen.getByText(/React \+ Keycloak \+ API Gateway/i);
  expect(header).toBeInTheDocument();
});
