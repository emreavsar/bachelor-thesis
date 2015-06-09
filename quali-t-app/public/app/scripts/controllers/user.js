'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:UserCtrl
 * @description
 * # UserCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('UserCtrl', function ($scope, $modal, apiService, ngTableParams, $filter, alertService, $rootScope, $state) {
    $scope.name = "";
    $scope.password = "";
    $scope.users = new Array();
    $scope.roles = new Array();
    $scope.tooltipsCurrentUserInfo = "You cannot change your own account in this view, otherwise you would logout yourself. " +
    "If you want to change your password, try it through your profile (navigation left, click on your login name)";

    $scope.loadUsers = function () {
      var loadPromise = apiService.getUsers();
      loadPromise.then(function (payload) {
        $scope.users = payload.data;

        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'asc'
            }
          }, {
            total: function () {
              return $scope.users.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.users, params.orderBy()) :
                $scope.users;
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

    /**
     * Checks wether the current user is edited/deleted
     * @param user
     */
    $scope.isCurrentUser = function (user) {
      return $rootScope._identity.userid == user.id;
    }

    $scope.loadRoles = function () {
      var loadPromise = apiService.getRoles();
      loadPromise.then(function (payload) {
        $scope.roles = payload.data;
      });
    }


    $scope.delete = function (user) {
      if ($scope.isCurrentUser(user)) {
        var conf = confirm("This is your user, are you sure you want to delete your own user?");
      } else {
        var conf = confirm('This will delete the user with his favorites and tasks. Are you sure, you want to do this?');
      }
      if (conf) {
        var deletePromise = apiService.deleteUser(user.id);
        deletePromise.then(function (payload) {
          alertService.createSuccess("User successfully deleted");
          if ($scope.isCurrentUser(user)) {
            $state.go("logout");
          } else {
            $scope.loadUsers();
          }
        });
      }
    }

    $scope.editUser = function (user) {
      var conf = true;
      if ($scope.isCurrentUser(user)) {
        var conf = confirm("This is your user, after changing your user, you have to login again. Do you want to do this?");
      }
      var selectedRoles = new Array();
      _.forEach(user.roles, function (n) {
        selectedRoles.push(n.id);
      });
      var updatePromise = apiService.updateUser(user.id, user.name, selectedRoles);

      if (conf) {

        updatePromise.then(function (payload) {
          alertService.createSuccess("User successfully updated");
          if ($scope.isCurrentUser(user)) {
            $state.go("logout");
          } else {
            $scope.loadUsers();
          }
        });
      }
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
      _.forEach(user.roles, function (n) {
        selectedRoles.push(n.id);
      });

      var createPromise = apiService.createUser(user.name, user.password, selectedRoles);

      createPromise.then(function (payload) {
        alertService.createSuccess("User successfully created");
        $scope.loadUsers();
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
