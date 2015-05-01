'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, $http, alerts) {
    $scope.qaText = "";

    $scope.selectedCategories = new Array();

    $scope.getSelectedCategories = function (clickedElement) {
      $scope.selectedCategories = new Array();
      $("#categories input[type='checkbox']:checked").each(function () {
        $scope.selectedCategories.push({id: $(this).data('id')});
      });
    }

    $http.get("/api/cat")
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $scope.createQA = function (qaText) {
      $scope.getSelectedCategories();
      $http.post('/api/qa', {
        qaText: qaText,
        categories: $scope.selectedCategories
      }, {headers: {'Content-Type': 'application/json'}}).
        success(function (data, status, headers, config) {
          $scope.qaText = "";
          var alert = alerts.createSuccess('QA created successfully', "#alerts-container");
        }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data, "#alerts-container");
        });
    }

    $http.get('/api/qa')
      .success(function (data) {
        $scope.qaList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
