# language: en
Feature: Servicio SOAP de Calculadora

  @Sumar
  Scenario: Calcular la suma de dos números
    Given que los números 3 y 4 están listos para ser sumados
    When envío una solicitud SOAP a "/calculator.asmx"
    Then la respuesta debe contener el resultado 7