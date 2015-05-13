'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:JiraconnectionCtrl
 * @description
 * # JiraconnectionCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('JiraconnectionCtrl', function ($scope, $http, $modal) {
    $scope.name = "";
    $scope.description = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadJIRAConnections = function () {
      $http.get('/api/jiraconnection')
        .success(function (data) {
          $scope.jiraConnectionList = data;
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.loadJIRAConnections();


    $scope.delete = function (jiraconnection) {
      var conf = confirm('This will delete all projects etc. of the qp, u sure?');
      if (conf) {
        alert("deleted" + jiraconnection.id);
      }
      $http.delete('/api/jiraconnection/' + jiraconnection.id)
        .success(function (data) {
          $scope.jiraConnectionList = data;
          $scope.loadJIRAConnections();
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.editJIRAConnection = function (jiraconnection) {
      $http.put('/api/jiraconnection', {
        id: jiraconnection.id,
        hostAddress: jiraconnection.hostAddress,
        username: jiraconnection.username,
        password: jiraconnection.password
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadJIRAConnections();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
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


      // in modal <button ng-click=callBackFunction(customer) />

    }


    $scope.addJIRAConnection = function (jiraconnection) {
      $http.post('/api/jiraconnection', {
        hostAddress: jiraconnection.hostAddress,
        username: jiraconnection.username,
        password: jiraconnection.password
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadJIRAConnections();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
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


      // in modal <button ng-click=callBackFunction(customer) />

    }

  });
