'use strict';

describe('Controller: CreateCatalogCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var CreateCatalogCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CreateCatalogCtrl = $controller('CreateCatalogCtrl', {
      $scope: scope
    });
  }));

  it('should be implemented', function () {
    expect('todo').toBe('todo');
  });
});
