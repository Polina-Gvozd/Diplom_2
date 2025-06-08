package project;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    String accessToken;
    UserSteps userSteps = new UserSteps();

    String email;
    String password;
    String name;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphabetic(6)+"@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, password, name);
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Позитивная проверка на авторизацию пользователя с вводом валидных данных, ожидаем код 200")
    public void loginOkTest(){
        userSteps.loginUser(email, password)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    @Description("Негативная проверка на авторизацию пользователя с неверным email, ожидаем код 401")
    public void wrongEmailTest(){
        userSteps.loginUser("wrong@hehe.ru", password)
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным password")
    @Description("Негативная проверка на авторизацию пользователя с неверным password, ожидаем код 401")
    public void wrongPasswordTest(){
        userSteps.loginUser(email, "wrong")
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
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
