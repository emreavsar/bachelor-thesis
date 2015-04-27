'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:SearchCtrl
 * @description
 * # SearchCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('SearchCtrl', function ($scope, $http, $modal) {
    $scope.searchArgument = "";
    $scope.searchInProgress = false;
    $scope.searchResults = new Array();

    $scope.search = function (searchArgument) {
      // reset
      $scope.searchResults = new Array();
      $scope.searchInProgress = true;

      // start search
      $http.get('/api/search/' + searchArgument)
        .success(function (data) {
          $scope.searchResults = data;
          $scope.searchInProgress = false;

          // create new isolated scope for modal view
          var searchResultsScope = $scope.$new(true);
          searchResultsScope.searchResults = $scope.searchResults;

          var modal = $modal({
            scope: searchResultsScope,
            title: "Search results for " + searchArgument,
            template: "templates/searchresults-modal.tpl.html"
          })
        })
        .error(function (data, status) {
          // TODO emre: add error handling
          console.log(status)
          $scope.searchInProgress = false;
        });
    }
  });
