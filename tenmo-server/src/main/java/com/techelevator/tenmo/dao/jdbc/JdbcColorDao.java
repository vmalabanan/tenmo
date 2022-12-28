package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.ColorDao;
import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JdbcColorDao implements ColorDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcColorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Color changeColor(Color color, int id) {
        String sql = "UPDATE tenmo_user SET avatar_color_id = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, color.getColorId(), id);

        return getColorByUserId(id);
    }

    @Override
    public List<Color> getAll() {
        List<Color> colors = new ArrayList<>();

        String sql = "SELECT avatar_color_id " +
                ", avatar_color_desc " +
                "FROM avatar_color;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            colors.add(mapRowToColor(results));
        }
        return colors;
    }

    @Override
    // returns all colors except the user's current avatar color
    public List<Color> getApplicableColors(User user) {
        int colorId = user.getAvatar().getColor().getColorId();

        List<Color> colors = getAll();
        List<Color> colorsExceptCurrent = colors.stream()
                                                .filter(c -> c.getColorId() != colorId)
                                                .collect(Collectors.toList());

        return colorsExceptCurrent;
    }


    private Color getColorByUserId(int id) {
        String sql = "SELECT ac.avatar_color_id " +
                ", ac.avatar_color_desc " +
                "FROM tenmo_user as tu " +
                "JOIN avatar_color as ac " +
                "ON tu.avatar_color_id = ac.avatar_color_id " +
                "WHERE tu.user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next())
        {
            return mapRowToColor(results);
        }
        else
        {
            return null;
        }
    }

    // helper function to map each row returned from the sql query to a Color object
    private Color mapRowToColor(SqlRowSet rs)
    {
        Color color = new Color();
        color.setColorId(rs.getInt("avatar_color_id"));
        color.setColorDesc(rs.getString("avatar_color_desc"));

        return color;
    }

}
