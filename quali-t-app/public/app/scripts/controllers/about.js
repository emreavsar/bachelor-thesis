'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
