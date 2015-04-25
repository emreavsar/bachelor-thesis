'use strict';

describe('Controller: MytasksCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var MytasksCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MytasksCtrl = $controller('MytasksCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
