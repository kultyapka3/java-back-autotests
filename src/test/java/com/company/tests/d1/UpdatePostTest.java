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

/** ТК02. Обновление поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Обновление поста")
@Severity(SeverityLevel.CRITICAL)
public class UpdatePostTest extends BaseTest {

    @Test(
            description = "ТК02. Обновление поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testUpdatePost() {
        int postId = createTestPost();

        PostRequest updateRequest =
                PostRequest.builder().title("Updated Title").content("Updated Content").build();

        wpApiClient
                .updatePost(postId, updateRequest)
                .then()
                .statusCode(200)
                .body("title.raw", equalTo("Updated Title"))
                .body("content.raw", equalTo("Updated Content"))
                .extract()
                .as(PostResponse.class);

        Allure.step(
                "Проверка обновленного поста в БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID " + postId + " не найден в БД");
                    assertEquals(
                            dbPost.get().getPostTitle(),
                            "Updated Title",
                            "Пост с ID " + postId + " не был обновлен");
                });
    }
}
