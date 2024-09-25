@pettypes
Feature: Gestionar tipos de mascotas

  @listarTiposMascotas
  Scenario: Listar todos los tipos de mascotas
    Given el cliente configura la URI base
    When el cliente realiza una peticion GET a "/pettypes"
    Then el servidor debe de responder con un status 200
    And el cuerpo de la respuesta debe de ser una lista de tipos de mascotas

#  Scenario: Listar un tipo de mascota por id 4
#    Given el cliente configura la URI base
#    When el cliente realiza una peticion GET a "/pettypes/4"
#    Then el servidor debe de responder con un status 200
#
#  Scenario: Listar un tipo de mascota por id 5
#    Given el cliente configura la URI base
#    When el cliente realiza una peticion GET a "/pettypes/5"
#    Then el servidor debe de responder con un status 200

  @obtenerMascotaPorId
  Scenario Outline: Obtener mascota por id
    Given el cliente configura la URI base
    When el cliente realiza una peticion GET a <path>
    Then el servidor debe de responder con un status <statusCode>
    And el cuerpo de la respuesta contiene la propiedad id con el valor <id>
    And el cuerpo de la respuesta contiene la propiedad name con el valor <name>
    Examples:
      | path          | statusCode | id | name    |
      | "/pettypes/4" | 200        | 4  | "snake" |
      | "/pettypes/5" | 200        | 5  | "bird"  |

  @registrarTipoMascota
  Scenario: Registrar un nuevo tipo de mascota
    Given el cliente configura la URI base
    And el cliente configura el recurso "/pettypes" con los datos
       """
         {
             "name": "rabbit"
         }
       """
    When el cliente registra el nuevo tipo de mascota
    Then el servidor debe de responder con un status 201
    And el cuerpo de la respuesta debe contener los detalles del nuevo tipo de mascota registrado

  @actualizarTipoMascota
  Scenario: Actualizar un tipo de mascota
    Given el cliente configura la URI base
    And el cliente configura el recurso "/pettypes/{id}" con id 9 usando los datos
       """
         {
             "name": "abc"
         }
       """
    When el cliente actualiza el tipo de mascota
    Then el servidor debe de responder con un status 204
    And el cuerpo de la respuesta debe estar vacío

  @eliminarTipoMascota
  Scenario: Eliminar un tipo de mascota
    Given el cliente configura la URI base
    And el cliente configura el recurso "/pettypes/{id}" con id 2
    When el cliente elimina el tipo de mascota
    Then el servidor debe de responder con un status 204
    And el cuerpo de la respuesta debe estar vacío