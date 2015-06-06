'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:MytasksCtrl
 * @description
 * # MytasksCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('MytasksCtrl', function ($scope, apiService, $rootScope) {
    $scope.tasks = new Array();

    $scope.toggleDoneState = function (taskId) {
      var togglePromise = apiService.toggleTask(taskId);
    }

    $scope.init = function () {
      var initPromise = apiService.getMyTasks();
      initPromise.then(function (payload) {
        $scope.tasks = payload.data;
      });
    }
  });
