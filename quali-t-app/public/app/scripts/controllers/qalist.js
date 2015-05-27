'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QaListCtrl
 * @description
 * # QaListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QaListCtrl', function ($scope, apiService, ngTableParams, $filter) {
    $scope.qaList = new Array();

    $scope.deleteQA = function (qaId) {
      var deletePromise = apiService.deleteCatalogQa(qaId);

      deletePromise.then(
        function (payload) {
          return apiService.getStandardCatalogQas();
        }).then(function (payload) {
          $scope.qaList = payload.data;
        });
    }

    $scope.cloneQA = function (qaId) {
      var duplicatePromise = apiService.cloneCatalogQa(qaId);

      duplicatePromise.then(
        function (payload) {
          $scope.init();
        });
    }


    $scope.init = function () {
      var promiseInit = apiService.getStandardCatalogQas();

      promiseInit.then(
        function (payload) {
          $scope.qaList = payload.data;
          if ($scope.tableParams == undefined) {
            $scope.tableParams = new ngTableParams({
              page: 1,
              count: 10,
              sorting: {
                id: 'asc'
              }
            }, {
              total: function () {
                return $scope.qaList.length;
              },
              getData: function ($defer, params) {
                // use build-in angular filter
                var orderedData = params.sorting() ?
                  $filter('orderBy')($scope.qaList, params.orderBy()) :
                  $scope.qaList;
                params.total(orderedData.length);
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
              }
            });
          }
          else {
            $scope.tableParams.reload();
          }
        });
    }
  });
