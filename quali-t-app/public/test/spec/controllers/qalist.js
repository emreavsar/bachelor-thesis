'use strict';

describe('Controller: QaListCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var QaListCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    QaListCtrl = $controller('QaListCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
