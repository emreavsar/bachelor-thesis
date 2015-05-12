'use strict';

describe('Controller: EditProjectCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var EditProjectCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EditProjectCtrl = $controller('EditProjectCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
