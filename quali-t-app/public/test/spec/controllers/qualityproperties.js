'use strict';

describe('Controller: QualitypropertiesCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var QualitypropertiesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    QualitypropertiesCtrl = $controller('QualitypropertiesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
