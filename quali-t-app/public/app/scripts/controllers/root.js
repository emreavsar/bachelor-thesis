'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:RootCtrl
 * @description
 * # RootCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('RootCtrl', function ($scope, $state) {
    $scope.popover = {
      title: "Create new",
      buttons: [
        {
          title: "Project",
          clickFunction: function () {
            $state.go("newProject");
          },
          icon: "glyphicon glyphicon-folder-open"
        },
        {
          title: "Customer",
          clickFunction: function () {
            $state.go("customerCreate");
          },
          icon: "glyphicon glyphicon-user"
        },
        {
          title: "Catalog",
          clickFunction: function () {
            $state.go("newCatalog");
          },
          icon: "glyphicon glyphicon-list-alt"
        },
        {
          title: "Quality Attribute",
          clickFunction: function () {
            $state.go("newQA");
          },
          icon: "glyphicon glyphicon-grain"
        }
      ]
    };
  });
