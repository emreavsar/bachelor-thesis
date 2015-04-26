'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:MytasksCtrl
 * @description
 * # MytasksCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('MytasksCtrl', function ($scope, $http, $rootScope) {
    $scope.tasks = new Array();

    $scope.toggleDoneState = function(taskId, currentState) {
      alert("not implemented yet");
      // TODO: emre implement this functionality here
    }

    $http.get('/api/mytasks/')
      .success(function (data) {
        $scope.tasks = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
