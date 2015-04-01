'use strict';

describe('Service: authValidationFactory', function () {

  // load the service's module
  beforeEach(module('qualitApp'));

  // instantiate service
  var authValidationFactory;
  beforeEach(inject(function (_authValidationFactory_) {
    authValidationFactory = _authValidationFactory_;
  }));

  it('should do something', function () {
    //expect(!!authValidationFactory).toBe(true);
  });

});
