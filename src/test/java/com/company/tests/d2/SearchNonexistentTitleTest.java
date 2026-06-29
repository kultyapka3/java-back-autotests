package com.company.tests.d2;

import static org.hamcrest.Matchers.hasSize;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК11. Поиск поста по несуществующему заголовку */
@Epic("WordPress DB")
@Feature("Получение данных")
@Story("Поиск поста по несуществующему заголовку")
@Severity(SeverityLevel.CRITICAL)
public class SearchNonexistentTitleTest extends BaseTest {

    @Test(
            description = "ТК11. Поиск поста по несуществующему заголовку",
            groups = {"wp", "negative", "posts", "d2"})
    public void testSearchNonexistentTitle() {
        String nonExistentTitle = "123NONEXISTENT_TITLE321";

        wpApiClient.searchPosts(nonExistentTitle).then().statusCode(200).body("$", hasSize(0));
    }
}
