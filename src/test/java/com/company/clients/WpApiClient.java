package com.company.clients;

import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.company.config.Config;
import com.company.models.wp.PostRequest;

/** Клиент для работы с WordPress API */
public class WpApiClient {
    private final RequestSpecification spec;

    public WpApiClient() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(Config.getWpUser());
        authScheme.setPassword(Config.getWpPass());

        this.spec =
                new RequestSpecBuilder()
                        .setBaseUri(Config.getWpBaseUrl())
                        .setAuth(authScheme)
                        .setContentType(ContentType.JSON)
                        .build();
    }

    /** Создает пост */
    public Response createPost(PostRequest request) {
        return RestAssured.given(spec).body(request).post(Config.getWpPrefix() + "/posts");
    }

    /** Обновляет пост */
    public Response updatePost(int postId, PostRequest request) {
        return RestAssured.given(spec)
                .body(request)
                .post(Config.getWpPrefix() + "/posts/{id}", postId);
    }

    /** Удаляет пост */
    public Response deletePost(int postId, boolean force) {
        return RestAssured.given(spec)
                .queryParam("force", force)
                .delete(Config.getWpPrefix() + "/posts/{id}", postId);
    }
}
