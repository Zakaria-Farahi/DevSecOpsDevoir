package ma.enset.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ma.enset.orderservice.client.ProductServiceClient;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final ProductServiceClient productServiceClient;

    public OrderController(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MS_CLIENT','MS_ADMIN')")
    public String getOrders(Authentication authentication) {
        return "Commandes de l'utilisateur : " + authentication.getName();
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('MS_CLIENT','MS_ADMIN')")
    public String getOrdersWithProducts(Authentication authentication) {
        String bearerToken = extractBearerToken(authentication);
        try {
            return productServiceClient.fetchProducts(bearerToken);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Product service call interrupted", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Product service call failed", ex);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MS_CLIENT','MS_ADMIN')")
    public String createOrder(Authentication authentication) {
        return "Commande créée par : " + authentication.getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('MS_ADMIN')")
    public String adminOrders() {
        return "Gestion des commandes (ADMIN)";
    }

    private String extractBearerToken(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token is missing");
    }
}
