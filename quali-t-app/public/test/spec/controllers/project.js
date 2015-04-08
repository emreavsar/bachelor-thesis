'use strict';

describe('Controller: ProjectCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var ProjectCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProjectCtrl = $controller('ProjectCtrl', {
      $scope: scope
    });
  }));

  it('should be a test', function () {
    expect('todo').toBe('todo');
  });
});
