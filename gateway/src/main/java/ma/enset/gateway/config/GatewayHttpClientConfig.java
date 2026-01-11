package ma.enset.gateway.config;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

@Configuration
public class GatewayHttpClientConfig {

    @Bean
    public SslContext gatewaySslContext(
            @Value("${gateway.mtls.key-store}") Resource keyStore,
            @Value("${gateway.mtls.key-store-password}") String keyStorePassword,
            @Value("${gateway.mtls.trust-store}") Resource trustStore,
            @Value("${gateway.mtls.trust-store-password}") String trustStorePassword
    ) throws Exception {
        KeyManagerFactory keyManagerFactory = buildKeyManagerFactory(keyStore, keyStorePassword);
        TrustManagerFactory trustManagerFactory = buildTrustManagerFactory(trustStore, trustStorePassword);
        return SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();
    }

    @Bean
    public HttpClientCustomizer httpClientCustomizer(SslContext sslContext) {
        return httpClient -> httpClient.secure(ssl -> ssl.sslContext(sslContext));
    }

    private KeyManagerFactory buildKeyManagerFactory(Resource keyStore, String password) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream in = keyStore.getInputStream()) {
            ks.load(in, password.toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());
        return kmf;
    }

    private TrustManagerFactory buildTrustManagerFactory(Resource trustStore, String password) throws Exception {
        KeyStore ts = KeyStore.getInstance("PKCS12");
        try (InputStream in = trustStore.getInputStream()) {
            ts.load(in, password.toCharArray());
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);
        return tmf;
    }
}
