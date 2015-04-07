'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope) {
    $scope.qaText = "";

    $scope.createQA = function(qaText) {
      // TODO implement me
      console.log("backend for create Quality Attribute not implemented yet");
    }
  });
