package com.mycompany.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.util.CsvUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.apache.commons.csv.CSVRecord;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.hamcrest.Matchers.*;

public class OwnersDefinitions {

    private EnvironmentVariables environmentVariables;
    private String BASE_PATH;
    String filePath = "src/test/resources/owners-data.csv";

    @Given("el cliente tiene los datos de un nuevo propietario")
    public void elClienteTieneLosDatosDeUnNuevoPropietario(String docString) {
        Serenity.setSessionVariable("owner").to(docString);
    }

    @When("el cliente realiza una peticion POST a {string} con los detalles del nuevo propietario")
    public void elClienteRealizaUnaPeticionPOSTAConLosDetallesDelNuevoPropietario(String path) {
        String owner = Serenity.sessionVariableCalled("owner");
        given().contentType(ContentType.JSON)
                .body(owner)
                .post(BASE_PATH.concat(path))
                .andReturn();
    }

    @Given("el cliente omite el campo requerido {string} en el siguiente JSON:")
    public void elClienteOmiteElCampoRequeridoEnElSiguienteJSON(String campo , String docString) throws JsonProcessingException {
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        jsonMap.remove(campo);
        Serenity.setSessionVariable("owner").to(jsonMap);
    }

    @When("el cliente realiza una peticion POST a {string}")
    public void elClienteRealizaUnaPeticionPOSTA(String path) {
        Map<String, Object> owner = Serenity.sessionVariableCalled("owner");
        given().contentType(ContentType.JSON)
                .body(owner)
                .post(BASE_PATH.concat(path))
                .andReturn();
    }


    @Given("el cliente proporciona los campos {string} {string} {string} {string} {string}")
    public void elClienteProporcionaLosCampos(String firstName, String lastName, String address, String city, String telephone) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("firstName", firstName);
        jsonMap.put("lastName", lastName);
        jsonMap.put("address", address);
        jsonMap.put("city", city);
        jsonMap.put("telephone", telephone);
        Serenity.setSessionVariable("owner").to(jsonMap);
    }


    @Given("el cliente proporciona el campo {string} con una longitud de {int} caracteres en el siguiente JSON:")
    public void elClienteProporcionaElCampoConUnaLongitudDeCaracteresEnElSiguienteJSON(String campo, int longitud, String docString) throws JsonProcessingException {
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        String valorGenerado = generarString(jsonMap.get(campo).toString(), longitud);
        jsonMap.put(campo, valorGenerado);
        Serenity.setSessionVariable("owner").to(jsonMap);
    }

    private String generarString(String valorActual, int longitud) {
        return valorActual.repeat((longitud / valorActual.length()) + 1).substring(0, longitud);
    }

    @Given("el cliente omite el campo requerido {string} con valor null en el siguiente JSON:")
    public void elClienteOmiteElCampoRequeridoConValorNullEnElSiguienteJSON(String campo, String docString) throws JsonProcessingException {
        Map<String, Object> jsonMap = new ObjectMapper().readValue(docString, new TypeReference<>() {
        });
        jsonMap.put(campo, null);
        Serenity.setSessionVariable("owner").to(jsonMap);
    }

    @And("el cuerpo de la respuesta debe ser una lista de propietarios valida")
    public void elCuerpoDeLaRespuestaDebeSerUnaListaDePropietariosValida() {
        then().body("$", instanceOf(List.class));
        then().body("$", not(empty()));

        then().body("firstName", everyItem(notNullValue()));
        then().body("lastName", everyItem(notNullValue()));
        then().body("address", everyItem(notNullValue()));
        then().body("city", everyItem(notNullValue()));
        then().body("telephone", everyItem(notNullValue()));
        then().body("id", everyItem(notNullValue()));
        then().body("pets", everyItem(instanceOf(List.class)));

        then().body("pets.name", everyItem(everyItem(notNullValue())));
        then().body("pets.birthDate", everyItem(everyItem(notNullValue())));
        then().body("pets.type", everyItem(everyItem(notNullValue())));
        then().body("pets.type.name", everyItem(everyItem(notNullValue())));
        then().body("pets.type.id", everyItem(everyItem(notNullValue())));
        then().body("pets.id", everyItem(everyItem(notNullValue())));
        then().body("pets.ownerId", everyItem(everyItem(notNullValue())));

        then().body("pets.visits", everyItem(everyItem(instanceOf(List.class))));
        then().body("pets.visits.date", everyItem(everyItem(everyItem(notNullValue()))));
        then().body("pets.visits.description", everyItem(everyItem(everyItem(notNullValue()))));
        then().body("pets.visits.id", everyItem(everyItem(everyItem(notNullValue()))));

        then().body("firstName", everyItem(instanceOf(String.class)));
        then().body("lastName", everyItem(instanceOf(String.class)));
        then().body("address", everyItem(instanceOf(String.class)));
        then().body("city", everyItem(instanceOf(String.class)));
        then().body("telephone", everyItem(instanceOf(String.class)));
        then().body("id", everyItem(instanceOf(Integer.class)));
        then().body("pets", everyItem(instanceOf(List.class)));

        then().body("firstName", everyItem(allOf(notNullValue(), instanceOf(String.class))));
        then().body("lastName", everyItem(allOf(notNullValue(), instanceOf(String.class))));
        then().body("address", everyItem(allOf(notNullValue(), instanceOf(String.class))));
        then().body("city", everyItem(allOf(notNullValue(), instanceOf(String.class))));
        then().body("telephone", everyItem(allOf(notNullValue(), instanceOf(String.class))));
        then().body("id", everyItem(allOf(notNullValue(), instanceOf(Integer.class))));
        then().body("pets", everyItem(instanceOf(List.class)));
    }

    @And("el cliente comprueba el valor del campo {string} es igual a {string}")
    public void elClienteCompruebaElValorDelCampoEsIgualA(String campo, String valor) {
        then().body(campo, is(valor));
    }

    @And("el cliente comprueba el valor del campo {string} es igual a {int}")
    public void elClienteCompruebaElValorDelCampoEsIgualA(String campo, Integer valor) {
        then().body(campo, is(valor));
    }

    @And("el cuerpo de la respuesta debe ser un propietario valido con las siguientes propiedades")
    public void elCuerpoDeLaRespuestaDebeSerUnPropietarioValidoConLasSiguientesPropiedades(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        data.forEach((campo, valor) -> {
            then().body(campo, is(parseValue(valor)));
        });
    }

    private Object parseValue(String valor) {
        if (valor.startsWith("$str{") && valor.endsWith("}")) {
            return valor.substring(5, valor.length() - 1);
        } else if (valor.matches("-?\\d+")) {
            return Integer.parseInt(valor);
        } else if (valor.matches("-?\\d+\\.\\d+")) {
            return Double.parseDouble(valor);
        } else {
            return valor;
        }
    }

    @Given("el cliente tiene los datos de un nuevo propietario identificado con id {int}")
    public void elClienteTieneLosDatosDeUnNuevoPropietarioIdentificadoConId(Integer id, DataTable dataTable) throws IOException {
        Optional<CSVRecord> recordOpt = CsvUtils.findRowByProperty(filePath, "id", String.valueOf(id));
        List<List<String>> rows = dataTable.asLists(String.class);
        Map<String, Object> jsonMap = new HashMap<>();
        rows.forEach(row -> {
            recordOpt.ifPresent(record -> {
                String campo = row.get(0);
                jsonMap.put(campo, recordOpt.get().get(campo));
            });
        });
        Serenity.setSessionVariable("owner").to(jsonMap);
    }
}