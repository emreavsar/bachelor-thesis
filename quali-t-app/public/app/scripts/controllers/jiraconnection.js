'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:JiraconnectionCtrl
 * @description
 * # JiraconnectionCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('JiraconnectionCtrl', function ($scope, apiService, $modal, ngTableParams, $filter, alertService) {
    $scope.name = "";
    $scope.description = "";

    $scope.loadJIRAConnections = function () {
      var promise = apiService.loadJIRAConnections();
      promise.then(function (payload) {
        $scope.jiraConnectionList = payload.data;
        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'asc'
            }
          }, {
            total: function () {
              return $scope.jiraConnectionList.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.jiraConnectionList, params.orderBy()) :
                $scope.jiraConnectionList;
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

    $scope.delete = function (jiraconnection) {
      var conf = confirm('This will delete all connections inside projects to this JIRA connection, are you sure you want to do this?');
      if (conf) {
        var deletePromise = apiService.deleteJiraConnection(jiraconnection.id);
        deletePromise.then(function (payload) {
          $scope.jiraConnectionList = payload.data;
          alertService.createSuccess("JIRA connection successfully deleted");
          $scope.loadJIRAConnections();
        });
      }
    }

    $scope.editJIRAConnection = function (jiraconnection) {
      var editPromise = apiService.editJIRAConnection(jiraconnection.id, jiraconnection.hostAddress, jiraconnection.username, jiraconnection.password);

      editPromise.then(function (payload) {
        alertService.createSuccess("JIRA connection successfully updated");
        $scope.loadJIRAConnections();
      });
    }

    $scope.edit = function (jiraconnection) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.jiraconnection = jiraconnection;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editJIRAConnection;

      var modal = $modal({
        scope: modalScope,
        template: "templates/jiraconnection-modal.tpl.html"
      });
    }


    $scope.addJIRAConnection = function (jiraconnection) {
      var addPromise = apiService.addJIRAConnection(jiraconnection.hostAddress, jiraconnection.username, jiraconnection.password);
      addPromise.then(function (payload) {
        alertService.createSuccess("JIRA connection successfully added");
        $scope.loadJIRAConnections();
      });
    }

    $scope.add = function (jiraconnection) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.jiraconnection = jiraconnection;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.addJIRAConnection;

      var modal = $modal({
        scope: modalScope,
        template: "templates/jiraconnection-modal.tpl.html"
      });
    }

  });
