'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:RootCtrl
 * @description
 * # RootCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('RootCtrl', function ($scope) {
    $scope.createModal = {
      title: "Create new",
      buttons: [
        {
          title: "Project",
          state: "newProject",
          icon: "glyphicon glyphicon-folder-open"
        },
        {
          title: "Customer",
          state: "customerCreate",
          icon: "glyphicon glyphicon-user"
        },
        {
          title: "Catalog",
          state: "newCatalog",
          icon: "glyphicon glyphicon-list-alt"
        },
        {
          title: "Quality Attribute",
          state: "newQA",
          icon: "glyphicon glyphicon-grain"
        }
      ]
    };
  });
