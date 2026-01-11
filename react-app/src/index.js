import ReactDOM from "react-dom/client";
import App from "./App";
import keycloak from "./keycloak";

const initApp = async () => {
    const authenticated = await keycloak.init({
        onLoad: "login-required",
        pkceMethod: "S256",
        checkLoginIframe: false,
    });

    if (!authenticated) {
        globalThis.location.reload();
        return;
    }

    const root = ReactDOM.createRoot(document.getElementById("root"));
    root.render(<App keycloak={keycloak} />);
};

initApp();
