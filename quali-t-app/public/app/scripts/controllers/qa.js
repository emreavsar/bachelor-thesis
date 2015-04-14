'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, $http, alerts) {
    $scope.qaText = "";

    $scope.createQA = function (qaText) {
      $http.post('/api/qa', {
        qaText: qaText
      }).
        success(function (data, status, headers, config) {
          $scope.qaText = "";
          var alert = alerts.createSuccess('QA created successfully', "#alerts-container");
        }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data, "#alerts-container");
        });
    }

    $http.get('/api/qa')
      .success(function (data) {
        $scope.qaList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
