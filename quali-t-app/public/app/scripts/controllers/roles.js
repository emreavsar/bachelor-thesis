'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:RolesCtrl
 * @description
 * # RolesCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('RolesCtrl', function ($scope, $rootScope) {
    $scope.tooltipsRoleChanger = "You can change your current role. " +
    "By changing the current role, the navigation will be adjusted, so that you can work more powerful.";

    $scope.changeRole = function (newRole) {
      $rootScope.selectedRole = newRole;
    }

    /**
     * Checks wether the role is available to change for the user, if not it has no sense to show it to the user
     * @param role
     * @returns {boolean}
     */
    $scope.isRoleAvailable = function (role) {
      return $rootScope._identity != undefined && $rootScope._identity.roles.indexOf(role) > -1;
    }
  });
