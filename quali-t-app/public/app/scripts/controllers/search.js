'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:SearchCtrl
 * @description
 * # SearchCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('SearchCtrl', function ($scope, apiService, $modal) {
    $scope.searchArgument = "";
    $scope.searchInProgress = false;
    $scope.searchResults = new Array();

    $scope.search = function (searchArgument) {
      // reset
      $scope.searchResults = new Array();
      $scope.searchInProgress = true;

      // start search
      var searchPromise = apiService.search(searchArgument);
      searchPromise.then(function (payload) {
        $scope.searchInProgress = false;
        $scope.searchResults = payload.data;

        // create new isolated scope for modal view
        var searchResultsScope = $scope.$new(true);
        searchResultsScope.searchResults = $scope.searchResults;

        var modal = $modal({
          scope: searchResultsScope,
          title: "Search results for " + searchArgument,
          template: "templates/searchresults-modal.tpl.html"
        });

      });
    }
  });
