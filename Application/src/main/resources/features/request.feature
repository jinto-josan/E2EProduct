Feature: Test HTTP Requests

  Scenario: Test GET request
    Given url 'http://localhost:8080/devicemanagement/vehicles/TESTVI/claims'
    And param method = 'POST'
    When method get
    Then status 200