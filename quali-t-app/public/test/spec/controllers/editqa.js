'use strict';

describe('Controller: EditQaCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var EditQaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EditQaCtrl = $controller('EditQaCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
