package com.mycompany.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

public class PetTypesDefinitions {

    private EnvironmentVariables environmentVariables;
    private String BASE_PATH;

    @Before
    public void setUp() {
        environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        BASE_PATH = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getOptionalProperty("base.uri").orElse("https://reqres.in/api");
        RestAssured.baseURI = BASE_PATH;
    }

    @Given("el cliente configura la URI base")
    public void el_cliente_configura_la_uri_base() {
        given().baseUri(BASE_PATH);
    }

    @When("el cliente realiza una peticion GET a {string}")
    public void el_cliente_realiza_una_peticion_get_a(String path) {
        when().get(path).andReturn();
    }

    @Then("el servidor debe de responder con un status {int}")
    public void el_servidor_debe_de_responder_con_un_status(Integer statusCode) {
        then().statusCode(statusCode);
    }

    @Then("el cuerpo de la respuesta debe de ser una lista de tipos de mascotas")
    public void el_cuerpo_de_la_respuesta_debe_de_ser_una_lista_de_tipos_de_mascotas() {
//        then().body("$", hasSize(6));
//        then().body("size()", is(6));
        then().body("$", not(empty()));
        then().body("[0].id", notNullValue());
        then().body("[0].name", notNullValue());

    }

    @Then("el cuerpo de la respuesta contiene la propiedad id con el valor {int}")
    public void el_cuerpo_de_la_respuesta_contiene_la_propiedad_id_con_el_valor(Integer id) {
        then().body("id", is(id));
    }

    @Then("el cuerpo de la respuesta contiene la propiedad name con el valor {string}")
    public void el_cuerpo_de_la_respuesta_contiene_la_propiedad_name_con_el_valor(String name) {
        then().body("name", is(name));
    }

    @Given("el cliente configura el recurso {string} con los datos")
    public void el_cliente_configura_el_recurso_con_los_datos(String path, String docString) {
        given()
//            .baseUri(BASE_PATH)
            .basePath(path)
            .contentType(ContentType.JSON)
            .body(docString);
        Serenity.setSessionVariable("petType").to(docString);
    }

    @When("el cliente registra el nuevo tipo de mascota")
    public void el_cliente_registra_el_nuevo_tipo_de_mascota() {
        when()
            .post()
            .andReturn();
    }

    @Then("el cuerpo de la respuesta debe contener los detalles del nuevo tipo de mascota registrado")
    public void el_cuerpo_de_la_respuesta_debe_contener_los_detalles_del_nuevo_tipo_de_mascota_registrado() throws JsonProcessingException {
        String docString = Serenity.sessionVariableCalled("petType");
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        then().body(notNullValue());
        then().body("id", notNullValue());
        then().body("id", instanceOf(Integer.class));
        then().body("name", notNullValue());
        then().body("name", is(jsonMap.get("name")));
    }

    @Given("el cliente configura el recurso {string} con id {int} usando los datos")
    public void el_cliente_configura_el_recurso_con_id_usando_los_datos(String path, Integer id, String docString) {
        given()
            .basePath(path)
            .pathParam("id", id)
            .contentType(ContentType.JSON)
            .body(docString);
    }

    @When("el cliente actualiza el tipo de mascota")
    public void el_cliente_actualiza_el_tipo_de_mascota() {
        when()
            .put()
            .andReturn();
    }

    @Then("el cuerpo de la respuesta debe estar vacío")
    public void el_cuerpo_de_la_respuesta_debe_estar_vacío() {
        then().body(emptyOrNullString());
    }

    @Given("el cliente configura el recurso {string} con id {int}")
    public void el_cliente_configura_el_recurso_con_id(String path, Integer id) {
        given()
                .basePath(path)
                .pathParam("id", id)
                .contentType(ContentType.JSON);
    }
    @When("el cliente elimina el tipo de mascota")
    public void el_cliente_elimina_el_tipo_de_mascota() {
        when()
            .delete()
            .andReturn();
    }
}
