package com.techroots.mbiz;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.google.common.collect.ImmutableMap;
import io.quarkus.amazon.lambda.runtime.AmazonLambdaApi;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class GreetingTest {
    @Test
    public void test() {
        APIGatewayV2HTTPEvent request = request("/hello");
        request.setHeaders(ImmutableMap.of("x-user", "test"));
        request.getRequestContext().setAuthorizer(new APIGatewayV2HTTPEvent.RequestContext.Authorizer());
//        request.getRequestContext().getAuthorizer().setLambda(new HashMap<>());
//        request.getRequestContext().getAuthorizer().getLambda().put("principalId", "Bill");

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(request)
                .when()
                .post(AmazonLambdaApi.API_BASE_PATH_TEST)
                .then()
                .statusCode(200)
                .body("body", equalTo("hello test"));
    }

    private APIGatewayV2HTTPEvent request(String path) {
        APIGatewayV2HTTPEvent request = new APIGatewayV2HTTPEvent();
        request.setRawPath(path);
        request.setRequestContext(new APIGatewayV2HTTPEvent.RequestContext());
        request.getRequestContext().setHttp(new APIGatewayV2HTTPEvent.RequestContext.Http());
        request.getRequestContext().getHttp().setMethod("GET");
        return request;
    }
}
