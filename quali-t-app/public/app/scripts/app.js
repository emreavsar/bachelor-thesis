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
  'restangular'
])
  .run(['$rootScope', '$state', '$stateParams', 'authorization', 'principal', '$cookies', 'Restangular',
    function ($rootScope, $state, $stateParams, authorization, principal, $cookies, Restangular) {
      $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
        // track the state the user wants to go to; authorization service needs this
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;
        // if the principal is resolved, do an authorization check immediately. otherwise,
        // it'll be done when the state it resolved.
        if (toState.resolve != null && principal.isIdentityResolved()) {
          authorization.authorize();
        }
      });

      // make logout available from everywhere
      $rootScope.logout = function () {
        principal.authenticate({
          username: null,
          password: null,
          roles: null
        });
        $rootScope.loginFailed = false;
        $state.go('welcome');
      }

      // credentials inside cookie
      if($cookies.loggedInIdentity == null) {
        $cookies.loggedInIdentity = JSON.stringify({
          username: "",
          password: "",
          roles: null
        });
      }

      var tmpIdentity = JSON.parse($cookies.loggedInIdentity);
      var identityToSave = {
        username: "",
        password: "",
        roles: null
      }

      if (tmpIdentity != null) {
        identityToSave.username = tmpIdentity.username;
        identityToSave.password = tmpIdentity.password;
        identityToSave.roles = tmpIdentity.roles;
      }

      $rootScope._identity = identityToSave;
      // todo CHANGE TO EVENTS
      $rootScope.loginFailed = false;

      // init restangular
      // TODO maybe move it to somewhere else?
      Restangular.setDefaultRequestParams({apikey: "secret key"});
    }
  ]);
