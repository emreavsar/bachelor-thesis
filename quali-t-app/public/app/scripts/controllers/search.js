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

    $scope.htmlizeResultsQA = function (qualityAttributes) {
      // TODO emre: find out how to render a view in the controller and get the html

      var html = "here come the quality attribues!";

      return html;
    };

    $scope.htmlizeResultsCatalog = function (catalogs) {
      // TODO emre: find out how to render a view in the controller and get the html

      var html = "here come the catalogs!";

      return html;
    };

    $scope.htmlizeResultsProject = function (projects) {
      // TODO emre: find out how to render a view in the controller and get the html

      var html = "here come the projects!";

      return html;
    };

    $scope.htmlizeResults = function (searchResults) {
      var html = "";

      html += $scope.htmlizeResultsQA(searchResults.qualityAttributes);
      html += $scope.htmlizeResultsCatalog(searchResults.catalogs);
      html += $scope.htmlizeResultsProject(searchResults.projects);

      return html;
    }

    $scope.search = function (searchArgument) {
      // reset
      $scope.searchResults = new Array();
      $scope.searchInProgress = true;

      // start search
      $http.get('/api/search/' + searchArgument)
        .success(function (data) {
          $scope.searchResults = data;
          $scope.searchInProgress = false;

          // if search results were found, open in modal view
          var modal = $modal({
            title: 'Search results for ' + searchArgument,
            content: $scope.htmlizeResults($scope.searchResults),
            template: "templates/modal.tpl.html",
            html: true
          });

        })
        .error(function (data, status) {
          console.log(status)
          $scope.searchInProgress = false;
        });
    }
  });
