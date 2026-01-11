package ma.enset.orderservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    @PreAuthorize("hasAnyRole('MS_CLIENT','MS_ADMIN')")
    public String getOrders(Authentication authentication) {
        return "Commandes de l'utilisateur : " + authentication.getName();
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
}
