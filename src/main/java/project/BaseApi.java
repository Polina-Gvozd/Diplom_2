package project;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseApi {
    RequestSpecification requestSpecification = given()
            .baseUri("https://stellarburgers.nomoreparties.site")
            .contentType(ContentType.JSON);
}
