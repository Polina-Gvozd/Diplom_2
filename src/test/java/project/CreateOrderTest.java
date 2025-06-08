package project;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    String accessToken;
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();

    String email;
    String password;
    String name;
    String[] ingredients;

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
    @DisplayName("Создание заказа")
    @Description("Позитивная проверка на создание заказа у авторизированного пользователя с валидными данными, ожидаем код 200")
    public void createOrderTest() {
        List<String> id = orderSteps.getIngredients().extract().path("data._id");
        ingredients = new String[]{id.get(0), id.get(1)};
        orderSteps.createOrder(ingredients, accessToken)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Позитивная проверка на создание заказа без авторизации с валидными данными, ожидаем код 200")
    public void orderWithoutAuthorizationTest() {
        List<String> id = orderSteps.getIngredients().extract().path("data._id");
        ingredients = new String[]{id.get(0), id.get(1)};
        orderSteps.createOrder(ingredients, "")
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Негативная проверка на создание заказа без ингредиентов, ожидаем код 400")
    public void orderWithoutIngredientsTest() {
        orderSteps.createOrder(null, accessToken)
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиентов")
    @Description("Негативная проверка на создание заказа с невалидным хешем ингредиентов, ожидаем код 500")
    public void orderWithWrongIngredientsTest() {
        ingredients = new String[]{"12345"};
        orderSteps.createOrder(ingredients, accessToken)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}
