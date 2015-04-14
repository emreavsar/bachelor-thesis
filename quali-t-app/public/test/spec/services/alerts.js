'use strict';

describe('Service: alerts', function () {

  // load the service's module
  beforeEach(module('qualitApp'));

  // instantiate service
  var alerts;
  beforeEach(inject(function (_alerts_) {
    alerts = _alerts_;
  }));

  it('todo', function () {
    expect('todo').toBe('todo');
  });

});
