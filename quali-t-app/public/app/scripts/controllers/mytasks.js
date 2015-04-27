'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:MytasksCtrl
 * @description
 * # MytasksCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('MytasksCtrl', function ($scope, $http, $rootScope, alerts) {
    $scope.tasks = new Array();

    $scope.toggleDoneState = function (taskId) {
      $http.post('/api/mytasks/toggle/', {
        taskId: taskId
      }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data, "#alerts-container");
        });
    }

    $http.get('/api/mytasks/')
      .success(function (data) {
        $scope.tasks = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
