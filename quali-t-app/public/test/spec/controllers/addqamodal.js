'use strict';

describe('Controller: AddQaModalCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var AddQaModalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddQaModalCtrl = $controller('AddQaModalCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
