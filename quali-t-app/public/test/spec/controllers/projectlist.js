'use strict';

describe('Controller: ProjectListCtrl', function () {

  // load the controller's module
  beforeEach(module('qualitApp'));

  var ProjectListCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProjectListCtrl = $controller('ProjectListCtrl', {
      $scope: scope
    });
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });
});
