'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CategoryCtrl
 * @description
 * # CategoryCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CategoryCtrl', function ($scope, $http) {
    this.errors = new Array();
    this.success = new Array();

    $http.get('/api/cat')
      .success(function (data) {
        this.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
