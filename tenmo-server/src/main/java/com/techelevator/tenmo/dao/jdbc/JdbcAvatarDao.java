package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.AvatarDao;
import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAvatarDao implements AvatarDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAvatarDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Avatar changeAvatar(Avatar avatar, int id) {
        String sql = "UPDATE tenmo_user SET avatar_id = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, avatar.getAvatarId(), id);

        return getAvatarByUserId(id);
    }

    @Override
    public List<Avatar> getAll() {
        List<Avatar> avatars = new ArrayList<>();

        String sql = "SELECT avatar_id " +
                ", avatar_desc " +
                ", avatar_line_1 " +
                ", avatar_line_2 " +
                ", avatar_line_3 " +
                ", avatar_line_4 " +
                ", avatar_line_5 " +
                "FROM avatar;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            avatars.add(mapRowToAvatar(results, false));
        }

        return avatars;
    }

    @Override
    // returns all avatars except:
    // the user's current avatar, or any single-char avatars that don't match the first char in the user's username
    // (e.g., if username = bob, will only return the single-char avatar 'b' and other non-single-char avatars, except for current avatar)
    public List<Avatar> getApplicableAvatars(User user) {
        int avatarId = user.getAvatar().getAvatarId();
        String firstChar = String.valueOf(user.getUsername().charAt(0));

        List<Avatar> avatars = getAll();
        List<Avatar> applicableAvatars = new ArrayList<>();

        // iterate through avatars list
        for (Avatar avatar : avatars) {
            String avatarDesc = avatar.getAvatarDesc();
            // if avatar != current avatar
            if (avatar.getAvatarId() != avatarId) {
                // if avatar is not a single-char avatar
                // or if the char avatar corresponds to the user's username
                if (avatarDesc.length() > 1 || avatarDesc.length() == 1 && avatarDesc.toLowerCase().equals(firstChar)) {
                    applicableAvatars.add(avatar);
                }
            }
        }
        return applicableAvatars;
    }

    private Avatar getAvatarByUserId(int id) {
        String sql = "SELECT a.avatar_id " +
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
                "ON tu.avatar_color_id = ac.avatar_color_id " +
                "WHERE tu.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next())
        {
            return mapRowToAvatar(results, true);
        }
        else
        {
            return null;
        }
    }

    // helper function to map each row returned from the sql query to an Avatar object
    private Avatar mapRowToAvatar(SqlRowSet rs, boolean setColor)
    {
        Avatar avatar = new Avatar();

        // set avatar
        avatar.setAvatarId(rs.getInt("avatar_id"));
        avatar.setAvatarDesc(rs.getString("avatar_desc"));
        avatar.setAvatarLine1(rs.getString("avatar_line_1"));
        avatar.setAvatarLine2(rs.getString("avatar_line_2"));
        avatar.setAvatarLine3(rs.getString("avatar_line_3"));
        avatar.setAvatarLine4(rs.getString("avatar_line_4"));
        avatar.setAvatarLine5(rs.getString("avatar_line_5"));

        if (setColor) {
            // Set color
            Color color = new Color();
            color.setColorId(rs.getInt("avatar_color_id"));
            color.setColorDesc(rs.getString("avatar_color_desc"));

            avatar.setColor(color);
        }

        return avatar;
    }
}
