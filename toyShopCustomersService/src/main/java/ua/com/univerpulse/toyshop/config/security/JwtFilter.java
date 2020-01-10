package ua.com.univerpulse.toyshop.config.security;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  10.10.2017 for springSecToken project.
 */
//@AllArgsConstructor
//@Log4j2
public class JwtFilter
//        extends GenericFilterBean
{
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//
//    private TokenProvider tokenProvider;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//        try {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//            String jwt = resolveToken(httpServletRequest);
//            if (jwt != null) {
//                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
//                if (authentication != null) {
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//            filterChain.doFilter(servletRequest, servletResponse);
//        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
//                | SignatureException | UsernameNotFoundException e) {
//            ((HttpServletResponse) servletResponse)
//                    .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            log.catching(e);
//        }
//    }
//
//    @Nullable
//    private static String resolveToken(@NotNull HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
//            throws IOException, ServletException {
//
//        final HttpServletRequest request = (HttpServletRequest) req;
//        final HttpServletResponse response = (HttpServletResponse) res;
//        final String authHeader = request.getHeader("authorization");
//
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//
//            chain.doFilter(req, res);
//        } else {
//
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                throw new ServletException("Missing or invalid Authorization header");
//            }
//
//            final String token = authHeader.substring(7);
//
//            try {
//                final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
//                request.setAttribute("claims", claims);
//            } catch (final SignatureException e) {
//                throw new ServletException("Invalid token");
//            }
//
//            chain.doFilter(req, res);
//        }
//    }
}
