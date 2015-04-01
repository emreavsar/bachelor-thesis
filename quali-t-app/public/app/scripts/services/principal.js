angular.module('qualitApp')
  .factory('principal', function ($q, $http, $rootScope, $cookies) {
    var _authenticated = false;
    var newIdentity = undefined;

    return {
      isIdentityResolved: function () {
        return angular.isDefined($rootScope._identity);
      },
      isAuthenticated: function () {
        return _authenticated;
      },
      isInRole: function (role) {
        if (!_authenticated || !$rootScope._identity.roles) return false;

        return $rootScope._identity.roles.indexOf(role) != -1;
      },
      isInAnyRole: function (roles) {
        if (!_authenticated || !$rootScope._identity.roles) return false;

        for (var i = 0; i < roles.length; i++) {
          if (this.isInRole(roles[i])) return true;
        }

        return false;
      },
      authenticate: function (identity) {
        $rootScope._identity = {
          username: identity.username,
          password: identity.password
        };
        _authenticated = identity.username != null;
      },
      identity: function (force) {
        var deferred = $q.defer();

        if (force === true) {
          $rootScope._identity = undefined;
        }

        $http.post('/api/auth/login', {
          username: $rootScope._identity.username,
          password: $rootScope._identity.password
        })
          .success(function (data) {
            var roles = new Array();
            $(data.roles).each(function (idx, role) {
              roles.push(role.name);
            });

            $rootScope._identity = {
              username: data.username,
              password: $rootScope._identity.password,
              roles: roles
            };
            $cookies.loggedInIdentity = JSON.stringify($rootScope._identity);
            newIdentity = $rootScope._identity;
            _authenticated = true;
            deferred.resolve(newIdentity);
          })
          .error(function () {
            $rootScope._identity = {
              username: null,
              password: null,
              roles: null
            };
            _authenticated = false;
            newIdentity = undefined;
            deferred.resolve(newIdentity);
            $rootScope.loginFailed = true;

          });

        return deferred.promise;
      }
    }
  });

