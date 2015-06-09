'use strict';

/**
 * @ngdoc service
 * @name qualitApp.configService
 * @description
 * # configService
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('configService', function () {
    var configService = {};

    configService.standardCatalogId = -6000;
    configService.apiPath = "/api/";

    return configService;
  });
