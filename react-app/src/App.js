import axios from "axios";

function App({ keycloak }) {

    const callProducts = async () => {
        await keycloak.updateToken(30); // refresh token si besoin

        const response = await axios.get(
            "http://localhost:8085/products",
            {
                headers: {
                    Authorization: `Bearer ${keycloak.token}`,
                },
            }
        );

        alert(response.data);
    };

    const callOrders = async () => {
        await keycloak.updateToken(30);

        const response = await axios.post(
            "http://localhost:8085/orders",
            {},
            {
                headers: {
                    Authorization: `Bearer ${keycloak.token}`,
                },
            }
        );

        alert(response.data);
    };

    return (
        <div style={{ padding: "30px", fontFamily: "Arial" }}>
            <h2>React + Keycloak + API Gateway</h2>

            <p>
                 Utilisateur : <b>{keycloak.idTokenParsed?.preferred_username}</b>
            </p>
            <p>
                Email : <b>{keycloak.idTokenParsed?.email}</b>
            </p>
            <p>
                 Rôles :
                <b> {keycloak.tokenParsed?.realm_access?.roles.join(", ")}</b>
            </p>

            <button onClick={callProducts}>
                Voir Produits
            </button>

            <button onClick={callOrders} style={{ marginLeft: "10px" }}>
                Créer Commande
            </button>

            <br /><br />

            <button onClick={() => keycloak.logout()}>
                Se déconnecter
            </button>
        </div>
    );
}

export default App;
