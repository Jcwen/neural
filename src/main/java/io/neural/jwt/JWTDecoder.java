package io.neural.jwt;

import io.neural.jwt.claim.Claim;
import io.neural.jwt.exceptions.JWTDecodeException;
import io.neural.jwt.header.Header;
import io.neural.jwt.playload.Payload;
import io.neural.jwt.support.TokenUtils;
import io.neural.micro.Base64;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;

/**
 * The JWTDecoder class holds the decode method to parse a given JWT token into it's JWT representation.
 */
public final class JWTDecoder implements DecodedJWT {

    private final String[] parts;
    private final Header header;
    private final Payload payload;

    JWTDecoder(String jwt) throws JWTDecodeException {
        parts = TokenUtils.splitToken(jwt);
        final JWTParser converter = new JWTParser();
        String headerJson;
        String payloadJson;
        try {
            headerJson = newStringUtf8(Base64.getUrlDecoder().decode(parts[0]));
            payloadJson = newStringUtf8(Base64.getUrlDecoder().decode(parts[1]));
        } catch (NullPointerException e) {
            throw new JWTDecodeException("The UTF-8 Charset isn't initialized.", e);
        }
        header = converter.parseHeader(headerJson);
        payload = converter.parsePayload(payloadJson);
    }
    
    public static String newStringUtf8(final byte[] bytes) {
    	return bytes == null ? null : new String(bytes, Charsets.UTF_8);
    }
    
    @Override
    public String getAlgorithm() {
        return header.getAlgorithm();
    }

    @Override
    public String getType() {
        return header.getType();
    }

    @Override
    public String getContentType() {
        return header.getContentType();
    }

    @Override
    public String getKeyId() {
        return header.getKeyId();
    }

    @Override
    public Claim getHeaderClaim(String name) {
        return header.getHeaderClaim(name);
    }

    @Override
    public String getIssuer() {
        return payload.getIssuer();
    }

    @Override
    public String getSubject() {
        return payload.getSubject();
    }

    @Override
    public List<String> getAudience() {
        return payload.getAudience();
    }

    @Override
    public Date getExpiresAt() {
        return payload.getExpiresAt();
    }

    @Override
    public Date getNotBefore() {
        return payload.getNotBefore();
    }

    @Override
    public Date getIssuedAt() {
        return payload.getIssuedAt();
    }

    @Override
    public String getId() {
        return payload.getId();
    }

    @Override
    public Claim getClaim(String name) {
        return payload.getClaim(name);
    }

    @Override
    public Map<String, Claim> getClaims() {
        return payload.getClaims();
    }

    @Override
    public String getHeader() {
        return parts[0];
    }

    @Override
    public String getPayload() {
        return parts[1];
    }

    @Override
    public String getSignature() {
        return parts[2];
    }

    @Override
    public String getToken() {
        return String.format("%s.%s.%s", parts[0], parts[1], parts[2]);
    }
}
