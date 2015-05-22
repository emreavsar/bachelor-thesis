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
          state: "settings",
          icon: "fa fa-cog"
        },
        {
          title: "Your Tasks",
          state: "mytasks",
          icon: "fa fa-tasks"
        },
        {
          title: "Logout",
          state: "logout",
          icon: "fa fa-sign-out"
        }
      ]
    };
  });
