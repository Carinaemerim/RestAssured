package br.ce.curso.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class OlaMundoTest {

    @Test
    public void testOlaMundo(){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validator = response.then();
        validator.statusCode(200);
    }

    @Test
    public void testOutroModoRestAssured(){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validator = response.then();
        validator.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);
    }

    @Test
    public void testMaisUmModoRestAssured(){
        given()
                //pré condições
        .when()
                //ação de fato
            .get("http://restapi.wcaquino.me/ola")
        .then()
                //Assertivas
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void validarBody(){
        given()
                //pré condições
                .when()
                //ação de fato
                .get("http://restapi.wcaquino.me/ola")
                .then()
                //Assertivas
                .statusCode(200)
                .body(Matchers.is("Ola Mundo!"))
                .body(Matchers.containsString("Mundo"));
    }

    @Test
    public void validaJson(){

    }

}
