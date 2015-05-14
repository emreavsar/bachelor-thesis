'use strict';

describe('Controller: EditQaCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var EditQaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EditQaCtrl = $controller('EditQaCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
