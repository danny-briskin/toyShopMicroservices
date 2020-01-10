package ua.com.univerpulse.toyshop.controllers;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 * on  27.10.2017 for springSecToken project.
 */
//@RestController
//@CrossOrigin
//@Log4j2
public class AuthController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;

//    public AuthController(PasswordEncoder passwordEncoder, UserService userService,
//                          TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
//        this.userService = userService;
//        this.tokenProvider = tokenProvider;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//
//        User user = new User();
//        user.setUsername("admin");
//        user.setPassword(this.passwordEncoder.encode("admin"));
//        this.userService.save(user);
//    }
//
//    @GetMapping("/authenticate")
//    public void authenticate() {
//        // we don't have to do anything here
//        // this is just a secure endpoint and the JWTFilter
//        // validates the token
//        // this service is called at startup of the app to check
//        // if the jwt token is still valid
//    }
//
//    @PostMapping("/login")
//    public String authorize(@NotNull @Valid @RequestBody User loginUser,
//                            HttpServletResponse response) {
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                loginUser.getEmail(), loginUser.getPassword());
//
//        try {
//            this.authenticationManager.authenticate(authenticationToken);
//            return this.tokenProvider.createToken(loginUser.getEmail());
//        } catch (AuthenticationException e) {
//            log.info("Security exception {}", e.getMessage());
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return null;
//        }
//    }
//
//    @PostMapping("/signup")
//    public String signup(@NotNull @RequestBody User signupUser) {
//        if (this.userRepository.existsByEmail(signupUser.getEmail())) {
//            return "EXISTS";
//        }
//
//        signupUser.setPassword(this.passwordEncoder.encode(signupUser.getPassword()));
//        this.userRepository.saveAndFlush(signupUser);
//        return this.tokenProvider.createToken(signupUser.getEmail());
//    }
//
//    @GetMapping("/secret")
//    @CrossOrigin
//    public String secretService() {
//        return "A secret message";
//    }
}