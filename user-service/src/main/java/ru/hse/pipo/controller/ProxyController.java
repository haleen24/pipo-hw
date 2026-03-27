package ru.hse.pipo.controller;

import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequiredArgsConstructor
public class ProxyController {
    private final RestClient proxyRestClient;

    @RequestMapping("/api/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request,
                                        @RequestBody(required = false) byte[] body) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String fullPath = query != null ? path + "?" + query : path;
        var requestBuilder = proxyRestClient
            .method(HttpMethod.valueOf(request.getMethod()))
            .uri(fullPath);
        Collections.list(request.getHeaderNames()).forEach(name -> {
            if (!name.equalsIgnoreCase("Host") && !name.equalsIgnoreCase("Content-Length")) {
                requestBuilder.header(name, request.getHeader(name));
            }
        });
        if (body != null && body.length > 0) {
            requestBuilder.body(body);
        }
        return requestBuilder.exchange((req, res) -> {
            byte[] responseBody = res.getBody().readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            res.getHeaders().forEach((name, values) -> {
                if (!name.equalsIgnoreCase("Transfer-Encoding")) {
                    headers.addAll(name, values);
                }
            });
            return ResponseEntity
                .status(res.getStatusCode())
                .headers(headers)
                .body(responseBody);
        });
    }
}