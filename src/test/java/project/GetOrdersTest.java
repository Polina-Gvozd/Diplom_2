package project;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class GetOrdersTest {
    String accessToken;
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();

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
    @DisplayName("Получение списка заказов")
    @Description("Позитивная проверка на получение списка заказов у конкретного пользователя, ожидаем код 200")
    public void getOrderTest() {
        orderSteps.getOrdersFromUser(accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Негативная проверка на получение списка заказов у конкретного пользователя без авторизации, ожидаем код 401")
    public void ordersWithoutAuthorizationTest() {
        orderSteps.getOrdersFromUser("")
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
