package com.mycompany.steps;

import com.mycompany.util.SoapRequestUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.CoreMatchers.is;

public class CalculatorDefinitions {

    private EnvironmentVariables environmentVariables;
    private String BASE_PATH;

    @Before
    public void setUp() {
        environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        BASE_PATH = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("environments.soap.base.uri");
//        rest().given().baseUri(BASE_PATH);
//        given().baseUri(BASE_PATH);
//        setDefaultBasePath(BASE_PATH);
        RestAssured.baseURI = BASE_PATH;
    }

    @Given("que los números {int} y {int} están listos para ser sumados")
    public void queLosNumerosYEstánListosParaSerSumados(int intA, int intB) throws Exception {
        String requestBody = SoapRequestUtil.createSoapRequest(SoapRequestUtil.Operation.ADD, Map.of("intA", intA, "intB", intB));
        given()
                .baseUri(BASE_PATH)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(requestBody);
    }

    @When("envío una solicitud SOAP a {string}")
    public void envioUnaSolicitudSOAPA(String path) {
        when()
                .post(path).andReturn();
    }

    @Then("la respuesta debe contener el resultado {int}")
    public void laRespuestaDebeContenerElResultado(Integer resultado) {
        then().statusCode(200)
                .and().contentType(ContentType.XML)
                .and().body("Envelope.Body.AddResponse.AddResult.text()", is(String.valueOf(resultado)));
//        then().statusCode(200);
//        then().contentType(ContentType.XML);
//        then().body("Envelope.Body.AddResponse.AddResult.text()", is(String.valueOf(resultado)));
        expect().statusCode(200);
    }
}
