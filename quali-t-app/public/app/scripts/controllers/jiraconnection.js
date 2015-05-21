'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:JiraconnectionCtrl
 * @description
 * # JiraconnectionCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('JiraconnectionCtrl', function ($scope, apiService, $modal) {
    $scope.name = "";
    $scope.description = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadJIRAConnections = function () {
      var promise = apiService.loadJIRAConnections();
      promise.then(function (payload) {
        $scope.jiraConnectionList = payload.data;
      });
    }

    $scope.delete = function (jiraconnection) {
      var conf = confirm('This will delete all projects etc. of the qp, u sure?');
      if (conf) {
        var deletePromise = apiService.deleteJiraConnection(jiraconnection.id);
        deletePromise.then(function (payload) {
          $scope.jiraConnectionList = payload.data;
          // todo: is this needed?
          $scope.loadJIRAConnections();
        });
      }
    }

    $scope.editJIRAConnection = function (jiraconnection) {
      var editPromise = apiService.editJIRAConnection(jiraconnection.id, jiraconnection.hostAddress, jiraconnection.username, jiraconnection.password);

      editPromise.then(function (payload) {
        $scope.loadJIRAConnections();
        $scope.success.push(payload.data);
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
        $scope.loadJIRAConnections();
        $scope.success.push(payload.data);
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
