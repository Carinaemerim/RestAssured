package br.ce.curso.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class UserJsonTest {

    @Test
    public void verificaPrimeiroNivelTest(){

        given()
                .when()
                .get("http://restapi.wcaquino.me/users/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name",containsString("Silva"))
                .body("age", greaterThan(18));
    }

    @Test
    public void verificaPrimeiroNivelAgainTest(){

        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");

        Assert.assertEquals(new Integer(1), response.path("id"));

        //jsonpath
        JsonPath jpath = new JsonPath(response.asString());
        Assert.assertEquals(1, jpath.getInt("id"));

        //From
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }

    @Test
    public void verificaSegundoNivelTest(){

        //navegar entre niveis de json
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name",containsString("Joaquina"))
                .body("age", greaterThan(18))
                .body("endereco.rua", is("Rua dos bobos"))
                ;
    }

    @Test
    public void verificaListaTest(){

        given()
                .when()
                .get("http://restapi.wcaquino.me/users/3")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("name",containsString("Ana"))
                .body("age", greaterThan(18))
                .body("filhos",hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))

        ;
    }

    @Test
    public void deveRetornarErroTest(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/4")
                .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"))

        ;
    }

    @Test
    public void verificaListaRaizTest(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("name", hasItems("João da Silva","Maria Joaquina", "Ana Júlia"))
                .body("age[1]", is(25))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
                .body("salary", contains(1234.5677f, 2500, null))
                ;
    }

    @Test
    public void verificacaoAvancadaTest(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/")
                .then()
                .statusCode(200)
                .body("", hasSize(3))
                .body("age.findAll{it <= 25}.size()", is(2) )
                .body("age.findAll{it <= 25 && it > 20}.size()", is(1) )
                .body("findAll{it.age <= 25 && it.age > 20}.name", contains("Maria Joaquina") )
                .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia") )
                .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
                .body("salary.findAll{it != null}sum()", allOf(greaterThan(3000d), lessThan(5000d)))
                ;
    }

    
    @Ignore
    public void JsonpathComJava(){
        ArrayList<String> names =
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/")
                .then()
                .statusCode(200)
                .extract().path("name.findAll(it.startsWith{'Maria'}")
                ;

        Assert.assertEquals(1,names.size());
        Assert.assertTrue(names.get(0).equalsIgnoreCase("mAria Joaquina"));
        Assert.assertEquals(names.get(0).toUpperCase(),"maria joaquina".toUpperCase());
    }


}
