package ua.com.univerpulse.toyshop.model.services;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.com.univerpulse.toyshop.model.entities.security.User;
import ua.com.univerpulse.toyshop.model.repositories.RoleRepository;
import ua.com.univerpulse.toyshop.model.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Component
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public final UserDetails loadUserByUsername(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + email + "' not found");
        }
        List<String> roles = roleRepository.getRoleNamesByUser(email);
        return org.springframework.security.core.userdetails.User.withUsername(email)
                .password(user.getPassword())
                .authorities(this.buildUserAuthority(roles))
                .accountExpired(false).accountLocked(false).credentialsExpired(false)
                .disabled(false).build();
    }

    @Contract("_ -> new")
    private List<GrantedAuthority> buildUserAuthority(@NotNull List<String> userRoles) {
        return userRoles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}