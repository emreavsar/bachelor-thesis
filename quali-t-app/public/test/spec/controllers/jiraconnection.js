'use strict';

describe('Controller: JiraconnectionCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var JiraconnectionCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    JiraconnectionCtrl = $controller('JiraconnectionCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
