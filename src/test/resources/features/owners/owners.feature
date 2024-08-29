@gestionarPropietarios
Feature: Gestionar propietarios

  @listarPropietarios
  Scenario: Listar todos propietarios
    Given el cliente configura la URI base
    When el cliente realiza una peticion GET a "/owners"
    Then el servidor debe de responder con un status 200
    And el cuerpo de la respuesta debe ser una lista de propietarios valida

  @listarPropietarioPorId
  Scenario: Validar detalles del propietario y sus mascotas
    Given el cliente configura la URI base
    When el cliente realiza una peticion GET a "/owners/6"
    Then el servidor debe de responder con un status 200
#    And el cuerpo de la respuesta contiene con las siguientes propiedades
#      | firstName |
#      | lastName  |
#      | address   |
#      | city      |
#      | telephone |
#    And el cuerpo de la respuesta contiene con las siguientes propiedades2
#      | firstName | lastName | address | city | telephone |
    And el cuerpo de la respuesta contiene con las siguientes propiedades y valores
      | firstName | Jean             |
      | lastName  | Coleman          |
      | address   | 105 N. Lake St.  |
      | city      | Monona           |
      | telephone | $str{6085552654} |
      | id        | 6                |
    And el cliente comprueba el valor del campo "firstName" es igual a "Jean"
    And el cliente comprueba el valor del campo "lastName" es igual a "Coleman"
    And el cliente comprueba el valor del campo "address" es igual a "105 N. Lake St."
    And el cliente comprueba el valor del campo "city" es igual a "Monona"
    And el cliente comprueba el valor del campo "telephone" es igual a "6085552654"
    And el cliente comprueba el valor del campo "id" es igual a 6

  @registrarPropietario
  Scenario: Registrar propietario exitosamente
    Given el cliente tiene los datos de un nuevo propietario
       """
          {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "123456789"
          }
       """
    When el cliente realiza una peticion POST a "/owners" con los detalles del nuevo propietario
    Then el servidor debe de responder con un status 201
    And el cliente comprueba el valor del campo "firstName" es igual a "Dany"
    And el cliente comprueba el valor del campo "lastName" es igual a "Cenas"
    And el cliente comprueba el valor del campo "address" es igual a "Av. Javier Prado"
    And el cliente comprueba el valor del campo "city" es igual a "Lima"
    And el cliente comprueba el valor del campo "telephone" es igual a "123456789"

  @registrarPropietarioConCamposRequeridos
  Scenario Outline: Registrar propietario con campos requeridos
    Given el cliente omite el campo requerido "<campo>" en el siguiente JSON:
       """
          {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "123456789"
          }
       """
    When el cliente realiza una peticion POST a "/owners"
    Then el servidor debe de responder con un status 400
    Examples:
      | campo     |
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |

  @registrarPropietarioConCamposVacios
  Scenario Outline: Registrar propietario con campos vacios
    Given el cliente proporciona los campos <firstName> <lastName> <address> <city> <telephone>
    When el cliente realiza una peticion POST a "/owners"
    Then el servidor debe de responder con un status 400
    Examples:
      | firstName | lastName | address            | city   | telephone   |
      | ""        | "Cenas"  | "Av. Javier Prado" | "Lima" | "123456789" |
      | "Dany"    | ""       | "Av. Javier Prado" | "Lima" | "123456789" |
      | "Dany"    | "Cenas"  | ""                 | "Lima" | "123456789" |
      | "Dany"    | "Cenas"  | "Av. Javier Prado" | ""     | "123456789" |
      | "Dany"    | "Cenas"  | "Av. Javier Prado" | "Lima" | ""          |

  @registrarPropietarioConCamposNulos
  Scenario Outline: Registrar propietario con campos nulos
    Given el cliente omite el campo requerido "<campo>" con valor null en el siguiente JSON:
       """
          {
              "firstName": "Dany",
              "lastName": "Cenas",
              "address": "Av. Javier Prado",
              "city": "Lima",
              "telephone": "123456789"
          }
       """
    When el cliente realiza una peticion POST a "/owners"
    Then el servidor debe de responder con un status 400
    Examples:
      | campo     |
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |

  @registrarPropietarioConCSV
  Scenario Outline: Registrar propietario exitosamente
    Given el cliente tiene los datos de un nuevo propietario identificado con id <id>
      | firstName |
      | lastName  |
      | address   |
      | city      |
      | telephone |
    When el cliente realiza una peticion POST a "/owners"
    Then el servidor debe de responder con un status 201

    Examples:
      | id  |
      | 1   |
      | 3   |
      | 5   |