'use strict';

describe('Directive: filter', function () {

  // load the directive's module
  beforeEach(module('qualitApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('todo', inject(function () {
    expect('todo').toBe('todo');
  }));
});
