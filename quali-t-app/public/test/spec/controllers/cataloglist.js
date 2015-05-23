'use strict';

describe('Controller: CatalogListCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var CatalogListCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CatalogListCtrl = $controller('CatalogListCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
