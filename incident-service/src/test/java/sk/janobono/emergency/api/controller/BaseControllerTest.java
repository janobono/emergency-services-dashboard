package sk.janobono.emergency.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import sk.janobono.emergency.BaseTest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseControllerTest extends BaseTest {

    @Autowired
    public ObjectMapper objectMapper;

    protected <T> Page<T> getPage(final JsonNode jsonNode, final Pageable pageable, final Class<T> clazz) {
        return Optional.ofNullable(jsonNode)
                .map(jsonNode1 -> {
                    final long totalElements = jsonNode.get("totalElements").asLong();
                    final List<T> content;
                    try {
                        content = getListFromNode(jsonNode.get("content"), clazz);
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new PageImpl<>(content, pageable, totalElements);
                })
                .orElse(new PageImpl<>(new ArrayList<>(), pageable, 0));
    }

    protected <T> List<T> getListFromNode(final JsonNode node, final Class<T> clazz) throws IOException {
        final List<T> content = new ArrayList<>();
        for (final JsonNode val : node) {
            content.add(objectMapper.readValue(val.traverse(), clazz));
        }
        return content;
    }

    protected RestClient getRestClient() {
        return RestClient.create();
    }

    protected URI getURI(final String path) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + serverPort)
                .path("/api" + path).build().toUri();
    }

    protected URI getURI(final String path, final Map<String, String> pathVars) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + serverPort)
                .path("/api" + path).buildAndExpand(pathVars).toUri();
    }

    protected URI getURI(final String path, final MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + serverPort)
                .path("/api" + path).queryParams(queryParams).build().toUri();
    }

    protected URI getURI(final String path, final Map<String, String> pathVars, final MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:" + serverPort)
                .path("/api" + path).queryParams(queryParams).buildAndExpand(pathVars).toUri();
    }

    protected void addPageableToParams(final MultiValueMap<String, String> params, final Pageable pageable) {
        if (pageable.isPaged()) {
            params.add("page", Integer.toString(pageable.getPageNumber()));
            params.add("size", Integer.toString(pageable.getPageSize()));
            if (pageable.getSort().isSorted()) {
                final StringBuilder sb = new StringBuilder();
                List<Sort.Order> orderList = pageable.getSort().get().filter(Sort.Order::isAscending).collect(Collectors.toList());
                if (!orderList.isEmpty()) {
                    for (final Sort.Order order : orderList) {
                        sb.append(order.getProperty()).append(',');
                    }
                    sb.append("ASC,");
                }
                orderList = pageable.getSort().get().filter(Sort.Order::isDescending).toList();
                if (!orderList.isEmpty()) {
                    for (final Sort.Order order : orderList) {
                        sb.append(order.getProperty()).append(',');
                    }
                    sb.append("DESC,");
                }
                String sort = sb.toString();
                sort = sort.substring(0, sort.length() - 1);
                params.add("sort", sort);
            }
        }
    }
}
