package tech.jabari.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    // 从配置文件中注入 RSA 私钥 (PKCS#8 格式的 PEM 字符串)
    @Value("${jwt.private-key}")
    private String privateKeyStr;

    // 从配置文件中注入 JWT 过期时间（单位：秒）
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Value("${jwt.token-header}")
    private String tokenHeader;

    /**
     * 从 token 中获取用户ID (Subject)
     */
    public Long getUserIdFromToken(String token) {
        String subject = getClaimFromToken(token, Claims::getSubject);
        return subject != null ? Long.parseLong(subject) : null;
    }

    /**
     * 从 token 中获取用户名 (来自 Claims)
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("username", String.class));
    }

    /**
     * 从 token 中获取用户角色 (来自 Claims)
     */
    public String getRolesFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("roles", String.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 Token 获取 Claims
     * 注意：此方法在网关验证时也会用到，但网关使用的是公钥
     * 在 auth-service 中，我们使用私钥来解析（因为私钥包含公钥信息），但更常见的做法是分开配置。
     * 这里为了简单，auth-service 也使用私钥来解析。生产环境建议 auth-service 也使用公钥解析以保持一致。
     */
    private Claims getAllClaimsFromToken(String token) {
        // 注意：这里使用私钥来解析。对于验证来说，公钥更合适，但私钥也可以。
        // 理想情况下，auth-service 应使用公钥验证，但需要额外配置。
        return Jwts.parserBuilder()
                .setSigningKey(getPrivateKey()) // 使用 getPrivateKey() 来解析
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 根据 UserDetails 生成 Token
     * 将 userId 作为 subject, 将 username 和 roles 放入 claims
     */
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        // 放入用户名
        claims.put("username", userDetails.getUsername());
        // 放入角色（逗号分隔的字符串，例如 "ROLE_USER,ROLE_ADMIN"）
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", roles);

        return doGenerateToken(claims, userId.toString());
    }

    /**
     * 生成 Token 的核心方法
     * 使用 RSA 私钥进行签名 (RS256)
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 这里 subject 是 userId 的字符串形式
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) // 使用私钥和 RS256 算法签名
                .compact();
    }

    /**
     * 验证 Token 是否有效（针对特定用户）
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String usernameFromToken = getUsernameFromToken(token);
        final String usernameFromUserDetails = userDetails.getUsername();
        return (usernameFromToken.equals(usernameFromUserDetails) && !isTokenExpired(token));
    }

    /**
     * 验证 Token 是否有效（不针对特定用户，只验证过期和签名）
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

/*    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenHeader);
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(getTokenPrefix().length());
        }
        return null;
    }*/

    public void invalidateToken(String token) {
        // 示例：可以将 token 加入 Redis 黑名单或本地缓存
        // 此处为示意代码，实际应根据项目技术选型实现
        if (token != null && validateToken(token)) {
            // 假设存在一个 redisTemplate 或 cache
            // redisTemplate.opsForValue().set("blacklist:" + token, "invalid", getExpirationDateFromToken(token).getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 刷新 Token（使用旧的 Token 中的 Claims 生成新 Token）
     */
    public String refreshToken(String oldToken) {
        try {
            final Claims claims = getAllClaimsFromToken(oldToken);
            claims.setIssuedAt(new Date());
            // 注意：这里直接使用 claims 和 subject 生成新 token
            return doGenerateToken(claims, claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public String getTokenPrefix() {
        return tokenPrefix + " ";
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    /**
     * 从配置的字符串加载 RSA 私钥
     */
    private PrivateKey getPrivateKey() {
        try {
            // 移除 PEM 文件中的头尾标识和换行符
            String privateKeyContent = privateKeyStr
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", ""); // 移除所有空白字符

            byte[] keyBytes = java.util.Base64.getDecoder().decode(privateKeyContent);
            java.security.spec.PKCS8EncodedKeySpec keySpec = new java.security.spec.PKCS8EncodedKeySpec(keyBytes);
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
}