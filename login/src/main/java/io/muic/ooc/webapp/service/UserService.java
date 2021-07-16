package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.User;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

@Setter
public class UserService {

    private static final String INSERT_USER_SQL = "INSERT INTO tbl_user (username, password, display_name) VALUES (?,?,?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM tbl_user WHERE username = ?;";


    private DatabaseConnectionService databaseConnectionService;

    // Create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL);

            ps.setString(1, username);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); // Instead of storing raw password, we hash it using BCrypt first and store encrypted version
            ps.setString(3, displayName);
            ps.executeUpdate();
            // manually commit change
            connection.commit();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("Username %s has already been taken.", username));
        } catch (SQLException throwables) {
            throw new UserServiceException(throwables.getMessage());
        }
    }

    // Find user by Username

    public User findByUsername(String username) {
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_SQL);

            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"), // Hashed version
                    resultSet.getString("display_name")
            );

        } catch (SQLException throwables) {
            return null;
        }
    }


    // Delete user
    // List all user
    // Update user by user id


    public static void main(String[] args) throws UserServiceException {
        UserService userService = new UserService();
        userService.setDatabaseConnectionService(new DatabaseConnectionService());

//        //Create new User
//        userService.createUser("Etheon", "12345", "Dragonslayer69");

        User user = userService.findByUsername("Etheon");
        System.out.println(user.getUsername());

    }

}
