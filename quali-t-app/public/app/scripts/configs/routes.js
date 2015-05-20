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
        templateUrl: 'views/errors/access_denied.html',
        url: '/authenticated/accessdenied'
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
        templateUrl: 'views/customer/admin.html',
        url: '/authenticated/customer/admin',
        controller: 'CustomerCtrl',
        data: {
          roles: ['curator', 'admin']
        }

      })
      .state('newQA', {
        templateUrl: 'views/qa/createEdit.html',
        url: '/authenticated/qa/create',
        controller: 'QACtrl',
        data: {
          roles: ['curator', 'admin']
        }
      })
      .state('editQA', {
        templateUrl: 'views/qa/createEdit.html',
        url: '/authenticated/qa/edit/:catalogQa',
        controller: 'QACtrl',
        data: {
          roles: ['analyst', 'admin']
        }
      })
      .state('showQA', {
        templateUrl: 'views/qa/list.html',
        url: '/authenticated/qa/list',
        controller: 'QaListCtrl',
        data: {
          roles: ['curator', 'admin']
        }
      })
      .state('newCatalog', {
        templateUrl: 'views/catalog/new.html',
        url: '/authenticated/catalog/create',
        controller: 'CatalogCtrl',
        data: {
          roles: ['curator', 'admin']
        }
      })
      .state('newProject', {
        templateUrl: 'views/project/new.html',
        url: '/authenticated/project/create',
        controller: 'ProjectCtrl',
        data: {
          roles: ['analyst', 'admin']
        }
      })
      .state('editProject', {
        templateUrl: 'views/project/edit.html',
        url: '/authenticated/project/edit/:projectId',
        controller: 'EditProjectCtrl',
        data: {
          roles: ['analyst', 'admin']
        }
      })
      .state('editCategories', {
        templateUrl: 'views/category/edit.html',
        url: '/authenticated/category/edit',
        controller: 'CategoryCtrl',
        data: {
          roles: ['curator', 'admin']
        }
      })
      .state('editQualityProperties', {
        templateUrl: 'views/qp/edit.html',
        url: '/authenticated/qp/edit',
        controller: 'QualitypropertyCtrl',
        data: {
          roles: ['curator', 'admin']
        }
      })
      .state('editJIRAConnections', {
        templateUrl: 'views/jiraconnection/edit.html',
        url: '/authenticated/jiraconnection/edit',
        controller: 'JiraconnectionCtrl',
        data: {
          roles: ['curator', 'synthesizer', 'projectmanager', 'analyst', 'admin']
        }
      })
  });
