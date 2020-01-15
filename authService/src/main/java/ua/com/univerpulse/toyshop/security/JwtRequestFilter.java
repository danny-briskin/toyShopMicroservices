package ua.com.univerpulse.toyshop.security;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicros project.
 */
//@Log4j2
//@Component
public class JwtRequestFilter
//        extends OncePerRequestFilter
{
//    @Autowired
//    private UserDetailsServiceImpl jwtUserDetailsService;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Override
//    protected void doFilterInternal(@NotNull HttpServletRequest request
//            , HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        final String requestTokenHeader = request.getHeader("Authorization");
//        String username = null;
//        String jwtToken = null;
//        // JWT Token is in the form "Bearer token". Remove Bearer word and get
//        // only the Token
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//            } catch (IllegalArgumentException e) {
//                log.warn("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                log.warn("JWT Token has expired");
//            }
//        } else {
//            log.warn("JWT Token does not begin with Bearer String [" + requestTokenHeader + ']');
//        }
//// Once we get the token validate it.
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
//// if token is valid configure Spring Security to manually set
//// authentication
//            if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//// After setting the Authentication in the context, we specify
//// that the current user is authenticated. So it passes the
//// Spring Security Configurations successfully.
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        chain.doFilter(request, response);
//    }
}
