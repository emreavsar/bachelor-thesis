'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, $http) {
    $scope.qaText = "";

    $scope.createQA = function(qaText) {
      $scope.errors = new Array();
      $scope.success = new Array();

      $http.post('/api/qa', {
        qaText: qaText
      }).
        success(function(data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);
        }).
        error(function(data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
      console.log("QA created: ", qaText);
    }

    $http.get('/api/qa')
      .success(function (data) {
        $scope.qaList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
