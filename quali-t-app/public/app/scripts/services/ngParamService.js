'use strict';

/**
 * @ngdoc service
 * @name qualitApp.ngParamService
 * @description
 * # ngParamService
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('ngParamService', function ($filter, ngTableParams) {
    var qaTextService = {};

    qaTextService.getDefaultNgParams = function (data) {
      return new ngTableParams({
        page: 1,
        count: 10,
        sorting: {
          name: 'id'
        }
      }, {
        total: data.length, // length of data
        getData: function ($defer, params) {
          // use build-in angular filter
          var orderedData = params.sorting() ?
            $filter('orderBy')(data, params.orderBy()) :
            data;

          $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
      })
    }

    return qaTextService;
  });
