'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:UserCtrl
 * @description
 * # UserCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('UserCtrl', function ($scope, $modal, apiService) {
    $scope.name = "";
    $scope.password = "";
    $scope.users = new Array();
    $scope.roles = new Array();

    $scope.loadUsers = function () {
      var loadPromise = apiService.getUsers();
      loadPromise.then(function (payload) {
        $scope.users = payload.data;
      });
    }

    $scope.loadRoles = function () {
      var loadPromise = apiService.getRoles();
      loadPromise.then(function (payload) {
        $scope.roles = payload.data;
      });
    }


    $scope.delete = function (user) {
      var conf = confirm('This will delete the user with his favorites and tasks. Are you sure, you want to do this?');
      if (conf) {
        var deletePromise = apiService.deleteUser(user.id);
        deletePromise.then(function (payload) {
          $scope.loadUsers();
        });
      }
    }

    $scope.editUser = function (user) {
      var updatePromise = apiService.updateUser(user.id, user.name, $scope.selectedRoles);

      updatePromise.then(function (payload) {
        $scope.loadUsers();
        // todo add success alert
      });
    }

    $scope.edit = function (user) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.user = user;
      modalScope.roles = $scope.roles;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editUser;

      var modal = $modal({
        scope: modalScope,
        template: "templates/user-modal.tpl.html"
      });
    }

    $scope.addUser = function (user) {
      var selectedRoles = new Array();
      _.forEach($scope.selectedRoles, function (n) {
        selectedRoles.push(n.id);
      });

      var createPromise = apiService.createUser(user.name, user.password, selectedRoles);

      createPromise.then(function (payload) {
        $scope.loadUsers();
        // todo add success alert
      });
    }

    $scope.add = function (user) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.user = user;
      modalScope.roles = $scope.roles;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.addUser;

      var modal = $modal({
        scope: modalScope,
        template: "templates/user-modal.tpl.html"
      });
    }

    $scope.init = function () {
      $scope.loadUsers();
      $scope.loadRoles();
    }
  });
