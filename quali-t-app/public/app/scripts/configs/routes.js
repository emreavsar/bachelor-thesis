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
        },
        onEnter: function ($rootScope) {
          $rootScope.logout();
        }
      })
      .state('successfulLogout', {
        url: '/logout',
        templateUrl: 'views/auth/logoutSuccessful.html'
      })
      .state('settings', {
        url: '/authenticated/profile/settings',
        controller: 'SettingsCtrl',
        templateUrl: 'views/profile/settings.html'
      })
      .state('mytasks', {
        url: '/authenticated/profile/mytasks',
        controller: 'MytasksCtrl',
        templateUrl: 'views/profile/mytasks.html'
      })
      .state('accessdenied', {
        templateUrl: 'views/errors/access_denied.html'
      })
      .state('dashboard', {
        url: '/authenticated/dashboard',
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
      .state('customer', {
        templateUrl: 'views/customer/show.html',
        url: '/authenticated/customer/view',
        controller: 'CustomerCtrl'
      })
      .state('customerCreate', {
        templateUrl: 'views/customer/new.html',
        url: '/authenticated/customer/create',
        controller: 'CustomerCtrl'
      })
      .state('newQA', {
        templateUrl: 'views/qa/new.html',
        url: '/authenticated/qa/create',
        controller: 'QACtrl'
      })
      .state('showQA', {
        templateUrl: 'views/qa/show.html',
        url: '/authenticated/qa/show',
        controller: 'QACtrl'
      })
      .state('newCatalog', {
        templateUrl: 'views/catalog/new.html',
        url: '/authenticated/catalog/create',
        controller: 'CatalogCtrl'
      })
      .state('newProject', {
	templateUrl: 'views/project/createProject.html',
        url: '/authenticated/project/create',
        controller: 'ProjectCtrl'
      })
  });
