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
          userid: identity.id,
          username: identity.username,
          token: identity.token
        };
        _authenticated = identity.username != null;
      },
      identity: function (force) {
        var deferred = $q.defer();

        if (force === true) {
          $rootScope._identity = undefined;
        }


        if ($rootScope._identity.username != null && $cookies.loggedInIdentity != null && $cookies.loggedInIdentity != String(null)) {
          $rootScope._identity = JSON.parse($cookies.loggedInIdentity);
        }


        var authData = {
          username: $rootScope._identity.username
        };

        // attach either the token or the password
        if ($rootScope._identity.password == null || $rootScope._identity.password == "") {
          authData.password = "";
          authData.token = $rootScope._identity.token;
        } else {
          authData.password = $rootScope._identity.password;
        }

        $http.post('/api/auth/login', authData)
          .success(function (data) {
            // get the data
            var roles = new Array();
            $(data.user.roles).each(function (idx, role) {
              roles.push(role.name);
            });
            $rootScope._identity = {
              userid: data.user.id,
              username: $rootScope._identity.username,
              token: data.token,
              password: "", // remove the password
              roles: roles
            };

            // save the new data locally
            $cookies.loggedInIdentity = JSON.stringify($rootScope._identity);
            newIdentity = $rootScope._identity;
            _authenticated = true;
            deferred.resolve(newIdentity);
          })
          .error(function (data, status) {
            $rootScope._identity = {
              userid: null,
              username: null,
              token: null,
              roles: null
            };
            _authenticated = false;
            newIdentity = undefined;
            deferred.resolve(newIdentity);

            $cookies.loggedInIdentity = null;
            $rootScope.inAuthArea=false;

          });

        return deferred.promise;
      }
    }
  });

