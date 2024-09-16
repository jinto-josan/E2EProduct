@ignore
Feature: OAuth 2.0 Authorization using Azure
  Scenario: Get access token from Azure
    Given url adOAuth2Url
    And form field grant_type = 'client_credentials'
    And form field client_id = smClientId
    And form field client_secret = smClientSecret
    And form field scope = smScope
    When method post
    Then status 200
    * def accessToken = 'Bearer ' + response.access_token
    * print 'Token fetched successfully'