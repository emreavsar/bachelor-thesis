'use strict';

describe('Controller: CategoryCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var CategoryCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CategoryCtrl = $controller('CategoryCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
