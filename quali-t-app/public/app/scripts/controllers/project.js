'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectCtrl
 * @description
 * # ProjectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectCtrl', function ($scope, $http, $state) {
    $scope.stage = 1;
    $scope.model = "Project";
    $scope.qaList = new Array();

    $scope.createProject = function(name, customer) {
      console.log("Data: " + name, customer);
      $scope.errors = new Array();
      $scope.success = new Array();
      $scope.stage = 2;

    //  $http.post('/api/project', {
    //    qaText: qaText
    //  }).
    //    success(function(data, status, headers, config) {
    //      console.log(status + " data: " + data);
    //      $scope.success.push(data);
    //      $scope.stage = 2;
    //    }).
    //    error(function(data, status, headers, config) {
    //      console.log(status);
    //      $scope.errors.push(data);
    //    });
    //  console.log("QA created: ", qaText);
    }

    $scope.loadQAs = function (catalog) {
      console.log("Selected Catalog: " + catalog);
      $http.get('/api/qa/catalog='+ catalog)
        .success(function (data) {
          console.log("success" + data)
          $scope.qaList = data;
//          console.log(qaList)
        })
        .error(function (data, status) {
          console.log("error" + status)
        });

    }

    $http.get('/api/customer')
      .success(function (data) {
      $scope.customerList = data;
    })
      .error(function (data, status) {
        console.log(status)
      });

    $http.get('/api/catalog')
      .success(function (data) {
        $scope.catalogList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
