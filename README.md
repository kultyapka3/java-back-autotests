## Тесты

Этот репозиторий содержит проект Java автотестов для WordPress DB & REST API

---

## Технологии

* **Язык программирования:** Java 17+
* **Тестовый фреймворк:** TestNG
* **Сборка проекта и зависимостей:** Maven
* **API:** RestAssured
* **Работа с БД:** JDBC

---

## Структура проекта

```
project_root/
├── README.md                                        # Этот файл
├── .gitignore                                       # Файл управления Git
└── src/                                             
...
```

---

## Установка и запуск

### Клонирование репозитория

1. Сначала клонируйте репозиторий:
   ```bash
   git clone https://github.com/kultyapka3/java-back-autotests.git
   ```

2. Перейдите в нужную директорию:
   ```bash
   cd java-back-autotests
   ```

### Сборка проекта

Для загрузки зависимостей и компиляции выполните:

```bash
mvn clean compile
```

---

## Запуск тестов

...

---

# WordPress Suite

## D1 Тест-кейсы

### Тест-кейс №01. Создание поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts`
        * В теле запроса передать: `{"title": "TestPost1", "status": "draft", "content": "Test content"}`
    2. Зафиксировать `id` поста из ответа

- **Ожидаемый результат**:
    1. Код ответа `201 Created`
    2. Тело ответа содержит переданные параметры
    3. Запрос `SELECT post_title, post_content FROM wp_posts WHERE ID = {id}` находит строку с `post_title = TestPost1`

- **Постусловие**:
    1. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {id} OR post_parent = %s`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `title` — `TestPost1`
    3. `status` — `draft`
    4. `content` — `Test content`

### Тест-кейс №02. Обновление поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
  ('Test Title', 'Test Content', 'publish')`
    3. Зафиксировать `id` поста

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts/{id}`
        * В теле запроса передать: `{"title": "Updated Title", "content": "Updated Content"}`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит переданные параметры
    3. Запрос `SELECT post_title, post_content FROM wp_posts WHERE ID = {id}` находит строку с
       `post_title = Updated Title`

- **Постусловие**:
    1. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {id} OR post_parent = {id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `title` — `Updated Title`
    3. `content` — `Updated Content`

### Тест-кейс №03. Удаление поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
  ('Test Title', 'Test Content', 'publish')`
    3. Зафиксировать `id` поста

- **Шаги**:
    1. Отправить DELETE-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts/{id}&force=true`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Запрос `SELECT COUNT(*) FROM wp_posts WHERE ID = {id}` возвращает `0`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `force` — `True`

### Тест-кейс №04. Создание поста без title

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts`
        * В теле запроса передать: `{"status": "draft", "content": "No Title"}`

- **Ожидаемый результат**:
    1. Код ответа `201 Created`
    2. Тело ответа содержит переданные параметры
    3. Запрос `SELECT post_title, post_content FROM wp_posts WHERE ID = {id}` находит строку с пустым `post_title`

- **Постусловие**:
    1. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {id} OR post_parent = {id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `status` — `draft`
    3. `content` — `No Title`

### Тест-кейс №05. Обновление несуществующего поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts/9999`
        * В теле запроса передать: `{"title": "New Title"}`

- **Ожидаемый результат**:
    1. Код ответа `404 Not Found`
    2. Тело ответа содержит `code` или `message`
    3. Запрос `SELECT COUNT(*) FROM wp_posts WHERE ID = 9999` возвращает `0`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `title` — `New Title`

### Тест-кейс №06. Удаление несуществующего поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить DELETE-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts/9999&force=true`

- **Ожидаемый результат**:
    1. Код ответа `404 Not Found`
    2. Тело ответа содержит `code` или `message`
    3. Запрос `SELECT COUNT(*) FROM wp_posts WHERE ID = 9999` возвращает `0`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `force` — `True`

### Тест-кейс №07. Создание комментария к существующему посту

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
    ('Test Title', 'Test Content', 'publish')`
    3. Зафиксировать `id` поста

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/comments`
        * В теле запроса передать: `{"post": {post_id}, "author_name": "Firstname.Lastname", 
        "author_email": "Firstname.Lastname@simbirsoft.com", "content": "Test comment", "status": "approved"}`
    2. Зафиксировать `id` комментария из ответа

- **Ожидаемый результат**:
    1. Код ответа `201 Created`
    2. Тело ответа содержит переданные параметры
    3. Запрос `SELECT comment_author, comment_content FROM wp_comments WHERE comment_ID = {id}` находит строку с
       `comment_author = Firstname.Lastname`

- **Постусловие**:
    1. Удалить созданный комментарий: `DELETE FROM wp_comments WHERE comment_ID = {id}`
    2. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {post_id} OR post_parent = {post_id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `author_name` — `Firstname.Lastname`
    3. `author_email` — `Firstname.Lastname@simbirsoft.com`
    4. `content` — `Test Comment`
    5. `status` — `approved`

### Тест-кейс №08. Изменение существующего комментария

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
    ('Test Title', 'Test Content', 'publish')`
    3. Создать тестовый комментарий: `INSERT INTO wp_comments (comment_post_ID, comment_author, comment_author_email, 
    comment_content, comment_approved) VALUES ({post_id}, 'Firstname.Lastname', 'Firstname.Lastname@simbirsoft.com', 
    'Test comment content', '1')`
    4. Зафиксировать `id` комментария

- **Шаги**:
    1. Отправить POST-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/comments/{id}`
        * В теле запроса передать: `{"content": "Updated comment", "author_name": "Test User"}`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит переданные параметры
    3. Запрос `SELECT comment_author, comment_content FROM wp_comments WHERE comment_ID = {id}` находит строку с
       `comment_content = Updated comment`

- **Постусловие**:
    1. Удалить созданный комментарий: `DELETE FROM wp_comments WHERE comment_ID = {id}`
    2. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {post_id} OR post_parent = {post_id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `content` — `Updated comment`
    3. `author_name` — `Test user`

### Тест-кейс №09. Удаление существующего комментария

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
    ('Test Title', 'Test Content', 'publish')`
    3. Создать тестовый комментарий: `INSERT INTO wp_comments (comment_post_ID, comment_author, comment_author_email, 
    comment_content, comment_approved) VALUES ({post_id}, 'Firstname.Lastname', 'Firstname.Lastname@simbirsoft.com', 
    'Test comment content', '1')`
    4. Зафиксировать `id` комментария

- **Шаги**:
    1. Отправить DELETE-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/comments/{id}&force=true`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Запрос `SELECT COUNT(*) FROM wp_comments WHERE comment_ID = {id}` возвращает `0`

- **Постусловие**:
    1. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {post_id} OR post_parent = {post_id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `force` — `true`

## D2 Тест-кейсы

### Тест-кейс №10. Получение существующего поста по ID

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
    ('Test Title', 'Test Content', 'publish')`
    3. Зафиксировать `id` поста

- **Шаги**:
    1. Отправить GET-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts/{id}`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит зафиксированный `id`
    3. Запрос `SELECT post_title, post_content FROM wp_posts WHERE ID = {id}` находит строку с
       `post_title = Test Title`

- **Постусловие**:
    1. Удалить созданный пост: `DELETE FROM wp_posts WHERE ID = {id} OR post_parent = {id}`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`

### Тест-кейс №11. Поиск поста по несуществующему заголовку

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить GET-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/posts&search=123NONEXISTENT_TITLE321`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит пустой массив `[]`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `search` — `123NONEXISTENT_TITLE321`

### Тест-кейс №12. Получение существующего комментария по ID

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)
    2. Создать тестовый пост: `INSERT INTO wp_posts (post_title, post_content, post_status) VALUES 
    ('Test Title', 'Test Content', 'publish')`
    3. Создать тестовый комментарий: `INSERT INTO wp_comments (comment_post_ID, comment_author, comment_author_email, 
    comment_content, comment_approved) VALUES ({post_id}, 'Firstname.Lastname', 'Firstname.Lastname@simbirsoft.com', 
    'Test comment content', '1')`
    4. Зафиксировать `id` комментария

- **Шаги**:
    1. Отправить GET-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/comments/{id}`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит зафиксированный `id`
    3. Запрос `SELECT comment_author, comment_content FROM wp_comments WHERE comment_ID = {id}` находит строку с
       `comment_author = Firstname.Lastname`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`

### Тест-кейс №13. Получение комментариев несуществующего поста

- **Предусловие**:
    1. Авторизоваться (`Firstname.Lastname:123-Test`)

- **Шаги**:
    1. Отправить GET-запрос:
        * На `http://localhost:8000/index.php?rest_route=/wp/v2/comments&post=9999`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит пустой массив `[]`

- **Тестовые данные**:
    1. Basic Auth `USERNAME:PASSWORD` — `Firstname.Lastname:123-Test`
    2. `post` — `9999`

## D4 Тест-кейсы

### Тест-кейс №14. Создание папки

- **Предусловие**:
    1. Авторизоваться (`valid_token`)

- **Шаги**:
    1. Отправить PUT-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/resources`
        * В параметрах запроса передать: `path=FolderForTest`

- **Ожидаемый результат**:
    1. Код ответа `201 Created`
    2. Тело ответа содержит `href` с параметром `path=FolderForTest`

- **Постусловие**:
    1. Удалить созданную папку:
       `DELETE https://cloud-api.yandex.net/v1/disk/resources?path=FolderForTest&permanently=true`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `FolderForTest`

### Тест-кейс №15. Удаление папки (перемещение в корзину)

- **Предусловие**:
    1. Авторизоваться (`valid_token`)
    2. Создать тестовую папку: `PUT https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder`

- **Шаги**:
    1. Отправить DELETE-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/resources`
        * В параметрах запроса передать: `path=TestFolder`

- **Ожидаемый результат**:
    1. Код ответа `204 No Content`
    2. Тело ответа отсутствует (`пустой ответ`)

- **Постусловие**:
    1. Удалить папку в корзине: `DELETE https://cloud-api.yandex.net/v1/disk/trash/resources?path=TestFolder`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `TestFolder`

### Тест-кейс №16. Восстановление папки

- **Предусловие**:
    1. Авторизоваться (`valid_token`)
    2. Создать тестовую папку: `PUT https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder`
    3. Переместить тестовую папку в корзину: `DELETE https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder`
    4. Зафиксировать `path` элемента с текстом `TestFolder`: `GET https://cloud-api.yandex.net/v1/disk/trash/resources`

- **Шаги**:
    1. Отправить PUT-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/trash/resources/restore`
        * В параметрах запроса передать зафиксированный `path`

- **Ожидаемый результат**:
    1. Код ответа `201 Created`
    2. Тело ответа содержит `href` с параметром `path=TestFolder`

- **Постусловие**:
    1. Удалить восстановленную папку:
       `DELETE https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder&permanently=true`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `TestFolder`

### Тест-кейс №17. Создание уже существующей папки

- **Предусловие**:
    1. Авторизоваться (`valid_token`)
    2. Создать тестовую папку: `PUT https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder`

- **Шаги**:
    1. Отправить PUT-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/resources`
        * В параметрах запроса передать: `path=TestFolder`

- **Ожидаемый результат**:
    1. Код ответа `409 Conflict`
    2. Тело ответа содержит `error` и `message`

- **Постусловие**:
    1. Удалить созданную папку: `DELETE https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder&permanently=true`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `TestFolder`

### Тест-кейс №18. Удаление несуществующей папки

- **Предусловие**:
    1. Авторизоваться (`valid_token`)

- **Шаги**:
    1. Отправить DELETE-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/resources`
        * В параметрах запроса передать: `path=123NONEXISTENT_FOLDER321`

- **Ожидаемый результат**:
    1. Код ответа `404 Not Found`
    2. Тело ответа содержит `error` и `message`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `123NONEXISTENT_FOLDER321`

## D7 Тест-кейс

### Тест-кейс №19. Получение списка файлов

- **Предусловие**:
    1. Авторизоваться (`valid_token`)
    2. Создать тестовую папку: `PUT https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder`
    3. Создать тестовые файлы:
        * Сгенерировать тестовые файлы: `testData1.txt`, `testData2.txt`, `testData3.txt` с содержанием `Test data 1`,
          `Test data 2`, `Test data 3` соответственно
        * Получить ссылку для загрузки (повторить для каждого файла):
          `GET https://cloud-api.yandex.net/v1/disk/resources/upload?path=TestFolder`
        * Загрузить тестовый файл (повторить для каждого файла):
          `PUT https://cloud-api.yandex.net/v1/disk/resources/upload?path=TestFolder&url=...`

- **Шаги**:
    1. Отправить GET-запрос:
        * На `https://cloud-api.yandex.net/v1/disk/resources`
        * В параметрах запроса передать: `path=TestFolder`

- **Ожидаемый результат**:
    1. Код ответа `200 OK`
    2. Тело ответа содержит имена созданных файлов
    3. Тело ответа строго соответствует `JSON Schema`

- **Постусловие**:
    1. Удалить созданную папку:
       `DELETE https://cloud-api.yandex.net/v1/disk/resources?path=TestFolder&permanently=true`

- **Тестовые данные**:
    1. OAuth `valid_token`
    2. `path` — `TestFolder`
    3. `name` — `testData1.txt`, `testData2.txt`, `testData3.txt`
    4. `content` — `Test data 1`, `Test data 2`, `Test data 3`
