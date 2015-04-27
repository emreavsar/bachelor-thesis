angular.module('qualitApp')
  .factory('authorization', function ($rootScope, $state, principal) {
    return {
      authorize: function () {
        if (principal.isAuthenticated()) {
          if ($rootScope.toState.data == undefined || $rootScope.toState.data.roles == undefined
            || $rootScope.toState.data.roles.length == 0 || principal.isInAnyRole($rootScope.toState.data.roles)) {
            return true;
          }
        }
        return principal.identity()
          .then(function () {
            var isAuthenticated = principal.isAuthenticated();
            if ($rootScope.toState.data != undefined && $rootScope.toState.data.roles
              && $rootScope.toState.data.roles.length > 0 && !principal.isInAnyRole($rootScope.toState.data.roles)) {
              if (isAuthenticated) {
                // not enough roles for the user
                $state.go('accessdenied');
              }
              else {
                // user not authenticated, save the state (to go to this state after successful login)
                $rootScope.returnToState = $rootScope.toState;
                $rootScope.returnToStateParams = $rootScope.toStateParams;
                // go to login, since not authenticated
                $state.go('login');
              }
            }
          });
      }
    };
  });
