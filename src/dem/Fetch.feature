Feature: Verify fetch API

  Background:

    * def Query = Java.type('APIAutomation_HopStream.Utility.GetEnvURL');
    * def Base_URL = Query.GetBaseURL2()
    * def Query = Java.type('APIAutomation_HopStream.Fetch.Fetch_Query');
    * def keyStorePath = keyStorePath
    * def keyStorepwd = keyStorePasword
    * configure ssl = { keyStore:'#(keyStorePath)',keyStorePassword:'#(keyStorepwd)', keyStoreType: 'pkcs12'}
    * url  Base_URL
    * def Id = []

  Scenario: Validating with valid job ID
    * def Jobs = call read('classpath:src/test/java/APIAutomation_HopStream/AllJobs/AllJobs.feature')


    And eval for(var i=0;i<Jobs.response.data.length; i++) if(Jobs.response.data[i].status.endsWith("completed")) Id.add(Jobs.response.data[i].job_id)
    * def a = Id[0] == null ? 'classpath:src/test/java/APIAutomation_HopStream/Fetch/Helper.feature@WithInvalidID' : 'classpath:src/test/java/APIAutomation_HopStream/Fetch/Helper.feature@WithValidID'
    * print a
    * def result = call read(a) {JobID : #(Id[0])}




