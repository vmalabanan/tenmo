package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Color;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ColorService extends AuthenticatedApiService<Color> {
    public List<Color> getAllColors() {
        List<Color> colors;

        try
        {
            var url = baseUrl + "color";
            var entity = makeAuthEntity();
            ResponseEntity<Color[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Color[].class);
            colors = Arrays.asList(Objects.requireNonNull(response.getBody()));
        }
        catch(Exception ex)
        {
            colors = null;
            BasicLogger.log(ex.getMessage());

        }
        return colors;
    }

    public Color changeColor(Color color) {
        Color newColor;
        try
        {
            var url = baseUrl + "color";
            var entity = makeAuthEntity(color);
            ResponseEntity<Color> response = restTemplate.exchange(url, HttpMethod.POST, entity, Color.class);
            newColor = response.getBody();
        }
        catch(Exception ex)
        {
            newColor = null;
            BasicLogger.log(ex.getMessage());

        }
        return newColor;
    }
}
