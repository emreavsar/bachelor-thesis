'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectInitiatorCtrl
 * @description
 * # ProjectInitiatorCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectInitiatorCtrl', function ($scope, apiService, $modal, ngTableParams, $filter) {
    $scope.name = "";
    $scope.address = "";
    $scope.errors = new Array();
    $scope.success = new Array()
    $scope.projectInitiatorList = new Array();

    $scope.createProjectInitiator = function (name, address) {
      $scope.errors = new Array();
      $scope.success = new Array();
      $scope.model = "ProjectInitiator";

      var createPromise = apiService.createProjectInitiator(name, address);
      createPromise.then(function (payload) {
        // todo add success alert
        $scope.loadProjectInitiators();
      });
    }

    $scope.loadProjectInitiators = function () {
      var loadPromise = apiService.getProjectInitiators();
      loadPromise.then(function (payload) {
        $scope.projectInitiatorList = payload.data;
        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'asc'
            }
          }, {
            total: function () {
              return $scope.projectInitiatorList.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.projectInitiatorList, params.orderBy()) :
                $scope.projectInitiatorList;
              params.total(orderedData.length);
              $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
          });
        }
        else {
          $scope.tableParams.reload();
        }
      });
    }


    $scope.delete = function (projectInitiator) {
      var conf = confirm('This will delete all projects etc. of the project initiator, u sure?');
      if (conf) {
        var deletePromise = apiService.deleteProjectInitiator(projectInitiator.id);
        deletePromise.then(function (payload) {
          $scope.projectInitiatorList = payload.data;
          $scope.loadProjectInitiators();
          // todo add success alert
        });
      }
    }

    $scope.editProjectInitiator = function (projectInitiator) {
      var updatePromise = apiService.updateProjectInitiator(projectInitiator.id, projectInitiator.name, projectInitiator.address);
      updatePromise.then(function (payload) {
        $scope.projectInitiatorList = payload.data;
        $scope.loadProjectInitiators();
        // todo add success alert
      });
    }

    $scope.edit = function (projectInitiator) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.projectInitiator = projectInitiator;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editProjectInitiator;

      var modal = $modal({
        scope: modalScope,
        template: "templates/projectInitiator-modal.tpl.html"
      });
    }

    $scope.add = function (projectInitiator) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.projectInitiator = projectInitiator;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.createProjectInitiator;

      var modal = $modal({
        scope: modalScope,
        template: "templates/projectInitiator-modal.tpl.html"
      });
    }

    $scope.init = function () {
      $scope.loadProjectInitiators();
    }
  })
;
