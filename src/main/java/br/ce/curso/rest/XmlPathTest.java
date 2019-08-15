package br.ce.curso.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class XmlPathTest {

    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "http://restapi.wcaquino.me";

    }

    @Test
    public void exemplosXml(){

        RequestSpecBuilder reqSpec = new RequestSpecBuilder();
        reqSpec.log(LogDetail.ALL);
        RequestSpecification reqSpecification = reqSpec.build();

        given()
                .spec(reqSpecification)
                .when()
                    .get("/usersXML/3")
                .then()
                    .statusCode(200)
                    .body("user.name", is("Ana Julia"))
                    .body("user.@id", is("3"))
                    .body("user.filhos.name.size()", is(2))
                    .body("user.filhos.name[0]", is("Zezinho"))
                    .body("user.filhos.name", hasItem("Luizinho"))
                    .body("user.filhos.name", hasItems("Luizinho", "Zezinho"))
                ;
    }

    @Test public void outroModoXml(){
        given()
                    .when()
                        .get("/usersXML/3")
                    .then()
                        .statusCode(200)
                        .rootPath("user")
                        .body("name", is("Ana Julia"))
                .body("@id", is("3"))
                .body("filhos.name.size()", is(2))
                .body("filhos.name[0]", is("Zezinho"))
                .body("filhos.name", hasItem("Luizinho"))
                .body("filhos.name", hasItems("Luizinho", "Zezinho"))
        ;
    }

    @Test public void maisUmModoXml(){
        given()
                .when()
                .get("/usersXML/")
                .then()
                .statusCode(200)
                .body("users.user.size()",is(3))
                .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
                .body("users.user.@id", hasItems("1","2","3"))
                .body("users.user.find{it.age == 25}.name", is("Maria Joaquina") )
                .body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
                .body("users.user.salary.find{it != null}.toDouble()", is(1234.5678))
        ;
    }

    @Test public void pesquisaAvancadaXmlJava(){
        ArrayList<NodeImpl> names = given()
                .when()
                .get("/usersXML/")
                .then()
                .statusCode(200)
                .extract().path("users.user.name.findAll{it.toString().contains('n')}");

        Assert.assertEquals(2, names.size());

    }
}
