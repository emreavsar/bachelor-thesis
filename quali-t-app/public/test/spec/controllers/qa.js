'use strict';

describe('Controller: QACtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var QaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    QaCtrl = $controller('QACtrl', {
      $scope: scope
    });
  }));

  it('should be implemented', function () {
    expect('todo').toBe('todo');
  });
});
