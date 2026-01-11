package ma.enset.productservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    @PreAuthorize("hasAnyRole('MS_CLIENT','MS_ADMIN')")
    public String products(Authentication auth) {
        return "Produits accessibles pour : " + auth.getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('MS_ADMIN')")
    public String adminProducts(Authentication auth) {
        return "Gestion des produits (ADMIN) : " + auth.getName();
    }
}
