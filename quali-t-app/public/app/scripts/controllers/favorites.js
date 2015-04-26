'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:FavoritesCtrl
 * @description
 * # FavoritesCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('FavoritesCtrl', function ($scope, $http) {
    $scope.favoriteProjects = new Array();

    $scope.openFavorite = function (favoriteId) {
      console.log("going to open favorite with id: " + favoriteId);
    }

    $http.get("/api/myfavorites/")
      .success(function (data) {
        $scope.favoriteProjects = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

  });
