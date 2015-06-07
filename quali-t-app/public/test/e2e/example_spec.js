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

});
