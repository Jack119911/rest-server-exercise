package ninja.parkverbot.restserver.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    // ToDo Sanitize Input

    @PostMapping("/")
    public User newUser(@RequestBody User newUser) {
        // ToDo Implement
        System.out.println("New User created with name: " + newUser.getFname() + " " + newUser.getLname());
        return newUser;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User userToUpdate) {
        //ToDo Implement
        System.out.println("User updated with name: " + userToUpdate.getFname() + " " + userToUpdate.getLname());
        return userToUpdate;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        //ToDo Implement
        System.out.println("User requested with id: " + id);
        return new User("test", "test", "test", "test", "test");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        //ToDo Implement
        System.out.println("User deleted with id: " + id);
    }

}
