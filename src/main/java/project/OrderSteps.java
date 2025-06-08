package project;

import constants.Pens;
import dto.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


public class OrderSteps extends BaseApi {

    @Step("Вызов ручки на получение списка ингредиентов")
    public ValidatableResponse getIngredients(){
        return requestSpecification
                .get(Pens.INGREDIENTS)
                .then();
    }

    @Step("Вызов ручки на создание заказа")
    public ValidatableResponse createOrder(String[] ingredients, String accessToken){
        Order order = new Order();
        order.setIngredients(ingredients);
        return requestSpecification
                .headers("authorization", accessToken)
                .body(order)
                .when()
                .post(Pens.ORDER)
                .then();
    }

    @Step("Вызов ручки на получение заказов от конкретного пользователя")
    public ValidatableResponse getOrdersFromUser(String accessToken){
        return requestSpecification
                .headers("authorization", accessToken)
                .get(Pens.ORDER)
                .then();
    }
}
