'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CatalogListCtrl
 * @description
 * # CatalogListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CatalogListCtrl', function ($scope, apiService, alerts, ngParamService) {
    $scope.catalogs = new Array();


    $scope.deleteCatalog = function (catalogId) {
      var deletePromise = apiService.deleteCatalog(catalogId);

      deletePromise.then(
        function (payload) {
          $scope.init();
        });
    }

    $scope.init = function () {
      var initPromise = apiService.getCatalogs();
      initPromise.then(function (payload) {
        var data = payload.data;
        $scope.catalogs = data;
        $scope.tableParams = ngParamService.getDefaultNgParams(data);
      })
    }
  });
