package ninja.parkverbot.restserver.security;

import ninja.parkverbot.restserver.user.User;
import ninja.parkverbot.restserver.user.UserDatabaseService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
            return new CustomUserDetails(user.getLogin(), user.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException("No access to Database");
        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
            throw new UsernameNotFoundException("Invalid Username");
        }
    }
}
