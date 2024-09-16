Feature: FB CRM onboarding

  Background: Setup
    * def result = callonce read(base_path + 'setup/authorization.feature')
    * def token = result.accessToken
    * def requests = base_path + 'requests/'
    Given url smApi

  Scenario: Testing FB CRM onboarding
    #    Setup Onboarding contract
    Given url e2eApi
    And path createResponse
    And request read(requests + 'e2eInternalContractCreate.json')
    When method post
    Then status 200
    * print response

    Given url customSendContractUrl
    When method post
    Then status 202





  #    * header Authorization = token
  #    And path crmContractUri
  #    And request read(requests + 'crmContract.json')
  #    When method post
  #    Then status 201
  #    * def vehicles = response.body[0]
  #    * def services = vehicles.services[0]
  #    * print vehicles
  #    And match karate.filterKeys(vehicles, basicVehicleValidation) == basicVehicleValidation
  #    And match karate.filterKeys(services, serviceValidation) == serviceValidation