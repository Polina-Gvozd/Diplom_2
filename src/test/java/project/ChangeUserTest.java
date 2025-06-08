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


public class ChangeUserTest {
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
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        accessToken = loginResponse.path("accessToken");
    }

    @Test
    @DisplayName("Смена email")
    @Description("Позитивная проверка на смену email у авторизированного пользователя, ожидаем код 200")
    public void changeEmailTest() {
        userSteps.changeUser("update@hehe.ru", password, name, accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Смена password")
    @Description("Позитивная проверка на смену password у авторизированного пользователя, ожидаем код 200")
    public void changePasswordTest() {
        userSteps.changeUser(email, "update", name, accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Смена name")
    @Description("Позитивная проверка на смену name у авторизированного пользователя, ожидаем код 200")
    public void changeNameTest() {
        userSteps.changeUser(email, password, "update", accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Смена email без авторизации")
    @Description("Негативная проверка на смену email у не авторизированного пользователя, ожидаем код 401")
    public void changeEmailNotAuthorizedTest() {
        userSteps.changeUser("update@hehe.ru", password, name, "")
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Смена password без авторизации")
    @Description("Негативная проверка на смену password у не авторизированного пользователя, ожидаем код 401")
    public void changePasswordNotAuthorizedTest() {
        userSteps.changeUser(email, "update", name, "")
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Смена name без авторизации")
    @Description("Негативная проверка на смену name у не авторизированного пользователя, ожидаем код 401")
    public void changeNameNotAuthorizedTest() {
        userSteps.changeUser(email, password, "update", "")
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
