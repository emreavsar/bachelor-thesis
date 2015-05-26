'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('NavigationCtrl', function ($scope, $state, $location) {
    $scope.customers = "";

    $scope.navigationItems = [
      {
        linkName: 'Home',
        toState: 'dashboard'
      },
      {
        linkName: 'Show Customer',
        toState: 'customer'
      },
      {
        linkName: 'Show QAs',
        toState: 'showQA'
      },
      {
        linkName: 'Show Catalogs',
        toState: 'showCatalogs'
      },
      {
        linkName: 'Show Projects',
        toState: 'showProjects'
      }

    ]

    $scope.isActive = function (toState) {
      return $state.href(toState).substr(1) === $location.path();
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
