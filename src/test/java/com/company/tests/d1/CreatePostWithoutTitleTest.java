package com.company.tests.d1;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.db.PostRecord;
import com.company.models.wp.PostRequest;
import com.company.models.wp.PostResponse;

/** ТК04. Создание поста без title */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Создание поста без title")
@Severity(SeverityLevel.CRITICAL)
public class CreatePostWithoutTitleTest extends BaseTest {

    @Test(
            description = "ТК04. Создание поста без title",
            groups = {"wp", "negative", "posts", "d1"})
    public void testCreatePostWithoutTitle() {
        PostRequest request = PostRequest.builder().status("draft").content("No Title").build();

        PostResponse response =
                wpApiClient
                        .createPost(request)
                        .then()
                        .statusCode(201)
                        .body("title.raw", equalTo(""))
                        .body("content.raw", equalTo("No Title"))
                        .body("status", equalTo("draft"))
                        .extract()
                        .as(PostResponse.class);

        int postId = response.getId();
        postsToCleanup.get().add(postId);

        Allure.step(
                "Проверка данных в БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID " + postId + " не найден в БД");
                    assertEquals(dbPost.get().getPostTitle(), "", "Заголовок в БД не совпадает");
                });
    }
}
