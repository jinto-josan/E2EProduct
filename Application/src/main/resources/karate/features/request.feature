@e2eHappyDev
Feature: Vehicle Lifecycle e2e

Create a new Vehicle Contract and check all lifecycle steps

  Background: Prepare validation objects
#    * def contractRequest = read('classpath:e2e/lifecycle/vehicle/contractRequest.json')
#    * def InfoHubTruckLiveContract = read('classpath:e2e/lifecycle/vehicle/InfoHubTruckLiveContract.json')
#    * def basicVehicleValidation = read('classpath:e2e/lifecycle/validation/basicVehicle.json')
#    * def serviceValidation =  read('classpath:e2e/lifecycle/validation/vehicleUptimeService.json')
#    * def claimingValidation =  read('classpath:e2e/lifecycle/validation/vehicleClaimed.json')
#    * def profileValidation = read('classpath:e2e/lifecycle/validation/vehiclesProfiles.json')
#    * def unclaimingValidation =  read('classpath:e2e/lifecycle/validation/vehicleUnclaimed.json')
#    * def token = devtoken
#    * def sleep = function(pause){ java.lang.Thread.sleep(pause*1000) }




  Scenario: Vehicle Lifecycle Procedure
    # Send vehicle with E2ETest contract
    * header Authorization = token

#    Given url serviceMgmtDevBaseUri
#    And path smContractUrl
#    And request contractRequest
#    When method post
#    Then status 201
#    * def vehicles = response.body[0]
#    * def services = vehicles.services[0]
#    #    And match karate.filterKeys(vehicles, basicVehicleValidation) == basicVehicleValidation
#    And match karate.filterKeys(services, serviceValidation) == serviceValidation
#
#    # Start onboarding manually
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smOnboardingVinUri + contractRequest[0].vin
#    When method get
#    Then status 200
#
#    # Check claiming Status
#    * def isValidTime = read('classpath:scripts/valid-timestamp.js')
#    * configure retry = { count: 10, interval: 5000 }
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smVehicleBaseUri + '/' + contractRequest[0].vin + '/status'
#    And retry until response.body.claiming.claimingState == 'CLAIMED'
#    When method get
#    Then status 200
#    * def claimed = response.body
#    * def service = claimed.services[0]
#    And claimed.hasActiveService == true
#    And claimed.activatedServices == false
#    And match claimed.claiming.startedAt == "##string"
#    And match karate.filterKeys(service, serviceValidation) == serviceValidation
#    And match karate.filterKeys(claimed.claiming, claimingValidation) == claimingValidation
#
#    # Start Profile Fetch manually
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smConfigVinUri + contractRequest[0].vin
#    When method get
#    Then status 200
#
#    # Check Profile Status after activation
#    * sleep(20)
#    * def isValidTime = read('classpath:scripts/valid-timestamp.js')
#    * def isCorrectStateDescription = function(x) { return ['Activating', 'Active' ,'Inactive'].includes(x) }
#    * def isCorrectState = function(x) { return [0, 1, 2].includes(x) }
#    * def isValidMsgId = function(x) { return ['#present' , '##string'].includes(x) }
#    * def vehicleProfileSchema = read('classpath:e2e/lifecycle/validation/schemas/vehicleProfile.schema.json')
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smVehicleBaseUri + '/' + contractRequest[0].vin + '/status'
#    When method get
#    Then status 200
#    * def profileVehicle = response.body
#    * def profile = profileVehicle.profiles[0]
#
#
#    # Check Successful ServiceOrder
#    # TODO: add retryable vehicle lookup until services are active
#
#    #Post Infohub truckLive Contract
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smmockKafkaUrl
#    And param topic = 'topic.infohub.trucklive.activationdeactivation.dev'
#    And request InfoHubTruckLiveContract
#    When method post
#    Then status 200
#
#    #check vehicle ->MM should be moved to idle
#    * sleep(20)
#    * def isValidTime = read('classpath:scripts/valid-timestamp.js')
#    * def isCorrectStateDescription = function(x) { return ['Activating', 'Active' ,'Inactive'].includes(x) }
#    * def isCorrectState = function(x) { return [0, 1, 2].includes(x) }
#    * def isValidMsgId = function(x) { return ['#present' , '##string'].includes(x) }
#    * def vehicleProfileSchema = read('classpath:e2e/lifecycle/validation/schemas/vehicleProfile.schema.json')
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smVehicleBaseUri + '/' + contractRequest[0].vin + '/status'
#    When method get
#    Then status 200
#    * def Vehicle = response.body
#    * def profile = Vehicle.profiles[0]
#    * def service0 = Vehicle.services[0]
#    * def service1 = Vehicle.services[1]
#    And match service0.serviceName == 'MBUptime'
#    And match service1.serviceName == 'Maintenance Management'
#    ## service contractstate nneds to be ACTIVE
#    And match service0.contractState == 'ACTIVE'
#    ## service contractstate nneds to be IDLE
#    And match service1.contractState == 'IDLE'
#    And claimed.hasActiveService == true
#
#
#    # update vehicle with inactive Contract
#    * contractRequest[0].status = 0
#    * contractRequest[0].contractend = '2001-01-01'
#    * header Authorization = token
#    Given url serviceMgmtDevBaseUri
#    And path smContractUrl
#    And request contractRequest
#    When method post
#    Then status 201
#    * def vehicles = response.body[0]
#    * def service0 = vehicles.services[0]
#    * def service1 = vehicles.services[1]
#    And claimed.hasActiveService == true
#    And claimed.activatedServices == true
#    And match service0.serviceName == 'MBUptime'
#    And match service1.serviceName == 'Maintenance Management'
#    ## service contractstate nneds to be ACTIVE
#    And match service0.contractState == 'INACTIVE'
#    ## service contractstate nneds to be IDLE
#    And match service1.contractState == 'ACTIVE'
#    And claimed.hasActiveService == true



  # TODO: check deactivated profiles and services
  #
  #  # Start offboarding manually
  #    * header Authorization = token
  #    Given url serviceMgmtBaseUri
  #    And path smOffboardingUri
  #    When method get
  #    Then status 200
  ##
  ###  # TODO: check offloaded claiming state
  #    * sleep(20)
  #    * def isValidTime = read('classpath:scripts/valid-timestamp.js')
  #    * configure retry = { count: 10, interval: 5000 }
  #    * header Authorization = token
  #    Given url serviceMgmtBaseUri
  #    And path smVehicleBaseUri + '/' + contractRequest[0].vin + '/status'
  #    And retry until response.body.claiming.claimingState == 'UNCLAIMED'
  #    When method get
  #    Then status 200
  #    * def claimed = response.body
  #    * def service = claimed.services[0]
  #    And claimed.hasActiveService == false
  #    And claimed.activatedServices == false
  #    And match claimed.claiming.startedAt == "##string"
  #    And match claimed.claiming.stateChangedAt == "##string"
  #    And match karate.filterKeys(claimed.claiming, unclaimingValidation) == unclaimingValidation


