'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectListCtrl
 * @description
 * # ProjectListCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectListCtrl', function ($scope, apiService, ngTableParams, $filter) {
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
        $scope.projects = payload.data;
        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'asc'
            }
          }, {
            total: function () {
              return $scope.projects.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.projects, params.orderBy()) :
                $scope.projects;
              params.total(orderedData.length);
              $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
          });
        }
        else {
          $scope.tableParams.reload();
        }
      })
    }
  });
