package project;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    String accessToken;
    UserSteps userSteps = new UserSteps();

    String email;
    String password;
    String name;

    @Test
    @DisplayName("Создание пользователя")
    @Description("Позитивная проверка на создание пользователя с вводом валидных данных, ожидаем код 200")
    public void createOkTest(){
        email = RandomStringUtils.randomAlphabetic(6)+"@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, password, name)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Негативная проверка на создание двух одинаковых пользователей, ожидаем код 403")
    public void alreadyExistTest(){
        email = RandomStringUtils.randomAlphabetic(6)+"@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, password, name);
        userSteps.createUser(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Негативная проверка на создание пользователя без email, ожидаем код 403")
    public void withoutEmailTest(){
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(null, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Негативная проверка на создание пользователя без password, ожидаем код 403")
    public void withoutPasswordTest(){
        email = RandomStringUtils.randomAlphabetic(6)+"@yandex.ru";
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, null, name)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Негативная проверка на создание пользователя без name, ожидаем код 403")
    public void withoutNameTest(){
        email = RandomStringUtils.randomAlphabetic(6)+"@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, password, null)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        if (loginResponse.getStatusCode() == SC_OK) {
            accessToken = loginResponse.path("accessToken");
            userSteps.deleteUser(accessToken);
        }
    }
}
