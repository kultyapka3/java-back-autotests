package com.company.base;

import java.util.ArrayList;
import java.util.List;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import com.company.clients.DbClient;
import com.company.clients.WpApiClient;

/** Базовый класс для всех тестов */
@Listeners(AllureTestNg.class)
public class BaseTest {

    protected WpApiClient wpApiClient;
    protected DbClient dbClient;

    /** Список ID постов для очистки после каждого теста */
    protected final ThreadLocal<List<Integer>> postsToCleanup =
            ThreadLocal.withInitial(ArrayList::new);

    @BeforeMethod
    public void setUp() {
        wpApiClient = new WpApiClient();
        dbClient = new DbClient();
        postsToCleanup.get().clear();
    }

    /** Создание тестового поста */
    protected int createTestPost() {
        int postId = dbClient.createTestPost("Test Title", "Test Content", "publish");
        postsToCleanup.get().add(postId);

        return postId;
    }

    @AfterMethod
    public void tearDown() {
        for (Integer postId : postsToCleanup.get()) {
            try {
                dbClient.deletePostById(postId);
            } catch (Exception e) {
                System.err.println(
                        "Не удалось удалить пост ID = " + postId + ". Ошибка: " + e.getMessage());
            }
        }
        postsToCleanup.remove();
    }
}
