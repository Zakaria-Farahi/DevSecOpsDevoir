import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: process.env.REACT_APP_KEYCLOAK_URL || "http://localhost:8080",
    realm: "microservices-realm",
    clientId: "react-client",
});

export default keycloak;
