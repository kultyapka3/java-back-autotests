package com.company.tests.d3;

import static org.hamcrest.Matchers.notNullValue;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;

/** ТК2. Попытка авторизации без токена */
@Epic("Yandex Disk")
@Feature("Авторизация")
@Story("Попытка авторизации без токена")
@Severity(SeverityLevel.NORMAL)
public class YandexAuthWithoutTokenTest extends BaseTest {

    @Test(
            description = "ТК2. Попытка авторизации без токена",
            groups = {"yandex", "negative", "d3"})
    public void testYandexAuthWithoutToken() {
        ydApiClient
                .getDiskInfoUnauthorized()
                .then()
                .statusCode(401)
                .body("error", notNullValue())
                .body("description", notNullValue())
                .body("message", notNullValue());
    }
}
