package com.hrs.mediarequesttool.kintone;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.kintone.data.PostData;
import com.hrs.mediarequesttool.kintone.data.PostResponse;
import com.hrs.mediarequesttool.kintone.data.Record;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
import com.hrs.mediarequesttool.pojos.RelationRequest;

public class API {
  KintoneAPIClientHttpRequestFactory requestFactory;
  Gson gson;
  Adapter adapter;

  public API() {
    requestFactory = new KintoneAPIClientHttpRequestFactory();
    adapter = new Adapter();
    gson = new Gson();
  }

  public PostResponse post(RelationRequest request, boolean isUkerukun) throws KintoneException {
    return post(adapter.parse(request, isUkerukun));
  }

  private PostResponse post(Record record) throws KintoneException {
    PostResponse postResponse;
    try {
      PostData post = new PostData();
      post.setApp("141");
      post.setRecord(record);

      ClientHttpRequest request = requestFactory.createRequest(URI.create("https://hrs.cybozu.com/k/v1/record.json"), HttpMethod.POST);

      PrintWriter writer = new PrintWriter(request.getBody());

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
    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
      ClientHttpRequest request = super.createRequest(uri, httpMethod);

      // TODO need parse username:password to BASE64
      request.getHeaders().add("X-Cybozu-Authorization", "bnRxc29sdXRpb25zOm50cS1zb2x1dGlvbnMjMQ==");
      request.getHeaders().add("Authorization", "Basic bnRxc29sdXRpb25zOm50cS1zb2x1dGlvbnMjMQ==");
      request.getHeaders().add("Host", "hrs.cybozu.com:443");

      if (httpMethod == HttpMethod.POST) {
        request.getHeaders().add("Content-Type", "application/json");
      }

      return request;
    }
  }
}
