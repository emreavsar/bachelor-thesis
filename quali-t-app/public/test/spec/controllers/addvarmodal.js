'use strict';

describe('Controller: AddVarModalCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var AddVarModalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddVarModalCtrl = $controller('AddVarModalCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
