'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:FavoritesCtrl
 * @description
 * # FavoritesCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('FavoritesCtrl', function ($scope, $state, apiService, $timeout) {
    $scope.favoriteProjects = new Array();
    $scope.isLoading = true;

    $scope.openFavorite = function (favoriteId) {
      $state.go('editProject', {
        projectId: favoriteId
      });
    }

    $timeout(function () {
      $scope.isLoading = false;
      var promise = apiService.getFavorites();
      promise.then(
        function (payload) {
          $scope.favoriteProjects = payload.data;
        });
    }, 2000);


    $scope.$on('favorites', function (event, arg) {
      $scope.favoriteProjects = arg;
    });
  });
