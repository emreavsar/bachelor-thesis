'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CatalogCtrl
 * @description
 * # CatalogCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CatalogCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
