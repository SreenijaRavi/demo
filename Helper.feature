Feature: Verify fetch API

  Background:

    * def Query = Java.type('APIAutomation_HopStream.Utility.GetEnvURL');
    * def Base_URL = Query.GetBaseURL2()
    * def Query = Java.type('APIAutomation_HopStream.Fetch.Fetch_Query');
    * def keyStorePath = keyStorePath
    * def keyStorepwd = keyStorePasword
    * configure ssl = { keyStore:'#(keyStorePath)',keyStorePassword:'#(keyStorepwd)', keyStoreType: 'pkcs12'}
    * url  Base_URL

  @WithValidID @ignore
  Scenario: valid
    * def ExpectedResponse = read('classpath:src/test/java/APIAutomation_HopStream/ResponseBody/Fetch.json')


    Given path "/api/logmon/v1/fetch/"+JobID
    When method get
    Then status 200
    And match response == ExpectedResponse
    And match response.data.raw_log_path == '#notnull'
    * def totalLinesFromQuery = Query.getFileNames(response.data.raw_log_path)
    And match totalLinesFromQuery == response.data.n_pushed

  @WithInvalidID
  Scenario: invalid
    Given path "/api/logmon/v1/fetch/"+Id[0]
    When method get
    Then status 404
