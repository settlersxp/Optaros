Feature: for testing purposes
  qkwlenqwe

  @success
  Scenario: awesome success
    Given I navigate to the homepage as a new visitor

  @failure
  Scenario: awesome failure
    Given I register as a new user on checkout

  Scenario: navigate to the tabaradetestare.ro blog
    Given I am on the google website
    And I search for "tabara de testare"
    When I click on the forth result
    Then I should be taken to "http://tabaradetestare.ro/"