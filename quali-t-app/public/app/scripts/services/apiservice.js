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

    apiService.getCategories = function () {
      return $http.get(this.apiPath + "cat")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.createQa = function (data) {
      return $http.post(this.apiPath + "qa", data, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alerts.createError(status, "Quality Attribute Template could not be created.");
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

    apiService.removeQaInstance = function (qaInstanceId) {
      return $http.delete(this.apiPath + "project/qa/" + qaInstanceId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.cloneQaInstance = function (qaInstanceId) {
      return $http.post(this.apiPath + "project/qa/clone/" + qaInstanceId)
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

    apiService.getStandardCatalogQas = function () {
      return $http.get(this.apiPath + "qa/standardcatalog")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.deleteCatalogQa = function (catalogQaId) {
      return $http.delete(this.apiPath + "catalog/qa/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.cloneCatalogQa = function (catalogQaId) {
      return $http.post(this.apiPath + "qa/clone/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.getCatalogQa = function (catalogQaId) {
      return $http.get(this.apiPath + "catalog/qa/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.updateQa = function (data) {
      return $http.put(this.apiPath + "qa", data)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.getJiraInstances = function () {
      return $http.get(this.apiPath + "jiraconnection")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.getCatalogs = function () {
      return $http.get(this.apiPath + "catalog")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }
    apiService.getQAsOfCatalog = function (catalogId) {
      return $http.get(this.apiPath + "qa/catalog/" + catalogId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }
    apiService.createProject = function (project) {
      return $http.post(this.apiPath + "project", project)
        .success(function (data) {
          return data;
        }).error(function (data, status) {
          alerts.createError(status, "Error at creating project.");
        });
    }

    apiService.exportToIssueTracker = function (projectId, selectedQas) {
      return $http.post(this.apiPath + "jira/export", {
        projectId: projectId,
        selectedQas: selectedQas
      })
        .success(function (data) {
          return data;
        }).error(function (data, status) {
          alerts.createError(status, "Error at exporting Quality Attributes to Issue Tracking System.");
        });
    }

    apiService.validateProjectQas = function (projectId) {
      return $http.get(this.apiPath + "validate/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }


    return apiService;
  });
