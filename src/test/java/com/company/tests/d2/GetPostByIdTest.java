package com.company.tests.d2;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.db.PostRecord;

/** ТК10. Получение существующего поста по ID */
@Epic("WordPress DB")
@Feature("Получение данных")
@Story("Получение существующего поста по ID")
@Severity(SeverityLevel.CRITICAL)
public class GetPostByIdTest extends BaseTest {

    @Test(
            description = "ТК10. Получение существующего поста по ID",
            groups = {"wp", "positive", "posts", "d2"})
    public void testGetPostById() {
        int postId = createTestPost();

        wpApiClient.getPost(postId).then().statusCode(200).body("id", equalTo(postId));

        Allure.step(
                "Проверка наличия поста в БД",
                () -> {
                    Optional<PostRecord> dbPost = dbClient.getPostById(postId);
                    assertTrue(dbPost.isPresent(), "Пост с ID = " + postId + " не найден в БД");
                    assertEquals(dbPost.get().getPostTitle(), "Test Title");
                });
    }
}
