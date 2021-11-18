package ninja.parkverbot.restserver.security;

import ninja.parkverbot.restserver.user.User;
import ninja.parkverbot.restserver.user.UserDatabaseService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDatabaseService userDatabaseService;

    public CustomUserDetailsService(UserDatabaseService userDatabaseService) {
        this.userDatabaseService = userDatabaseService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDatabaseService.getUserByLogin(username);
            return new CustomUserDetails(user.getLogin(), user.getPassword(), getGrantedAuthorities(user));
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException("No access to Database");
        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException("Invalid Username");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) throws SQLException {
        var grantedAuthorities = new ArrayList<GrantedAuthority>();

        grantedAuthorities.add(new SimpleGrantedAuthority(Authority.USER));
        if (userDatabaseService.isAdmin(user)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Authority.ADMIN));
        }

        return grantedAuthorities;
    }

}
