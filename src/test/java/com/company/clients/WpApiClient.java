package com.company.clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.Step;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.company.config.Config;
import com.company.models.wp.CommentRequest;
import com.company.models.wp.PostRequest;

/** Клиент для работы с WordPress API */
public class WpApiClient {
    private final RequestSpecification spec;

    public WpApiClient() {
        this.spec =
                new RequestSpecBuilder()
                        .setBaseUri(Config.getWpBaseUrl())
                        .setAuth(
                                new PreemptiveBasicAuthScheme() {
                                    {
                                        setUserName(Config.getWpUser());
                                        setPassword(Config.getWpPass());
                                    }
                                })
                        .setContentType(ContentType.JSON)
                        .addFilter(new AllureRestAssured())
                        .build();
    }

    // Posts
    /** Создает пост */
    @Step("Создание поста через API")
    public Response createPost(PostRequest request) {
        return RestAssured.given(spec).body(request).post(Config.getWpPrefix() + "/posts");
    }

    /** Обновляет пост */
    @Step("Обновление поста через API с ID = {postId}")
    public Response updatePost(int postId, PostRequest request) {
        return RestAssured.given(spec)
                .body(request)
                .post(Config.getWpPrefix() + "/posts/{id}", postId);
    }

    /** Удаляет пост */
    @Step("Удаление поста через API с ID = {postId} и force = {force}")
    public Response deletePost(int postId, boolean force) {
        return RestAssured.given(spec)
                .queryParam("force", force)
                .delete(Config.getWpPrefix() + "/posts/{id}", postId);
    }

    // Comments
    /** Создает комментарий */
    @Step("Создание комментария через API к посту с ID = {request.post}")
    public Response createComment(CommentRequest request) {
        return RestAssured.given(spec).body(request).post(Config.getWpPrefix() + "/comments");
    }

    /** Обновляет комментарий */
    @Step("Обновление комментария через API с ID = {commentId}")
    public Response updateComment(int commentId, CommentRequest request) {
        return RestAssured.given(spec)
                .body(request)
                .post(Config.getWpPrefix() + "/comments/{id}", commentId);
    }

    /** Удаляет комментарий */
    @Step("Удаление комментария через API с ID = {commentId} и force = {force}")
    public Response deleteComment(int commentId, boolean force) {
        return RestAssured.given(spec)
                .queryParam("force", force)
                .delete(Config.getWpPrefix() + "/comments/{id}", commentId);
    }
}
