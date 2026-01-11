• To verify mTLS is actually enforced between order-service → product-service, do these checks (one should fail, one should succeed):

1. Export the CA cert (so curl can trust the server cert)

keytool -exportcert -alias dev-ca \
-keystore product-service/src/main/resources/certs/truststore.p12 \
-storepass changeit -rfc -file dev-ca.crt

2. Call product-service without client cert (should fail TLS handshake)

curl https://localhost:8081/products \
-H "Authorization: Bearer $TOKEN" \
--cacert dev-ca.crt -v

Expected: TLS error like alert certificate required (handshake fails).

3. Call product-service with client cert (should succeed)

curl https://localhost:8081/products \
-H "Authorization: Bearer $TOKEN" \
--cacert dev-ca.crt \
--cert order-service/src/main/resources/certs/client.p12:changeit \
--cert-type P12 -v

Expected: 200 + response body.

4. End-to-end check through order-service (proves it presents a client cert)

curl http://localhost:8085/orders/products \
-H "Authorization: Bearer $TOKEN" -v

Expected: 200. If you break the client cert config in order-service, you should get 502/Bad Gateway.

If any of these don’t match, tell me which step fails and the exact error; I’ll pinpoint the cause.
