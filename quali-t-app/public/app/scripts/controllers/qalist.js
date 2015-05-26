'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QaListCtrl
 * @description
 * # QaListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QaListCtrl', function ($scope, apiService, ngParamService) {
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
          return apiService.getStandardCatalogQas();
        }).then(function (payload) {
          $scope.qaList = payload.data;
        });
    }


    $scope.init = function () {
      var promiseInit = apiService.getStandardCatalogQas();

      promiseInit.then(
        function (payload) {
          var data = payload.data;
          $scope.qaList = data;
          $scope.tableParams = ngParamService.getDefaultNgParams(data);
        });
    }
  });
