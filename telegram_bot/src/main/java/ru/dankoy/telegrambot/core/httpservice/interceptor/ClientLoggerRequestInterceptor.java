package ru.dankoy.telegrambot.core.httpservice.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Slf4j
@Component("clientLoggerRequestInterceptor")
public class ClientLoggerRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    logRequest(request, body);
    var response = execution.execute(request, body);
    return logResponse(request, response);
  }

  private void logRequest(HttpRequest request, byte[] body) {
    log.info("Request: {} {}", request.getMethod(), request.getURI());
    logHeaders(request.getHeaders());
    if (body != null && body.length > 0) {
      log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
    }
  }

  private ClientHttpResponse logResponse(HttpRequest request, ClientHttpResponse response)
      throws IOException {
    log.info("Response status: {}", response.getStatusCode());
    logHeaders(response.getHeaders());

    byte[] responseBody = response.getBody().readAllBytes();
    if (responseBody.length > 0) {
      log.info("Response body: {}", new String(responseBody, StandardCharsets.UTF_8));
    }

    // Return wrapped response to allow reading the body again
    return new BufferingClientHttpResponseWrapper(response, responseBody);
  }

  private void logHeaders(HttpHeaders headers) {
    headers.forEach(
        (key, value) -> {
          if (!value.isEmpty()) {
            log.info(
                key,
                value.stream()
                    .filter(v -> !v.isEmpty() || v.isBlank() || v != null)
                    .collect(Collectors.joining(" ")));
          }
        });
  }

  private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse response;

    private volatile byte @Nullable [] body;

    BufferingClientHttpResponseWrapper(ClientHttpResponse response, byte[] responseBody) {
      this.response = response;
      this.body = responseBody;
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
      return this.response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
      return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
      return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
      byte[] body = this.body;
      if (body == null) {
        synchronized (this) {
          body = this.body;
          if (body == null) {
            body = StreamUtils.copyToByteArray(this.response.getBody());
            this.body = body;
          }
        }
      }
      return new ByteArrayInputStream(body);
    }

    @Override
    public void close() {
      this.response.close();
    }
  }
}
