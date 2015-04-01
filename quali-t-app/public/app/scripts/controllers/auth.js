'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:AuthCtrl
 * @description
 * # AuthCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('AuthCtrl', function ($scope, $location, $state, principal, $rootScope, $cookies, authValidationFactory, Restangular) {
    $scope.username = "";
    $scope.password = "";
    $scope.errors = new Array();

    $scope.login = function (username, password) {
      // save into rootscope
      $rootScope._identity = {
        username: username,
        password: password
      };

      if ($scope.returnToState) {
        $state.go($scope.returnToState.name, $scope.returnToStateParams);
      }
      else {
        $state.go('dashboard');
      }
    }

    $scope.register = function (username, password, passwordRepeated) {
      $scope.errors = new Array();
      var errors = authValidationFactory.validateRegistration(username, password, passwordRepeated);
      $scope.errors = errors;
      if (errors.length < 0) {
        // register at backend
      }
    }

    /**
     * When the user is logged in to the system, it does not make sense to present him the login screen again.
     * Instead show the dashboard.
     */
    $scope.checkAuthNeeded = function () {
      if (principal.isAuthenticated()) {
        $state.go('dashboard');
      }
    }
  });
