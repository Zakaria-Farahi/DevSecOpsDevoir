package ma.enset.orderservice.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ma.enset.orderservice.client.ProductServiceClient;
import ma.enset.orderservice.config.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceClient productServiceClient;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void getOrders_returnsOk() throws Exception {
        mockMvc.perform(get("/orders")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MS_CLIENT"))))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Commandes")));
    }

    @Test
    void createOrder_returnsOk() throws Exception {
        mockMvc.perform(post("/orders")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MS_CLIENT"))))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Commande créée")));
    }

    @Test
    void getOrdersWithProducts_returnsOk() throws Exception {
        when(productServiceClient.fetchProducts("test-token")).thenReturn("products");

        mockMvc.perform(get("/orders/products")
                        .with(jwt().jwt(jwt -> jwt.tokenValue("test-token"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MS_CLIENT"))))
                .andExpect(status().isOk())
                .andExpect(content().string("products"));

        verify(productServiceClient).fetchProducts("test-token");
    }

    @Test
    void adminOrders_returnsOk() throws Exception {
        mockMvc.perform(get("/orders/admin")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MS_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ADMIN")));
    }
}
