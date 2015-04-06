'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('NavigationCtrl', function ($scope, $state, $location) {
    $scope.navigationItems = [
      {
        linkName: 'Home',
        toState: 'dashboard'
      },
      {
        linkName: 'Show Customer',
        toState: 'customer'
      },
      {
        linkName: 'Create Customer',
        toState: 'customerCreate'
      }
    ]

    $scope.isActive = function (toState) {
      return $state.href(toState).substr(1) === $location.path();
    }
  });
