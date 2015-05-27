'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CatalogListCtrl
 * @description
 * # CatalogListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CatalogListCtrl', function ($scope, apiService, alerts, ngTableParams, $filter) {
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
        $scope.catalogs = payload.data;

        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'asc'
            }
          }, {
            total: function () {
              return $scope.catalogs.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.catalogs, params.orderBy()) :
                $scope.catalogs;
              params.total(orderedData.length);
              $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
          });
        }
        else {
          $scope.tableParams.reload();
        }
      })
    }
  });
