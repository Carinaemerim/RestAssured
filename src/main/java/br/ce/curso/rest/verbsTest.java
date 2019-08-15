package br.ce.curso.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class verbsTest {

    @Test
    public void saveUserTest(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Carina Leal\",\"age\": 29,\"salary\": 2.805}\n")
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Carina Leal"))
            .body("age", is(29))
        ;
    }

    @Test
    public void validateSaveUserNameTest(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"age\": 29,\"salary\": 2.805}\n")
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;

    }

    @Test
    public void saveUserXmlTest(){
        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Melissa Emerim</name><age>3</age></user>")
        .when()
                .post("http://restapi.wcaquino.me/usersXML")
        .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Melissa Emerim"))
                .body("user.age", is("3"))
        ;
    }

    @Test
    public void alterUserTest(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Carina Leal\",\"age\": 29}")
        .when()
                .put("http://restapi.wcaquino.me/users/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Carina Leal"))
                .body("age", is(29))
        ;
    }

    @Test
    public void customUrlTest(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Carina Leal\",\"age\": 29}")
        .when()
                .put("http://restapi.wcaquino.me/{entidade}/{user_id}", "users", "1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Carina Leal"))
                .body("age", is(29))
        ;
    }

    @Test
    public void anotherCustomUrlTest(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Carina Leal\",\"age\": 29}")
                .pathParam("entidade","users")
                .pathParam("user_id", "1")
        .when()
                .put("http://restapi.wcaquino.me/{entidade}/{user_id}")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Carina Leal"))
                .body("age", is(29))
        ;
    }

    @Test
    public void deleteUserTest(){
        given()
                .log().all()
                .pathParam("entidade","users")
                .pathParam("user_id", "1")
        .when()
                .delete("http://restapi.wcaquino.me/{entidade}/{user_id}")
        .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void deleteInvalidUserTest(){
        given()
                .log().all()
                .pathParam("entidade","users")
                .pathParam("user_id", "5")
        .when()
                .delete("http://restapi.wcaquino.me/{entidade}/{user_id}")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }

    @Test
    public void saveUserWithMapTest(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "User via map");
        params.put("age", 36);

        given()
                .log().all()
                .contentType("application/json")
                .body(params)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("User via map"))
                .body("age", is(36))
        ;
    }

    @Test
    public void saveUserWithObjectTest(){
        User user = new User("User via object", 25);

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("User via object"))
                .body("age", is(25))
        ;
    }

    @Test
    public void saveDeserializeUserWithObjectTest(){
        User user = new User("Usuario desserializado", 25);

        User usuarioInserido = given()
                .log().all()
                .contentType("application/json")
                .body(user)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
                ;
        System.out.println(usuarioInserido);
        Assert.assertEquals("Usuario desserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(25));
        Assert.assertThat(usuarioInserido.getId(),notNullValue());
    }



}

//https://github.com/Carinaemerim/RestAssured.git
