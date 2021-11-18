package ninja.parkverbot.restserver.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {

    UserDatabaseService userDatabaseService;
    PasswordEncoder passwordEncoder;

    public UserController(UserDatabaseService userDatabaseService){
        this.userDatabaseService = userDatabaseService;
        createPasswordDecoder();
    }

    // ToDo Sanitize Input
    // ToDo Throw suititable errors
    @PostMapping("/")
    public User newUser(@RequestBody User newUser) throws SQLException {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User createdUser = userDatabaseService.createUser(newUser);
        System.out.println("New User created with id: " + createdUser.getId());
        return createdUser;
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userDatabaseService.userWithIdHasLogin(#id, authentication.name) || hasAuthority('ROLE_ADMIN')")
    public User updateUser(@PathVariable int id, @RequestBody User userToUpdate) throws SQLException {
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        userToUpdate.setId(id);
        var updatedUser = userDatabaseService.updateOrCreateUser(userToUpdate);
        System.out.println("User updated with id: " + updatedUser.getId());
        return updatedUser;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userDatabaseService.userWithIdHasLogin(#id, authentication.name) || hasAuthority('ROLE_ADMIN')")
    public User getUser(@PathVariable int id) throws SQLException {
        try {
            var requestedUser = userDatabaseService.getUser(id);
            System.out.println("User requested with id: " + id);
            return requestedUser;
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    // ToDo change successful response code to 204

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(@PathVariable int id) throws SQLException {
        try{
            userDatabaseService.deleteUser(id);
            System.out.println("User deleted with id: " + id);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    private void createPasswordDecoder() {
        String idForEncode = "bcrypt";
        Map<String,PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(13));
        encoders.put("noop", NoOpPasswordEncoder.getInstance()); // Just used for testing
        passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
