'use strict';

describe('Controller: RolesCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var RolesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    RolesCtrl = $controller('RolesCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
