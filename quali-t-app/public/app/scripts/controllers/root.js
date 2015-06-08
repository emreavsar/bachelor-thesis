'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:RootCtrl
 * @description
 * # RootCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('RootCtrl', function ($scope, $state, $rootScope) {
    $scope.popover = {
      title: "Create new",
      buttons: [
        {
          title: "Project",
          clickFunction: function () {
            $state.go("newProject");
          },
          icon: "glyphicon glyphicon-folder-open",
          visibleFor: [
            'admin', 'projectmanager', 'analyst', 'synthesizer', 'evaluator'
          ]
        },
        {
          title: "Project Initiator",
          clickFunction: function () {
            $state.go("projectInitiatorCreate");
          },
          icon: "glyphicon glyphicon-user",
          visibleFor: [
            'admin', 'projectmanager', 'analyst', 'synthesizer', 'evaluator'
          ]
        },
        {
          title: "Catalog",
          clickFunction: function () {
            $state.go("newCatalog");
          },
          icon: "glyphicon glyphicon-list-alt",
          visibleFor: [
            'admin', 'evaluator'
          ]
        },
        {
          title: "Quality Attribute",
          clickFunction: function () {
            $state.go("newQA");
          },
          icon: "glyphicon glyphicon-grain",
          visibleFor: [
            'admin', 'projectmanager'
          ]
        }
      ]
    };

    $scope.isVisible = function (navItem) {
      return navItem.visibleFor.indexOf($rootScope.selectedRole) > -1;
    }

  });
