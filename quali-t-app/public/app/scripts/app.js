'use strict';

/**
 * @ngdoc overview
 * @name qualitApp
 * @description
 * # qualitApp
 *
 * Main module of the application.
 */
angular.module('qualitApp', [
  'ngAnimate',
  'ngCookies',
  'ngResource',
  'ngRoute',
  'ngSanitize',
  'ngTouch',
  'angular-loading-bar',
  'ui.router',
  'mgcrea.ngStrap',
  'ui.gravatar',
  'textAngular',
  'ui.select',
  'frapontillo.bootstrap-switch',
  'ngTable'
])
  .run(['$rootScope', '$http', '$state', '$stateParams', 'authorization', 'principal', '$cookies',
    function ($rootScope, $http, $state, $stateParams, authorization, principal, $cookies) {
      $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
        // track the state the user wants to go to; authorization service needs this
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;

        if (principal.isIdentityResolved()) {
          authorization.authorize();
        }


        // in index.html there are some special behaviour if the user is authenticated
        // for example hiding the sidebar (which is only available when authenticated)
        $rootScope.inAuthArea = toState.url.indexOf('/authenticated/') > -1;

      });

      // make logout available from everywhere
      $rootScope.logout = function () {
        // if already logged out, no need to logout again
        if (!$rootScope.inAuthArea) {
          $state.go("welcome");
        }
        principal.authenticate({
          userid: null,
          username: null,
          password: null,
          token: null,
          roles: null
        });
        $rootScope.loginFailed = false;

        // invalidate session on server side
        var loggedInUser = JSON.parse($cookies.loggedInIdentity);
        $http.post('/api/auth/logout', {
          username: loggedInUser.username,
          token: loggedInUser.token
        })
          .success(function (data) {
            $cookies.loggedInIdentity = null;
            $state.go('successfulLogout');
          });
      }

      // credentials inside cookie
      var tmpIdentity = null;
      if ($cookies.loggedInIdentity != null) {
        tmpIdentity = JSON.parse($cookies.loggedInIdentity);
      }

      var identityToSave = {
        userid: "",
        username: "",
        password: "",
        roles: null
      }

      if (tmpIdentity != null) {
        identityToSave.userid = tmpIdentity.userid;
        identityToSave.username = tmpIdentity.username;
        identityToSave.password = tmpIdentity.password;
        identityToSave.roles = tmpIdentity.roles;
      }

      $rootScope._identity = identityToSave;
      // todo CHANGE TO EVENTS
      $rootScope.loginFailed = false;

      $rootScope.currentEditedQa = {
        id: "",
        description: ""
      };
    }
  ]);
