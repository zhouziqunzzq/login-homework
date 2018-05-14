package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class User extends BasicModel {
    private int id;
    private String username;
    private String password;

    public User() {
        super();
        this.id = -1;
    }

    public User(Properties properties, int id, String username, String password) {
        super(properties);
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(Properties properties) {
        super(properties);
        this.id = -1;
    }

    public User(ResultSet resultSet) throws SQLException {
        super();
        id = resultSet.getInt("id");
        username = resultSet.getString("username");
        password = resultSet.getString("password");
    }

    public User getUserByUsername(String username) {
        try {
            PreparedStatement sql = getConn().prepareStatement(
                    "SELECT * FROM `users` WHERE `username` = ?");
            sql.setString(1, username);
            ResultSet rs = sql.executeQuery();

            if (!rs.next()) {
                return null;
            } else {
                return new User(rs);
            }

        } catch (SQLException e) {
            System.out.println("Failed to get users");
            e.printStackTrace();
            return null;
        }
    }

    public int insert() {
        try {
            PreparedStatement sql = getConn().prepareStatement(
                    "INSERT INTO `users` ( `username`, `password`) " +
                            "VALUES ( ?, ? )", Statement.RETURN_GENERATED_KEYS);
            sql.setString(1, this.username);
            sql.setString(2, this.password);

            int affectedRows = sql.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = sql.getGeneratedKeys();
                generatedKeys.next();
                this.id = generatedKeys.getInt(1);
                return this.id;
            } else {
                System.out.println(String.format("Failed to insert user(username = %s)", this.username));
                return -1;
            }

        } catch (SQLException e) {
            System.out.println(String.format("Failed to insert user(username = %s)", this.username));
            e.printStackTrace();
            return -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
