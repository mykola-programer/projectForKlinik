package ua.nike.project.spring.service.auth.variant2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nike.project.spring.service.UserService;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ua.nike.project.hibernate.entity.User user = userService.findUserByUsername(username);
        if (user != null) {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_" + (user.getRole() != null ? user.getRole().getName() : "ANONYMOUS"));

            return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, grantedAuthorities);
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}
