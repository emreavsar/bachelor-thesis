'use strict';

describe('Service: qaTextService', function () {

  // load the service's module
  beforeEach(module('qualitApp'));

  // instantiate service
  var qaTextService;
  beforeEach(inject(function (_qaTextService_) {
    qaTextService = _qaTextService_;
  }));

  it('should do something', function () {
    expect(!!qaTextService).toBe(true);
  });

});
