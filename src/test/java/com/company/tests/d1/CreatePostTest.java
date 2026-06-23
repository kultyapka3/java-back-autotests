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

/** ТК01. Создание поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Создание поста")
@Severity(SeverityLevel.CRITICAL)
public class CreatePostTest extends BaseTest {

    @Test(
            description = "ТК01. Создание поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testCreatePost() {
        PostRequest request =
                PostRequest.builder()
                        .title("TestPost1")
                        .status("draft")
                        .content("Test content")
                        .build();

        PostResponse response =
                wpApiClient
                        .createPost(request)
                        .then()
                        .statusCode(201)
                        .body("title.raw", equalTo("TestPost1"))
                        .body("content.raw", equalTo("Test content"))
                        .body("status", equalTo("draft"))
                        .extract()
                        .as(PostResponse.class);

        int postId = response.getId();
        postsToCleanup.get().add(postId);

        Allure.step(
                "Проверка содержимого БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID " + postId + " не найден в БД");
                    assertEquals(
                            dbPost.get().getPostTitle(),
                            "TestPost1",
                            "Заголовок в БД не совпадает");
                });
    }
}
