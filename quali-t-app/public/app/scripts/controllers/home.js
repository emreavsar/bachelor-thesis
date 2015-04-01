'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:HomeCtrl
 * @description
 * # HomeCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('HomeCtrl', function ($scope, $state) {
    $scope.login = function() {
      $state.go('login');
    };
  });
