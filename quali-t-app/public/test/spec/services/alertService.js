'use strict';

describe('Service: alertService', function () {

  // load the service's module
  beforeEach(module('qualitApp'));

  // instantiate service
  var alertService;
  beforeEach(inject(function (_alertService_) {
    alertService = _alertService_;
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });

});
