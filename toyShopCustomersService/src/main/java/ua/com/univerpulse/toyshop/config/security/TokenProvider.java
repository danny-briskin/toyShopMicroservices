package ua.com.univerpulse.toyshop.config.security;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  27.10.2017 for springSecToken project.
 */
//@Component
public class TokenProvider {
//
//    private String secretKey;
//    private long tokenValidityInMilliseconds;
//
//    @Autowired
//    private UserDetailsService userService;
//
//    public TokenProvider(@NotNull Environment env) {
//        String key = env.getProperty("secretKey");
//        String tokenLive = env.getProperty("tokenLive");
//        if (key == null) {
//            key = "key";
//        }
//        if (tokenLive == null) {
//            tokenLive = "100000";
//        }
//        this.secretKey = Base64.getEncoder().encodeToString(key.getBytes());
//        this.tokenValidityInMilliseconds = 1000 * Long.parseLong(tokenLive);
//    }
//
//    public String createToken(String username) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
//
//        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(username)
//                .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
//                .setExpiration(validity).compact();
//    }
//
//    public Authentication getAuthentication(String token) {
//        String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token)
//                .getBody().getSubject();
//        UserDetails userDetails = this.userService.loadUserByUsername(username);
//        return new UsernamePasswordAuthenticationToken(userDetails, "",
//                userDetails.getAuthorities());
//    }
}