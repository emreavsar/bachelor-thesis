'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('DashboardCtrl', function ($rootScope, $scope, $http, $state, $location) {
    $scope.testBackendResponse='';

    $scope.testBackend = function() {
      $scope.testBackendResponse = {
        id: '',
        desc: ''
      }

      // TODO: refactor url
      $http.get('/api/get/nfr/first', {
        username: $rootScope.username
      }).
        success(function(data, status, headers, config) {
          //$scope.testBackendResponse = data;
          $scope.testBackendResponse.id = data.id;
          $scope.testBackendResponse.description = data.description;
        }).
        error(function(data, status, headers, config) {
          console.log(data);
          $scope.testBackendResponse = "error=" + data;
        });
    }
  });
