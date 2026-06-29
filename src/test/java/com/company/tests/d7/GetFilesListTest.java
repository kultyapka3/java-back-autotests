package com.company.tests.d7;

import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import com.company.base.BaseTest;
import com.company.models.yandex.ResourcesResponse;

/** ТК19. Получение списка файлов */
@Epic("Yandex Disk")
@Feature("Управление файлами")
@Story("Получение списка файлов")
@Severity(SeverityLevel.CRITICAL)
public class GetFilesListTest extends BaseTest {

    @Test(
            description = "ТК19. Получение списка файлов",
            groups = {"yandex", "positive", "d7"})
    public void testGetFilesList() throws Exception {
        String filesFolder = uploadedFilesPath();

        String rawResponse =
                ydApiClient.getItems(filesFolder).then().statusCode(200).extract().asString();

        ObjectMapper mapper = new ObjectMapper();
        ResourcesResponse uploadedFiles = mapper.readValue(rawResponse, ResourcesResponse.class);

        Set<String> fileNames =
                uploadedFiles.get_embedded().getItems().stream()
                        .map(ResourcesResponse.Embedded.Items::getName)
                        .collect(Collectors.toSet());

        Set<String> expectedFileNames = Set.of("testData1.txt", "testData2.txt", "testData3.txt");

        Allure.step(
                "Проверка содержит ли ответ созданные файлы: (" + fileNames + ")",
                () ->
                        assertTrue(
                                fileNames.containsAll(expectedFileNames),
                                "Ответ ("
                                        + fileNames
                                        + ") не содержит созданные файлы ("
                                        + expectedFileNames
                                        + ")"));

        Allure.step(
                "Проверка соответствия ответа схеме",
                () -> {
                    try {
                        JsonSchemaFactory factory =
                                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

                        InputStream schemaStream =
                                getClass()
                                        .getClassLoader()
                                        .getResourceAsStream(
                                                "schemas/yandex-disk-resources-schema.json");

                        JsonNode schemaNode = mapper.readTree(schemaStream);
                        JsonNode responseNode = mapper.readTree(rawResponse);
                        factory.getSchema(schemaNode).validate(responseNode);
                    } catch (Exception e) {
                        Assert.fail("Ошибка при валидации схемы: " + e.getMessage());
                    }
                });
    }
}
