package mx.tecnm.toluca.usuarios.security;
import io.jsonwebtoken.Claims; import io.jsonwebtoken.Jwts; import io.jsonwebtoken.SignatureAlgorithm; import io.jsonwebtoken.io.Decoders; import io.jsonwebtoken.security.Keys;
import java.security.Key; 
import java.util.Date; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 
import java.util.UUID; 
import java.util.concurrent.ConcurrentHashMap; 
import java.util.function.Function;
import jakarta.enterprise.context.ApplicationScoped; 

@ApplicationScoped
public class JwtService {
    private final String SECRET_KEY = "UnaClaveSecretaMuyLargaYSeguraQueNuncaDeberiasHardcodearEnProduccionYMejorUsaVariablesDeEntornoParaJakartaEE";
    private final long JWT_EXPIRATION = 3600000;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String generateToken(UUID userId, String email, String userType, String rolInterno, List<String> accesoModulos) {
        Map<String, Object> claims = new HashMap<>(); claims.put("userId", userId.toString()); claims.put("email", email); claims.put("userType", userType);
        if (rolInterno != null) { claims.put("rolInterno", rolInterno); } claims.put("accesoModulos", accesoModulos);
        return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }
    private Key getSigningKey() { byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); return Keys.hmacShaKeyFor(keyBytes); }
    public Claims extractAllClaims(String token) { return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody(); }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { final Claims claims = extractAllClaims(token); return claimsResolver.apply(claims); }
    public String extractEmail(String token) { return extractClaim(token, Claims::getSubject); }
    public Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }
    public Boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); }
    public Boolean isTokenBlacklisted(String token) { return blacklistedTokens.contains(token); }
    public Boolean validateToken(String token, String userEmail) { return (extractEmail(token).equals(userEmail) && !isTokenExpired(token) && !isTokenBlacklisted(token)); }
    public void blacklistToken(String token) { blacklistedTokens.add(token); }
    public UUID extractUserId(String token) { return UUID.fromString(extractClaim(token, claims -> claims.get("userId", String.class))); }
    @SuppressWarnings("unchecked") public List<String> extractAccesoModulos(String token) { return extractClaim(token, claims -> (List<String>) claims.get("accesoModulos")); }
}