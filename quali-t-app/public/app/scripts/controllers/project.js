'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectCtrl
 * @description
 * # ProjectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectCtrl', function ($scope, $http) {

    $scope.createProject = function(name, customer) {
      // TODO implement me
      console.log("Data: " + name, customer);
    }

    $http.get('/api/customer')
      .success(function (data) {
      $scope.customerList = data;
    })
      .error(function (data, status) {
        console.log(status)
      });
  });
