'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('DashboardCtrl', function ($rootScope, $scope, $state, $location) {
    $scope.testBackendResponse = '';

    $scope.testBackend = function () {
      $scope.testBackendResponse = {
        id: '',
        desc: ''
      }
    }
  });
