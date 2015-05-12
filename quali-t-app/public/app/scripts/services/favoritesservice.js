'use strict';

/**
 * @ngdoc service
 * @name qualitApp.favoritesService
 * @description
 * # favoritesService
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('favoritesService', function () {
    var favoritesService = {};

    favoritesService.isProjectFavorite = function (projectId, favorites) {
      if (projectId != "" && favorites.length != 0) {
        var matchingFavorites = _.find(favorites, function (favorite) {
          return favorite.id == projectId;
        });
        return matchingFavorites != undefined;
      } else {
        return false;
      }
    }

    return favoritesService;
  });
