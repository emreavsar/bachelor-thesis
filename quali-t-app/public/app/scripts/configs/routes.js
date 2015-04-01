angular.module('qualitApp')
  .config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/welcome');

    $stateProvider
      .state('welcome', {
        templateUrl: 'views/home/welcome.html',
        url: '/welcome',
        controller: 'HomeCtrl'
      })
      .state('login', {
        templateUrl: 'views/auth/login.html',
        url: '/login',
        controller: 'AuthCtrl'
      })
      .state('register', {
        templateUrl: 'views/auth/register.html',
        url: '/register',
        controller: 'AuthCtrl'
      })
      .state('resetPassword', {
        templateUrl: 'views/auth/resetPassword.html',
        url: '/resetPassword',
        controller: 'AuthCtrl'
      })
      .state('logout', {
        url: '/logout',
        controller: 'AuthCtrl',
        data: {
          roles: ['logout']
        }
      })
      .state('accessdenied', {
        templateUrl: 'views/errors/access_denied.html'
      })
      .state('dashboard', {
        url: '/dashboard',
        controller: 'DashboardCtrl',
        templateUrl: 'views/dashboard.html',
        resolve: {
          authorize: ['authorization',
            function (authorization) {
              return authorization.authorize();
            }
          ]
        },
        data: {
          roles: ['dashboard']
        }
      })
  });
