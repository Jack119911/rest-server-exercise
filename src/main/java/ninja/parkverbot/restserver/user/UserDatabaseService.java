package ninja.parkverbot.restserver.user;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.NoSuchElementException;

@Service
public class UserDatabaseService {

    // ToDo check for duplicate name, login and email to throw suiting/more specific error than SQLException
    public User createUser(User newUser) throws SQLException {
        try (var connection = createConnection(); var statement = connection.createStatement()) {
            insertNewUser(newUser, statement);
            return getUserByLogin(newUser, statement);
        }
    }

    // ToDo check for duplicate name, login and email first to throw suiting/more specific error than SQLException
    public User updateOrCreateUser(User userToChange) throws SQLException {
        try (var connection = createConnection(); var statement = connection.createStatement()) {
            ResultSet result = selectUserByID(userToChange.getId(), statement);
            if (result.first()) {
                updateUser(userToChange, statement);
                return getUser(userToChange.getId());
            } else {
                return createUser(userToChange);
            }
        }
    }

    public User getUser(int id) throws SQLException {
        try (var connection = createConnection(); var statement = connection.createStatement()) {
            var result = selectUserByID(id, statement);
            return createUserFromResult(result);
        }
    }

    public void deleteUser(int id) throws SQLException {
        getUser(id);
        try (var connection = createConnection(); var statement = connection.createStatement()) {
            var deleteUser =
                    "DELETE FROM user " +
                    "WHERE id = " + id + ";";
            statement.executeUpdate(deleteUser);
        }
    }

    private void updateUser(User user, Statement statement) throws SQLException {
        var updateUser = String.format(
                    "UPDATE user " +
                    "SET login = '%s', password = '%s', fname = '%s', lname = '%s', email = '%s' " +
                    "WHERE id = %s;",
                user.getLogin(), user.getPasswordHash(), user.getFname(), user.getLname(), user.getEmail(), user.getId()
        );
        statement.executeUpdate(updateUser);
    }

    private void insertNewUser(User newUser, Statement statement) throws SQLException {
        String insertNewUser;

        if (newUser.getId() == User.NO_ID) {
            insertNewUser = String.format(
                    "INSERT INTO user (login, password, fname, lname, email) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s');",
                    newUser.getLogin(), newUser.getPasswordHash(), newUser.getFname(), newUser.getLname(), newUser.getEmail()
            );
        } else {
            insertNewUser = String.format(
                    "INSERT INTO user (id, login, password, fname, lname, email) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",
                    newUser.getId(), newUser.getLogin(), newUser.getPasswordHash(), newUser.getFname(), newUser.getLname(), newUser.getEmail()
            );
        }
        System.out.println(insertNewUser);
        statement.executeUpdate(insertNewUser);
    }

    private ResultSet selectUserByID(int id, Statement statement) throws SQLException {
        var selectUser =
                "SELECT * " +
                        "FROM user " +
                        "WHERE id = " + id + ";";
        return statement.executeQuery(selectUser);
    }

    private User getUserByLogin(User newUser, Statement statement) throws SQLException {
        var selectUser =
                "SELECT * " +
                "FROM user " +
                "WHERE login='" + newUser.getLogin() + "';";
        var result = statement.executeQuery(selectUser);
        return createUserFromResult(result);
    }

    private User createUserFromResult(ResultSet result) throws SQLException {
        var hasEntry = result.first();
        if (!hasEntry) throw new NoSuchElementException("No user with this id");

        var id = result.getInt("id");
        var login = result.getString("login");
        var passwordHash = result.getString("password");
        var fname = result.getString("fname");
        var lname = result.getString("lname");
        var email = result.getString("email");

        var createdUser = new User(login, passwordHash, fname, lname, email);
        createdUser.setId(id);
        return createdUser;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/restserver", "server", "server");
    }

}
