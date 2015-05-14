'use strict';

/**
 * @ngdoc service
 * @name qualitApp.apiService
 * @description
 * # apiService
 * Factory in the qualitApp.
 */

angular.module('qualitApp')
  .factory('apiService', function ($http, alerts, $rootScope) {
    var apiService = {};
    apiService.apiPath = "/api/";

    apiService.getFavorites = function () {
      return $http.get(this.apiPath + "myfavorites/")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Favorites were not found.");
        });
    }

    apiService.getProject = function (projectId) {
      return $http.get(this.apiPath + "project/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Project was not found.");
        });
    }

    apiService.getCustomers = function () {
      return $http.get(this.apiPath + "customer")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Customers were not found.");
        });
    }

    apiService.getQualityProperties = function () {
      return $http.get(this.apiPath + "qp")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Quality Properties were not found.");
        });
    }

    apiService.changeState = function (projectId, isFavorite) {
      return $http.post(this.apiPath + "myfavorites", {
        projectId: projectId,
        isFavorite: isFavorite
      })
        .success(function (data) {
          $rootScope.$broadcast('favoritesUpdated', data);
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Project favorite state coulnd't be changed.");
        });
    }

    apiService.updateProject = function (project) {
      return $http.put(this.apiPath + "project", project)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.removeQaInstance = function (qaId) {
      return $http.delete(this.apiPath + "project/qa/" + qaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.updateQaInstance = function (qaInstance) {
      return $http.put(this.apiPath + "project/qa", qaInstance)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    return apiService;
  });
