'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:EditProjectCtrl
 * @description
 * # EditprojectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('EditProjectCtrl', function ($scope, $stateParams, $http, apiService, favoritesService) {
    $scope.projectId = $stateParams.projectId;
    $scope.project = {};
    $scope.favoriteProjects = new Array();
    $scope.selectedCustomer = {};
    $scope.customerList = new Array();
    $scope.selectedQualityProperties = new Array();
    $scope.qualityPropertiesList = new Array();
    $scope.isProjectFavorite = false;

    $scope.checkIsFavorite = function (projectId, favoriteProjects) {
      return favoritesService.isProjectFavorite(projectId, favoriteProjects);
    }

    $scope.addToFavorites = function (projectId) {
      $scope.changeState(projectId, true);
    }

    $scope.removeFromFavorites = function (projectId) {
      $scope.changeState(projectId, false);
    }

    $scope.changeState = function (projectId, isFavorite) {
      var promiseQualityProperties = apiService.changeState(projectId, isFavorite);
      promiseQualityProperties.then(
        function (payload) {
          $scope.favoriteProjects = payload.data.favorites;
          $scope.isProjectFavorite = $scope.checkIsFavorite($scope.projectId, $scope.favoriteProjects);
        });
    }

    $scope.init = function () {
      var promiseInit = apiService.getFavorites();
      promiseInit.then(
        function (payload) {
          $scope.favoriteProjects = payload.data;
          return apiService.getProject($scope.projectId);
        }).then(
        function (payload) {
          $scope.project = payload.data;
          $scope.selectedCustomer = $scope.project.projectCustomer;
          $scope.selectedQualityProperties = $scope.project.qualityProperties;
          $scope.isProjectFavorite = $scope.checkIsFavorite($scope.projectId, $scope.favoriteProjects);
          return apiService.getCustomers();
        }).then(
        function (payload) {
          $scope.customerList = payload.data;
          return apiService.getQualityProperties();
        }).then(
        function (payload) {
          $scope.qualityPropertiesList = payload.data;
        }
      );
    }
  });
