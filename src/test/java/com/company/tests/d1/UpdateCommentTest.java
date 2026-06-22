package com.company.tests.d1;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.db.CommentRecord;
import com.company.models.wp.CommentRequest;
import com.company.models.wp.CommentResponse;

/** ТК08. Изменение существующего комментария */
@Epic("WordPress DB")
@Feature("Управление комментариями")
@Story("Изменение существующего комментария")
@Severity(SeverityLevel.CRITICAL)
public class UpdateCommentTest extends BaseTest {

    @Test(
            description = "ТК08. Изменение существующего комментария",
            groups = {"wp", "positive", "comments", "d1"})
    public void testUpdateComment() {
        int commentId = createTestComment();

        CommentRequest updateRequest =
                CommentRequest.builder()
                        .author_name("Test User")
                        .content("Updated comment")
                        .build();

        wpApiClient
                .updateComment(commentId, updateRequest)
                .then()
                .statusCode(200)
                .body("author_name", equalTo("Test User"))
                .body("content.raw", equalTo("Updated comment"))
                .extract()
                .as(CommentResponse.class);

        Allure.step(
                "Проверка обновленного комментария в БД",
                () -> {
                    Optional<CommentRecord> dbComment = dbClient.getCommentById(commentId);
                    assertTrue(
                            dbComment.isPresent(),
                            "Комментарий с ID " + commentId + " не найден в БД");
                    assertEquals(
                            dbComment.get().getCommentAuthor(),
                            "Test User",
                            "Автор комментария в БД не совпадает");
                    assertEquals(
                            dbComment.get().getCommentContent(),
                            "Updated comment",
                            "Содержание комментария в БД не совпадает");
                });
    }
}
