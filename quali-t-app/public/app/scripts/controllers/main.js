'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
  .controller('MainCtrl', function ($scope, $http) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    $scope.testBackendResponse='';

    $scope.testBackend = function() {
      $scope.testBackendResponse = {
        id: '',
        desc: ''
      }


      // TODO: refactor url
      $http.get('../api/get/nfr/1').
        success(function(data, status, headers, config) {
          //$scope.testBackendResponse = data;
          $scope.testBackendResponse.id = data.id;
          $scope.testBackendResponse.desc = data.desc;
        }).
        error(function(data, status, headers, config) {
          console.log(data);
          $scope.testBackendResponse = "error=" + data;
        });
    }
  });
