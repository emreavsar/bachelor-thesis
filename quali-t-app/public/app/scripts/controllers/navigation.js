'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('NavigationCtrl', function ($scope, $state, $location, $rootScope) {
    $scope.customers = "";

    $scope.navigationItems = [
      {
        linkName: 'Home',
        toState: 'dashboard',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Show Customer',
        toState: 'customer',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Show QAs',
        toState: 'showQA',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Show Catalogs',
        toState: 'showCatalogs',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Create Catalog',
        toState: 'newCatalog',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Show Projects',
        toState: 'showProjects',
        visibleFor: [
          'admin', 'curator'
        ]
      },
      {
        linkName: 'Help',
        toState: 'help',
        visibleFor: [
          'curator', 'synthesizer', 'projectmanager', 'analyst', 'admin'
        ]
      }
    ]

    $scope.isActive = function (toState) {
      return $state.href(toState).substr(1) === $location.path();
    }

    $scope.isVisible = function(navItem) {
      return navItem.visibleFor.indexOf($rootScope.selectedRole) > -1;
    }


    $scope.popover = {
      title: "Your Profile",
      buttons: [
        {
          title: "Settings",
          clickFunction: function () {
            $state.go("settings");
          },
          icon: "fa fa-cog"
        },
        {
          title: "Your Tasks",
          clickFunction: function () {
            $state.go("mytasks");
          },
          icon: "fa fa-tasks"
        },
        {
          title: "Logout",
          clickFunction: function () {
            $state.go("logout");
          },
          icon: "fa fa-sign-out"
        }
      ]
    };
  });
