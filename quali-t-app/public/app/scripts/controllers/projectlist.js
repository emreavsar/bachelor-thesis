'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectListCtrl
 * @description
 * # ProjectListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectListCtrl', function ($scope, apiService, alerts, ngParamService) {
    $scope.projects = new Array();


    $scope.deleteProject = function (projectId) {
      var deletePromise = apiService.deleteProject(projectId);

      deletePromise.then(
        function (payload) {
          $scope.init();
        });
    }

    $scope.init = function () {
      var initPromise = apiService.getProjects();
      initPromise.then(function (payload) {
        var data = payload.data;
        $scope.projects = data;
        $scope.tableParams = ngParamService.getDefaultNgParams(data);
      })
    }
  });
