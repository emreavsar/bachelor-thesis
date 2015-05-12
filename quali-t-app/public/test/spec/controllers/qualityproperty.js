'use strict';

describe('Controller: QualitypropertyCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var QualitypropertyCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    QualitypropertyCtrl = $controller('QualitypropertyCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
