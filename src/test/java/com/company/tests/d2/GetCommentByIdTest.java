package com.company.tests.d2;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.config.Config;
import com.company.db.CommentRecord;

/** ТК12. Получение существующего комментария по ID */
@Epic("WordPress DB")
@Feature("Получение данных")
@Story("Получение существующего комментария по ID")
@Severity(SeverityLevel.CRITICAL)
public class GetCommentByIdTest extends BaseTest {

    @Test(
            description = "ТК12. Получение существующего комментария по ID",
            groups = {"wp", "positive", "comments", "d2"})
    public void testGetCommentById() {
        int commentId = createTestComment();

        wpApiClient.getComment(commentId).then().statusCode(200).body("id", equalTo(commentId));

        Allure.step(
                "Проверка наличия комментария в БД",
                () -> {
                    Optional<CommentRecord> dbComment = dbClient.getCommentById(commentId);
                    assertTrue(
                            dbComment.isPresent(),
                            "Комментарий с ID " + commentId + " не найден в БД");
                    assertEquals(
                            dbComment.get().getCommentAuthor(),
                            Config.getWpUser(),
                            "Автор комментария в БД не совпадает");
                });
    }
}
