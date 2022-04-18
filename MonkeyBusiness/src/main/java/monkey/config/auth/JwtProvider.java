package monkey.config.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JwtProvider {
    private static final String JWT_SECRET = "SESACSESACSESACSESACSESACSESACSESACSESACSESACSESACSESAC";

    public static Map<String, String> getUserIdAndNicknameFromJWT(String token) {
        Claims claims = getClaims(token);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", claims.get("id", String.class));
        map.put("nickname", claims.get("nickname", String.class));

        return map;
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Claims getClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
