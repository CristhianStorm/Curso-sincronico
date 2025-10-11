package com.gestion.emprendedora.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gestion.emprendedora.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Service
public class JaaS_TokenService {

    @Value("${jaas.app.id}")
    private String appId;

    @Value("${jaas.key.id}")
    private String keyId;

    private RSAPrivateKey privateKey;

    public JaaS_TokenService() {
        try {
            ClassPathResource resource = new ClassPathResource("ap-private.pk");
            String keyString = new String(resource.getInputStream().readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

            byte[] decodedKey = java.util.Base64.getDecoder().decode(keyString);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la clave privada de JaaS", e);
        }
    }

    public String generarToken(Usuario usuario) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3 * 60 * 60 * 1000; // Válido por 3 horas
        Date exp = new Date(expMillis);

        Algorithm algorithm = Algorithm.RSA256(null, this.privateKey);

        return JWT.create()
            .withKeyId(this.keyId)
            .withIssuer("chat")
            .withAudience("jitsi")
            .withSubject(this.appId)
            .withClaim("room", "*") // Permite al usuario unirse a cualquier sala
            .withClaim("context", new java.util.HashMap<String, Object>() {{
                put("user", new java.util.HashMap<String, String>() {{
                    put("id", usuario.getId().toString());
                    put("name", usuario.getNombreCompleto());
                    put("email", usuario.getEmail());
                }});
            }})
            .withNotBefore(now)
            .withIssuedAt(now)
            .withExpiresAt(exp)
            .sign(algorithm);
    }
}