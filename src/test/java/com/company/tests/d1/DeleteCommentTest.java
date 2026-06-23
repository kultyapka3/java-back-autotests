package com.company.tests.d1;

import static org.testng.Assert.assertEquals;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК09. Удаление существующего комментария */
@Epic("WordPress DB")
@Feature("Управление комментариями")
@Story("Удаление существующего комментария")
@Severity(SeverityLevel.CRITICAL)
public class DeleteCommentTest extends BaseTest {

    @Test(
            description = "ТК09. Удаление существующего комментария",
            groups = {"wp", "positive", "comments", "d1"})
    public void testDeleteComment() {
        int commentId = createTestComment();

        wpApiClient.deleteComment(commentId, true).then().statusCode(200);

        Allure.step(
                "Проверка, что комментарий был удален в БД",
                () -> {
                    int count = dbClient.getCountCommentsById(commentId);
                    assertEquals(count, 0, "Комментарий с ID = " + commentId + " не был удален");
                });

        // Убираем удаленный комментарий из списка очистки
        commentsToCleanup.get().remove(Integer.valueOf(commentId));
    }
}
