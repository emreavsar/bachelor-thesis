'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:EditQaCtrl
 * @description
 * # EditQaCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('EditQaCtrl', function ($scope) {

    /**
     * Helper function. Workaround to hide modal through controller.
     * @param hideFunction
     */
    $scope.bindHide = function (hideFunction) {
      $scope.hideModal = hideFunction;
    }

    $scope.update = function() {
      alert("todo");
      $scope.hideModal();
    }
  });
