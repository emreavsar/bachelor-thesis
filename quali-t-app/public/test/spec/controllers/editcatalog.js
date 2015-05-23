'use strict';

describe('Controller: EditCatalogCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var EditCatalogCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EditCatalogCtrl = $controller('EditCatalogCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
