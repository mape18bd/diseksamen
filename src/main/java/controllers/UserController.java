package controllers;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
/*
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
*/
import model.User;
import utils.Hashing;
import utils.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {

  private static DatabaseController dbCon;

  public UserController() {
    dbCon = new DatabaseController();
  }

  public static User getUser(int token) {

    // Check for connection
    if (dbCon == null) {
      dbCon = new DatabaseController();
    }

    // Build the query for DB
    String sql = "SELECT * FROM user where id=" + token;

    // Actually do the query
    ResultSet rs = dbCon.query(sql);
    User user = null;

    try {
      // Get first object, since we only have one
      if (rs.next()) {
        user =
                new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("email"));

        // return the create object
        return user;
      } else {
        System.out.println("No user found");
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }

    // Return null
    return user;
  }

  /**
   * Get all users in database
   *
   * @return
   */
  public static ArrayList<User> getUsers() {

    // Check for DB connection
    if (dbCon == null) {
      dbCon = new DatabaseController();
    }

    // Build SQL
    String sql = "SELECT * FROM user";

    // Do the query and initialyze an empty list for use if we don't get results
    ResultSet rs = dbCon.query(sql);
    ArrayList<User> users = new ArrayList<User>();

    try {
      // Loop through DB Data
      while (rs.next()) {
        User user =
                new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password"),
                        rs.getString("email"));

        // Add element to list
        users.add(user);
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }

    // Return the list of users
    return users;
  }

  public static User createUser(User user) {

    // Write in log that we've reach this step
    Log.writeLog(UserController.class.getName(), user, "Actually creating a user in DB", 0);

    // Set creation time for user.
    user.setCreatedTime(System.currentTimeMillis() / 1000L);

    // Check for DB Connection
    if (dbCon == null) {
      dbCon = new DatabaseController();
    }

    // Insert the user in the DB
    // TODO: Hash the user password before saving it. - FIX
    int userID = dbCon.insert(
            "INSERT INTO user(first_name, last_name, password, email, created_at) VALUES('"
                    + user.getFirstname()
                    + "', '"
                    + user.getLastname()
                    + "', '"
                    + Hashing.md5(user.getPassword())
                    + "', '"
                    + user.getEmail()
                    + "', "
                    + user.getCreatedTime()
                    + ")");

    if (userID != 0) {
      //Update the userid of the user before returning
      user.setId(userID);
    } else {
      // Return null if user has not been inserted into database
      return null;
    }

    // Return user
    return user;
  }

  /*
  public static boolean deleteUser(String token) {

    //Tjekker for forbindelse til databasen
    if (dbCon == null) {
      dbCon = new DatabaseController();
    }
    DecodedJWT jwt = null;
    try {
      JCEMapper.Algorithm algorithm = Algorithm.HMAC256("secret");
      JWTVerifier verifier = JWT.require(algorithm)
              .withIssuer("auth0")
              .build();
      jwt = verifier.verify(token);
    } catch (JWTVerificationException exception) {

    }

      String sql = "DELETE FROM user WHERE id = " + jwt.getClaim( "userid").asInt();

    return dbCon.insert(sql) == 1;

  }
  */


  public static String loginUser(User user) {
    return null;
  }


  private static class DecodedJWT {
    public JSONDocument.Type getClaim(String userid) {
      return null;
    }
  }

  private static class JWTVerifier {
    public DecodedJWT verify(String token) {
      return null;
    }
  }

  private static class JWT {
    public static Object require(JCEMapper.Algorithm algorithm) {
      return null;
    }

    public static Object create() {
      return null;
    }
  }

  private static class JWTVerificationException extends Throwable {

  }

  private static class JWTCreationException extends Throwable {
  }

  public static boolean updateUser(User user, String id) {

    // Check for DB Connection
    if (dbCon == null) {
      dbCon = new DatabaseController();

    }

    String sql = "SELECT FROM user WHERE id =" + id;

    dbCon.updateUser(sql);


    return false;
  }

}