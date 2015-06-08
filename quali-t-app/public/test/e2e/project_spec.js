'use strict';

describe('project', function () {
  var testProjectName = "A test Project";
  var testJiraKey = "QTP";


  it('should create a new project with the first customer and SM quality properties', function () {
    browser.get('/#/authenticated/project/create');
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/project/create");

    // basic information about the catalog
    element(by.id("projectName")).sendKeys(testProjectName);
    element(by.id("jiraKey")).sendKeys(testJiraKey);

    // special input (needs workarounds because it is ui-select)
    element(by.id("customerName")).click(); // opens the selection box
    element(by.css("#customerName .ui-select-choices-row")).click(); // selects the first customer

    // same for quality properties and jira instance
    element(by.id("jiraInstance")).click();
    element(by.css("#jiraInstance .ui-select-choices-row")).click();

    // select two quality properties
    for (var i = 0; i < 2; i++) {
      element(by.id("projectQualityProperties")).click();
      element(by.css("#projectQualityProperties .ui-select-choices-row")).click();
    }

    element(by.id("choose-catalog-button")).click();

    // select the first catalog
    element(by.id("catalog")).click();
    element(by.css("#catalog .ui-select-choices-row")).click();

    // select 1 qa
    var qas = element.all(by.css('.qa'));
    qas.count().then(function (count) {
      var projectHasQas = count > 0;

      if (projectHasQas) {
        qas.get(0).click();
      }

      // create project
      element(by.id("create-project-button")).click();

      // verify
      expect(browser.getLocationAbsUrl()).toContain("/authenticated/project/edit");
      // if there were qas added to project, they should also appear in the created project
      if (projectHasQas) {
        expect(element.all(by.css("qa")).count()).toBeGreaterThan(0);
      } else {
        expect(element.all(by.css("qa")).count()).toBe(0);
      }
    });
  });
});
