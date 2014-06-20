package com.hrs.mediarequesttool.kintone;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.codec.Base64;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.common.PropertiesLoader;
import com.hrs.mediarequesttool.kintone.data.PostData;
import com.hrs.mediarequesttool.kintone.data.PostResponse;
import com.hrs.mediarequesttool.kintone.data.Record;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
import com.hrs.mediarequesttool.pojos.RelationRequest;

public class API {
  static private final String JSON_MIME = "application/json";
  static private final String HEADER_HOST = "Host";
  static private final String HEADER_AUTHORIZATION = "Authorization";
  static private final String HEADER_CYBOZU_AUTHORIZATION = "X-Cybozu-Authorization";
  static private final String HEADER_CONTENT_TYPE = "Content-Type";

  private KintoneAPIClientHttpRequestFactory requestFactory;
  private Gson gson;
  private Adapter adapter;

  private URI kintoneURI;
  private String kintoneApplicationID;

  public API() {
    requestFactory = new KintoneAPIClientHttpRequestFactory();
    adapter = new Adapter();
    gson = new Gson();

    // get configuration from .properties
    kintoneURI = URI.create(PropertiesLoader.instance.getKintoneUrl());
    kintoneApplicationID = PropertiesLoader.instance.getKintoneApplicationID();
  }

  public PostResponse post(RelationRequest request, boolean isUkerukun) throws KintoneException, Exception {
    return post(adapter.parse(request, isUkerukun));
  }

  private PostResponse post(Record record) throws KintoneException {
    PostResponse postResponse;
    try {
      PostData post = new PostData();
      post.setApp(kintoneApplicationID);
      post.setRecord(record);

      ClientHttpRequest request = requestFactory.createRequest(kintoneURI, HttpMethod.POST);

      //PrintWriter writer = new PrintWriter(request.getBody());
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(request.getBody(), "UTF-8"));

      writer.write(gson.toJson(post));

      writer.close();
      request.getBody().close();

      ClientHttpResponse response = request.execute();

      postResponse = gson.fromJson(new InputStreamReader(response.getBody()), PostResponse.class);
    } catch (Exception e) {
      throw new KintoneException(e);
    }

    return postResponse;
  }

  class KintoneAPIClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
    private String token;
    private String host;

    KintoneAPIClientHttpRequestFactory() {
      String username = PropertiesLoader.instance.getKintoneUsername();
      String password = PropertiesLoader.instance.getKintonePassword();

      token = generateToken(username, password);
      host = PropertiesLoader.instance.getKintoneHost();
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
      ClientHttpRequest request = super.createRequest(uri, httpMethod);

      request.getHeaders().add(HEADER_CYBOZU_AUTHORIZATION, token);
      request.getHeaders().add(HEADER_AUTHORIZATION, "Basic " + token);
      request.getHeaders().add(HEADER_HOST, host);

      if (httpMethod == HttpMethod.POST) {
        request.getHeaders().add(HEADER_CONTENT_TYPE, JSON_MIME);
      }

      return request;
    }

    private String generateToken(String username, String password) {
      byte[] bytes = Base64.encode((username + ":" + password).getBytes());
      return new String(bytes);
    }
  }
}
