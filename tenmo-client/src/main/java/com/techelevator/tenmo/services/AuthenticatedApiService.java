package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public abstract class AuthenticatedApiService<T>
{

    public static String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();

    protected static String authToken = null;

    public static void setBaseUrl(String baseUrl)
    {
        AuthenticatedApiService.baseUrl = baseUrl;
    }

    public static void setAuthToken(String authToken)
    {
        AuthenticatedApiService.authToken = authToken;
    }


    protected HttpEntity<T> makeAuthEntity(T body)
    {
        HttpHeaders headers = new HttpHeaders();
        if(authToken != null)
        {
            headers.setBearerAuth(authToken);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    protected HttpEntity<Void> makeAuthEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        if(authToken != null)
        {
            headers.setBearerAuth(authToken);
        }
        return new HttpEntity<>(headers);
    }
}
