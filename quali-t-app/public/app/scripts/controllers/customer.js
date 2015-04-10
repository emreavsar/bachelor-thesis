'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CustomerCtrl
 * @description
 * # CustomerCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CustomerCtrl', function ($scope, $http) {
    $scope.name = "";
    $scope.address = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.createCustomer = function (name, address) {
      $scope.errors = new Array();
      $scope.success = new Array();

      $scope.model = "Customer";
      $http.post('/api/customer', {
        name: name,
        address: address
      }).
        success(function(data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);
        }).
        error(function(data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }
    $http.get('/api/customer')
      .success(function (data) {
        $scope.customerList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

  });
