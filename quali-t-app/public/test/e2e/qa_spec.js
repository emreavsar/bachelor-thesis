'use strict';

describe('quality attribute templates', function () {
  it('should create a new quality attribute without variables', function () {
    browser.get('/#/authenticated/qa/create/');
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/create");

    var firstCategory = element.all(by.css("#categories input[type='checkbox']")).first();
    var textangularField = element.all(by.css("div[id^='taTextElement']")).first();
    var createQaButton = element(by.id("create-qa-button"));

    // select first category
    firstCategory.click();

    // dummy qa description text
    textangularField.sendKeys("This is a basic qa inserted through integration tests");

    element(by.id("create-qa-button")).click();

    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/admin");

    // check success message
    var successMessage = element(by.css('.register-form .text-success'));
    expect(successMessage.getText().indexOf("Quality Attribute Template created successfully"));
  });


  it('should create a new quality attribute with freenumber variable with range', function () {
    browser.get('/#/authenticated/qa/create/');
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/create");

    var firstCategory = element.all(by.css("#categories input[type='checkbox']")).first();
    var textangularField = element.all(by.css("div[id^='taTextElement']")).first();
    var createQaButton = element(by.id("create-qa-button"));
    var feetextVariableButton = element.all(by.css("[name='addVarFree']")).first();

    // select first category
    firstCategory.click();

    // dummy qa description text
    textangularField.sendKeys("This is an extended qa inserted through integration tests.");

    // add a variable
    feetextVariableButton.click();
    var freeNumericOption = element(by.id("free-numberic-option"));
    freeNumericOption.click();

    // add a range between 0 and 100
    var useRangeCheckbox = element(by.id("free-userange-checkbox"));
    useRangeCheckbox.click();
    element(by.id("freeMinValue")).sendKeys("0");
    element(by.id("freeMaxValue")).sendKeys("100");
    element(by.id("add-free-variable-button")).click();

    // save qa
    element(by.id("create-qa-button")).click();

    // check success
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/admin");
  });


  it('should create a new quality attribute with enumtext variable', function () {
    browser.get('/#/authenticated/qa/create/');
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/create");

    var firstCategory = element.all(by.css("#categories input[type='checkbox']")).first();
    var textangularField = element.all(by.css("div[id^='taTextElement']")).first();
    var createQaButton = element(by.id("create-qa-button"));
    var enumVariableButton = element.all(by.css("[name='addVarEnum']")).first();

    // select first category
    firstCategory.click();

    // dummy qa description text
    textangularField.sendKeys("This is an extended qa inserted through integration tests.");

    // add a variable
    enumVariableButton.click();
    var enumTextOption = element(by.id("enum-text-option"));
    enumTextOption.click();

    // add some items into enum

    var addedElementsToEnum = element.all(by.css("#enum-list-ul li"));

    // simulate a typo, and remove it after adding it to the list
    element(by.name("nameText")).sendKeys("Frontent");
    element(by.id("add-enum-element-button")).click();
    expect(addedElementsToEnum.count()).toBe(1);

    element(by.id("remove-enum-element-button")).click();
    expect(addedElementsToEnum.count()).toBe(0);

    element(by.name("nameText")).sendKeys("Frontend");
    element(by.id("add-enum-element-button")).click();
    expect(addedElementsToEnum.count()).toBe(1);

    element(by.name("nameText")).sendKeys("Backend");
    element(by.id("add-enum-element-button")).click();
    expect(addedElementsToEnum.count()).toBe(2);


    element(by.id("add-enum-variable-button")).click();

    // save qa
    element(by.id("create-qa-button")).click();

    // check success
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/qa/admin");
  });
});
