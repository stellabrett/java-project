package at.codersbay.java.taskapp.rest.restapi.security;

import at.codersbay.java.taskapp.rest.controller.ApplicationController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




    @Component
    public class JwtService {
        private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


        public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            logger.info("extractclaims");
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private Boolean isTokenExpired(String token) {
            logger.info("try if token is expired");
            return extractExpiration(token).before(new Date());
        }

        public Boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            logger.info("extract username");
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }


        public String generateToken(String userName){
            logger.info("try generate token");
            Map<String,Object> claims=new HashMap<>();

            String token = createToken(claims,userName);
            logger.info("token generated");
            return token;
        }

        private String createToken(Map<String, Object> claims, String userName) {

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();

            calendar.add(calendar.get(Calendar.HOUR), 4);
            Date expirationDate = calendar.getTime();

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        }

        private Key getSignKey() {
            try {
                byte[] keyBytes = Decoders.BASE64.decode(SECRET);
                return Keys.hmacShaKeyFor(keyBytes);
            } catch (Exception e) {
                logger.error("eroor loading secret", e);
                throw new RuntimeException("error loading secret", e);
            }
        }
    }

