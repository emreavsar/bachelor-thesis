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

    $scope.loadUsers= function () {
      var loadPromise = apiService.getUsers();
      loadPromise.then(function (payload) {
        $scope.users = payload.data;
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
      var updatePromise = apiService.updateUser(user.id, user.name, user.selectedRoles);

      updatePromise.then(function (payload) {
        $scope.loadUsers();
        $scope.success.push(payload.data);
        // todo add success alert
      });
    }

    $scope.edit = function (user) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.user = user;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editUser;

      var modal = $modal({
        scope: modalScope,
        template: "templates/user-modal.tpl.html"
      });
    }

    $scope.addUser = function (user) {
      var createPromise = apiService.createUser(user.name, user.password, user.selectedRoles);

      createPromise.then(function (payload) {
        $scope.loadUsers();
        // todo add success alert
      });
    }

    $scope.add = function (user) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.user = user;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.addUser;

      var modal = $modal({
        scope: modalScope,
        template: "templates/user-modal.tpl.html"
      });
    }

    $scope.init = function () {
      $scope.loadUsers();
    }
  });
