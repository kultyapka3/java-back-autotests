package com.company.tests.d1;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.config.Config;
import com.company.db.CommentRecord;
import com.company.models.wp.CommentRequest;
import com.company.models.wp.CommentResponse;

/** ТК07. Создание комментария к существующему посту */
@Epic("WordPress DB")
@Feature("Управление комментариями")
@Story("Создание комментария к существующему посту")
@Severity(SeverityLevel.CRITICAL)
public class CreateCommentTest extends BaseTest {

    @Test(
            description = "ТК07. Создание комментария к существующему посту",
            groups = {"wp", "positive", "comments", "d1"})
    public void testCreateComment() {
        int postId = createTestPost();

        CommentRequest request =
                CommentRequest.builder()
                        .post(postId)
                        .author_name(Config.getWpUser())
                        .author_email(Config.getWpEmail())
                        .content("Test comment")
                        .status("approved")
                        .build();

        CommentResponse response =
                wpApiClient
                        .createComment(request)
                        .then()
                        .statusCode(201)
                        .body("author_name", equalTo(Config.getWpUser()))
                        .body("author_email", equalTo(Config.getWpEmail()))
                        .body("content.raw", equalTo("Test comment"))
                        .body("status", equalTo("approved"))
                        .extract()
                        .as(CommentResponse.class);

        int commentId = response.getId();
        commentsToCleanup.get().add(commentId);

        Allure.step(
                "Проверка содержимого БД",
                () -> {
                    Optional<CommentRecord> dbComment = dbClient.getCommentById(commentId);
                    assertTrue(
                            dbComment.isPresent(),
                            "Комментарий с ID " + commentId + " не найден в БД");
                    assertEquals(
                            dbComment.get().getCommentAuthor(),
                            Config.getWpUser(),
                            "Автор комментария в БД не совпадает");
                    assertEquals(
                            dbComment.get().getCommentContent(),
                            "Test comment",
                            "Содержание комментария в БД не совпадает");
                });
    }
}
