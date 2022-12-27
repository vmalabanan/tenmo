package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class JdbcUserDao implements UserDao
{

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getIdByUsername(String username)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("Username cannot be null");
        }

        int userId;
        try
        {
            userId = jdbcTemplate.queryForObject("SELECT user_id FROM tenmo_user WHERE username = ?", int.class, username);
        }
        catch (NullPointerException | EmptyResultDataAccessException e)
        {
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }

        return userId;
    }

    @Override
    public User getUserById(int userId)
    {
        String sql = "SELECT tu.user_id " +
                " , tu.username " +
                " , tu.password_hash " +
                " , a.avatar_id " +
                " , a.avatar_desc " +
                " , a.avatar_line_1 " +
                " , a.avatar_line_2 " +
                " , a.avatar_line_3 " +
                " , a.avatar_line_4 " +
                " , a.avatar_line_5 " +
                " , ac.avatar_color_id " +
                " , ac.avatar_color_desc " +
                "FROM tenmo_user as tu " +
                "JOIN avatar as a " +
                "ON tu.avatar_id = a.avatar_id " +
                "JOIN avatar_color as ac " +
                "ON tu.avatar_color_id = ac.avatar_color_id " +
                "WHERE tu.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next())
        {
            return mapRowToUser(results);
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<User> getAll()
    {
        List<User> users = new ArrayList<>();
        String sql = "SELECT tu.user_id " +
                ", tu.username " +
                ", tu.password_hash " +
                ", a.avatar_id " +
                ", a.avatar_desc " +
                ", a.avatar_line_1 " +
                ", a.avatar_line_2 " +
                ", a.avatar_line_3 " +
                ", a.avatar_line_4 " +
                ", a.avatar_line_5 " +
                ", ac.avatar_color_id " +
                ", ac.avatar_color_desc " +
                "FROM tenmo_user as tu " +
                "JOIN avatar as a " +
                "ON tu.avatar_id = a.avatar_id " +
                "JOIN avatar_color as ac " +
                "ON tu.avatar_color_id = ac.avatar_color_id;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next())
        {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public List<User> getAllExceptCurrent(int id) {
        List<User> users = getAll();
        List<User> usersExceptCurrent = users.stream()
                                             .filter(u -> u.getId() != id)
                                             .collect(Collectors.toList());

        return usersExceptCurrent;
    }

    @Override
    public User getByUsername(String username)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("Username cannot be null");
        }

        String sql = "SELECT tu.user_id " +
                " , tu.username " +
                " , tu.password_hash " +
                " , a.avatar_id " +
                " , a.avatar_desc " +
                " , a.avatar_line_1 " +
                " , a.avatar_line_2 " +
                " , a.avatar_line_3 " +
                " , a.avatar_line_4 " +
                " , a.avatar_line_5 " +
                " , ac.avatar_color_id " +
                " , ac.avatar_color_desc " +
                "FROM tenmo_user as tu " +
                "JOIN avatar as a " +
                "ON tu.avatar_id = a.avatar_id " +
                "JOIN avatar_color as ac " +
                "ON tu.avatar_color_id = ac.avatar_color_id " +
                "WHERE tu.username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next())
        {
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password)
    {
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash, avatar_id, avatar_color_id) VALUES (?, ?, ?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        int avatarId = getAvatarId(username);
        int colorId = getColorId();
        Integer newUserId;
        newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash, avatarId, colorId);

        if (newUserId == null)
        {
            return false;
        }

        // create account
        sql = "INSERT INTO account (user_id, balance) values(?, ?)";
        try
        {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        }
        catch (DataAccessException e)
        {
            return false;
        }

        return true;
    }

    // helper function to map each row returned from the sql query to a user object
    private User mapRowToUser(SqlRowSet rs)
    {
        User user = new User();

        // set id, username, password, etc.
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");

        Avatar avatar = new Avatar();
        Color color = new Color();

        // set avatar
        avatar.setAvatarId(rs.getInt("avatar_id"));
        avatar.setAvatarDesc(rs.getString("avatar_desc"));
        avatar.setAvatarLine1(rs.getString("avatar_line_1"));
        avatar.setAvatarLine2(rs.getString("avatar_line_2"));
        avatar.setAvatarLine3(rs.getString("avatar_line_3"));
        avatar.setAvatarLine4(rs.getString("avatar_line_4"));
        avatar.setAvatarLine5(rs.getString("avatar_line_5"));

        // Set color
        color.setColorId(rs.getInt("avatar_color_id"));
        color.setColorDesc(rs.getString("avatar_color_desc"));

        avatar.setColor(color);

        user.setAvatar(avatar);

        return user;
    }

    // helper function to determine the default avatarId to be linked to the user's account when they register
    // By default, the avatar will be the first character of username if it starts w/ a-z; otherwise, a robot
    // Avatars can be changed on client side; this just sets the default when an account is first registered
    private int getAvatarId(String username) {
        char c = username.charAt(0);
        // using placeholder IDs for now, 1-6
        // TODO: Update the returns once I add all the avatars to the avatar table
        switch(c) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 1;
            case 'h':
                return 2;
            case 'i':
                return 3;
            case 'j':
                return 4;
            case 'k':
                return 5;
            case 'l':
                return 6;
            case 'm':
                return 1;
            case 'n':
                return 2;
            case 'o':
                return 3;
            case 'p':
                return 4;
            case 'q':
                return 5;
            case 'r':
                return 6;
            case 's':
                return 1;
            case 't':
                return 2;
            case 'u':
                return 3;
            case 'v':
                return 4;
            case 'w':
                return 5;
            case 'x':
                return 6;
            case 'y':
                return 4;
            case 'z':
                return 5;
            default:
                return 6;
        }

    }

    // helper function to determine the default colorId to be linked to the user's avatar when they register
    // By default, the color will be randomly generated. Colors can be changed on client side;
    // this just sets the default when an account is first registered
    private int getColorId() {
        // generate a random number between 1-7
        int min = 1;
        int max = 7;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;

        }

}
