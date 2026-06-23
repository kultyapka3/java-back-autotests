package com.company.tests.d1;

import static org.testng.Assert.assertEquals;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК03. Удаление поста */
@Epic("WordPress DB")
@Feature("Управление постами")
@Story("Удаление поста")
@Severity(SeverityLevel.CRITICAL)
public class DeletePostTest extends BaseTest {

    @Test(
            description = "ТК03. Удаление поста",
            groups = {"wp", "positive", "posts", "d1"})
    public void testDeletePost() {
        int postId = createTestPost();

        wpApiClient.deletePost(postId, true).then().statusCode(200);

        Allure.step(
                "Проверка, что пост был удален в БД",
                () -> {
                    int count = dbClient.getCountPostsById(postId);
                    assertEquals(count, 0, "Пост с ID = " + postId + " не был удален");
                });

        // Убираем удаленный пост из списка очистки
        postsToCleanup.get().remove(Integer.valueOf(postId));
    }
}
