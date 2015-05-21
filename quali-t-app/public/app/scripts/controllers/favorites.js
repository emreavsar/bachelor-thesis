'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:FavoritesCtrl
 * @description
 * # FavoritesCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('FavoritesCtrl', function ($scope, $state, apiService, alerts) {
    $scope.favoriteProjects = new Array();

    $scope.openFavorite = function (favoriteId) {
      $state.go('editProject', {
        projectId: favoriteId
      });
    }

    var promise = apiService.getFavorites();
    promise.then(
      function (payload) {
        $scope.favoriteProjects = payload.data;
      });
  });
