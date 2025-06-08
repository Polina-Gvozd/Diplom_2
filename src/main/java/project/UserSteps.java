package project;

import constants.Pens;
import dto.User;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserSteps extends BaseApi{

    @Step("Вызов ручки на создание пользователя")
    public ValidatableResponse createUser(String email, String password, String name){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        return requestSpecification
                .body(user)
                .when()
                .post(Pens.CREATE_USER)
                .then();
    }

    @Step("Вызов ручки на авторизацию пользователя")
    public ValidatableResponse loginUser(String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        return requestSpecification
                .body(user)
                .when()
                .post(Pens.LOGIN_USER)
                .then();
    }

    @Step("Вызов ручки на изменение данных пользователя")
    public ValidatableResponse changeUser(String email, String password, String name, String accessToken){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        return requestSpecification
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Pens.CHANGE_USER)
                .then();
    }

    @Step("Вызов ручки на удаление пользователя")
    public void deleteUser(String accessToken){
        requestSpecification
                .headers("authorization", accessToken)
                .delete(Pens.DELETE_USER);
    }
}
