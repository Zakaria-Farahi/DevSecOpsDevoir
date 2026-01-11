package ma.enset.orderservice.config;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import ma.enset.orderservice.client.ProductServiceClient;

@Configuration
public class ProductServiceClientConfig {

    @Bean
    public ProductServiceClient productServiceClient(
            @Value("${product-service.base-url}") String baseUrl,
            @Value("${product-service.ssl.key-store}") Resource keyStore,
            @Value("${product-service.ssl.key-store-password}") String keyStorePassword,
            @Value("${product-service.ssl.key-store-type:PKCS12}") String keyStoreType,
            @Value("${product-service.ssl.trust-store}") Resource trustStore,
            @Value("${product-service.ssl.trust-store-password}") String trustStorePassword,
            @Value("${product-service.ssl.trust-store-type:PKCS12}") String trustStoreType
    ) throws Exception {
        SSLContext sslContext = buildSslContext(
                keyStore,
                keyStorePassword,
                keyStoreType,
                trustStore,
                trustStorePassword,
                trustStoreType
        );
        HttpClient httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        return new ProductServiceClient(httpClient, baseUrl);
    }

    private SSLContext buildSslContext(
            Resource keyStore,
            String keyStorePassword,
            String keyStoreType,
            Resource trustStore,
            String trustStorePassword,
            String trustStoreType
    ) throws Exception {
        KeyStore clientKeyStore = KeyStore.getInstance(keyStoreType);
        try (InputStream in = keyStore.getInputStream()) {
            clientKeyStore.load(in, keyStorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, keyStorePassword.toCharArray());

        KeyStore trustedStore = KeyStore.getInstance(trustStoreType);
        try (InputStream in = trustStore.getInputStream()) {
            trustedStore.load(in, trustStorePassword.toCharArray());
        }

        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustedStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }
}
