package com.company.tests.d3;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import com.company.base.BaseTest;
import com.company.config.Config;

/** ТК1. Авторизация с валидным токеном */
@Epic("Yandex Disk")
@Feature("Авторизация")
@Story("Авторизация с валидным токеном")
@Severity(SeverityLevel.CRITICAL)
public class YandexAuthTest extends BaseTest {

    @Test(
            description = "ТК1. Авторизация с валидным токеном",
            groups = {"yandex", "positive", "d3"})
    public void testYandexAuth() {
        ydApiClient
                .getDiskInfo()
                .then()
                .statusCode(200)
                .body("user", notNullValue())
                .body("user.login", equalTo(Config.getYandexDiskLogin()))
                .body("user.display_name", equalTo(Config.getYandexDiskDisplayName()));
    }
}
