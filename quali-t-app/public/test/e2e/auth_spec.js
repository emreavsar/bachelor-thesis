'use strict';

describe('login', function () {
  it('should go to login page, try to login with a non existent user', function () {
    browser.get('/#/login');
    expect(browser.getLocationAbsUrl()).toMatch("/login");

    var usernameInput = element(by.id("input-username"));
    var passwordInput = element(by.id("input-password"));

    usernameInput.sendKeys("this_username_does_not_exist");
    passwordInput.sendKeys("foobar");

    element(by.id("button_sign_in")).click();

    expect(browser.getLocationAbsUrl()).toMatch("/login");
  });


  it('should go to login page, try to login with an existent user', function () {
    browser.get('/#/login');
    expect(browser.getLocationAbsUrl()).toMatch("/login");

    var usernameInput = element(by.id("input-username"));
    var passwordInput = element(by.id("input-password"));

    usernameInput.sendKeys("admin@quali-t.ch");
    passwordInput.sendKeys("admin");

    element(by.id("button_sign_in")).click();

    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/dashboard");
  });

  it('should try to register a new user', function () {
    browser.get('/#/register');
    expect(browser.getLocationAbsUrl()).toMatch("/register");

    var usernameInput = element(by.id("input-username"));
    var passwordInput = element(by.id("input-password"));
    var passwordRepeatedInput = element(by.id("input-passwordRepeated"));

    usernameInput.sendKeys("a_protractor_user@quali-t.ch");
    passwordInput.sendKeys("protractorIsCool");
    passwordRepeatedInput.sendKeys("protractorIsCool");

    element(by.id("btn-register")).click();

    // either the user was created successfully
    var successMessage = element(by.css('.register-form .text-success'));

    // or the user already exists (running multiple times)
    var errorMessage = element(by.css('#alerts-container .alert:first'));

    expect(successMessage.isPresent() || errorMessage.getText().toLowerCase().indexOf("user already exists") > -1).toBe(true);
  })

});
