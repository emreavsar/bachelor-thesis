'use strict';

/**
 * @ngdoc service
 * @name qualitApp.apiService
 * @description
 * # apiService
 * Factory in the qualitApp.
 */

angular.module('qualitApp')
  .factory('apiService', function ($http, alertService, $rootScope, configService) {
    var apiService = {};

    apiService.register = function (username, password, passwordRepeated) {
      return $http.post(configService.apiPath + "auth/register", {
        username: username,
        password: password,
        passwordRepeated: passwordRepeated
      })
        .success(function (data) {
          $scope.registrationDone = true;
        }).error(function (data) {
          alertService.createError(status, data);
        });
    }

    apiService.changePassword = function (currentPassword, newPassword, newPasswordRepeated) {
      return $http.post(configService.apiPath + "auth/changePw", {
        currentPassword: currentPassword,
        newPassword: newPassword,
        newPasswordRepeated: newPasswordRepeated
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, data);
      });
    }

    apiService.getFavorites = function () {
      return $http.get(configService.apiPath + "myfavorites")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Favorites were not found.");
        });
    }

    apiService.getCategories = function () {
      return $http.get(configService.apiPath + "cat")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.deleteCategory = function (categoryId) {
      return $http.delete(configService.apiPath + "cat/" + categoryId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.updateSubCategory = function (name, icon, id) {
      return $http.put(configService.apiPath + "cat", {
        name: name,
        icon: icon,
        id: id
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, data);
      });
    }

    apiService.createSubCategory = function (name, icon, parent) {
      return $http.post(configService.apiPath + "cat", {
        name: name,
        icon: icon,
        parent: parent
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, data);
      });
    }

    apiService.createQa = function (data) {
      return $http.post(configService.apiPath + "qa", data, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, "Quality Attribute Template could not be created.");
      });
    }

    apiService.getProject = function (projectId) {
      return $http.get(configService.apiPath + "project/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Project was not found.");
        });
    }

    apiService.getProjectInitiators = function () {
      return $http.get(configService.apiPath + "projectInitiator")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Project Initiators were not found.");
        });
    }

    apiService.createProjectInitiator = function (name, address) {
      return $http.post(configService.apiPath + "projectInitiator", {
        name: name,
        address: address
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Error at creating project initiator.");
        });
    }

    apiService.updateProjectInitiator = function (id, name, address) {
      return $http.put(configService.apiPath + "projectInitiator", {
        id: id,
        name: name,
        address: address
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.deleteProjectInitiator = function (id) {
      return $http.delete(configService.apiPath + "projectInitiator/" + id)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getQualityProperties = function () {
      return $http.get(configService.apiPath + "qp")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Quality Properties were not found.");
        });
    }

    apiService.deleteQualityPropery = function (qpId) {
      return $http.delete(configService.apiPath + "qp/" + qpId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Quality Properties was not found and could not be deleted.");
        });
    }

    apiService.createQualityPropery = function (name, description) {
      return $http.post(configService.apiPath + "qp", {
        name: name,
        description: description
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, data);
      });
    }

    apiService.updateQualityPropery = function (id, name, description) {
      return $http.put(configService.apiPath + "qp", {
        id: id,
        name: name,
        description: description
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alertService.createError(status, data);
      });
    }

    apiService.changeState = function (projectId, isFavorite) {
      return $http.post(configService.apiPath + "myfavorites", {
        projectId: projectId,
        isFavorite: isFavorite
      })
        .success(function (data) {
          $rootScope.$broadcast('favoritesUpdated', data);
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Project favorite state coulnd't be changed.");
        });
    }

    apiService.updateProject = function (project) {
      return $http.put(configService.apiPath + "project", project)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.removeQaInstance = function (qaInstanceId) {
      return $http.delete(configService.apiPath + "project/qa/" + qaInstanceId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.cloneQaInstance = function (qaInstanceId) {
      return $http.post(configService.apiPath + "project/qa/clone/" + qaInstanceId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.updateQaInstance = function (qaInstance) {
      return $http.put(configService.apiPath + "project/qa", qaInstance)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.cloneCatalogQa = function (catalogQaId) {
      return $http.post(configService.apiPath + "/qa/clone/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }
    apiService.removeQa = function (qaId) {
      return $http.delete(configService.apiPath + "qa/" + qaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.removeCatalogQa = function (catalogQaId) {
      return $http.delete(configService.apiPath + "catalog/qa/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getStandardCatalogQas = function () {
      return $http.get(configService.apiPath + "qa/standardcatalog")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.createCatalog = function (selectedQualityAttributes, name, description, image) {
      return $http.post(configService.apiPath + "catalog", {
        selectedQualityAttributes: selectedQualityAttributes,
        name: name,
        description: description,
        image: image
      }).
        success(function (data, status, headers, config) {
          return data;
        }).
        error(function (data, status, headers, config) {
          alertService.createError(status, data);
        });
    }

    apiService.deleteCatalog = function (catalogId) {
      return $http.delete(configService.apiPath + "catalog/" + catalogId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, "Catalog was not found.");
        });
    }


    apiService.updateCatalog = function (catalog) {
      return $http.put(configService.apiPath + "catalog", catalog)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.deleteCatalogQa = function (catalogQaId) {
      return $http.delete(configService.apiPath + "catalog/qa/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.cloneCatalogQa = function (catalogQaId) {
      return $http.post(configService.apiPath + "qa/clone/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getCatalogQa = function (catalogQaId) {
      return $http.get(configService.apiPath + "catalog/qa/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getProjects = function () {
      return $http.get(configService.apiPath + "project")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.deleteProject = function (projectId) {
      return $http.delete(configService.apiPath + "project/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.addCatalogQa = function (catalogId, qaId, variables) {
      return $http.post(configService.apiPath + "catalog/qa", {
        qa: {
          id: qaId
        },
        catalogQa: {
          catalog: catalogId,
          variables: variables
        }
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.addAdditionalQaToProject = function (projectId, additionalQualityAttributes) {
      return $http.post(configService.apiPath + "project/qa", {
        id: projectId,
        additionalQualityAttributes: additionalQualityAttributes
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }
    apiService.addCatalogQaToProject = function (projectId, selectedCatalogQaIds) {
      return $http.post(configService.apiPath + "project/qa", {
        id: projectId,
        qualityAttributes: selectedCatalogQaIds
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }


    apiService.updateQa = function (data) {
      return $http.put(configService.apiPath + "qa", data)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.updateQaFromCatalog = function (data) {
      return $http.put(configService.apiPath + "catalog/qa", data)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getJiraInstances = function () {
      return $http.get(configService.apiPath + "jiraconnection")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getCatalog = function (catalogId) {
      return $http.get(configService.apiPath + "catalog/" + catalogId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getCatalogs = function () {
      return $http.get(configService.apiPath + "catalog")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getEditableCatalogs = function () {
      return $http.get(configService.apiPath + "catalog/editable")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getQAsOfCatalog = function (catalogId) {
      return $http.get(configService.apiPath + "qa/catalog/" + catalogId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }
    apiService.createProject = function (project) {
      return $http.post(configService.apiPath + "project", project)
        .success(function (data) {
          return data;
        }).error(function (data, status) {
          alertService.createError(status, "Error at creating project.");
        });
    }

    apiService.exportToIssueTracker = function (projectId, selectedQas, exportAsRaw) {
      return $http.post(configService.apiPath + "jira/export", {
        projectId: projectId,
        selectedQas: selectedQas,
        exportAsRaw: exportAsRaw
      })
        .success(function (data) {
          return data;
        }).error(function (data, status) {
          alertService.createError(status, "Error at exporting Quality Attributes to Issue Tracking System.");
        });
    }

    apiService.validateProjectQas = function (projectId) {
      return $http.get(configService.apiPath + "validate/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.getMyTasks = function () {
      return $http.get(configService.apiPath + "mytasks")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alertService.createError(status, data);
        });
    }

    apiService.toggleTask = function (taskId) {
      return $http.post(configService.apiPath + "mytasks/toggle", {
        taskId: taskId
      }).
        error(function (data, status, headers, config) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.search = function (searchArgument) {
      return $http.get(configService.apiPath + "search/" + searchArgument)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.loadJIRAConnections = function () {
      return $http.get(configService.apiPath + "jiraconnection")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.deleteJiraConnection = function (jiraconnectionId) {
      return $http.delete(configService.apiPath + "jiraconnection/" + jiraconnectionId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.addJIRAConnection = function (hostAddress, username, password) {

      return $http.post(configService.apiPath + "jiraconnection", {
        hostAddress: hostAddress,
        username: username,
        password: password
      }).success(function (data) {
        return data;
      })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.editJIRAConnection = function (id, hostAddress, username, password) {
      return $http.put(configService.apiPath + "jiraconnection", {
        id: id,
        hostAddress: hostAddress,
        username: username,
        password: password
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        var alert = alertService.createError(status, data);
      });
    }

    apiService.exportRessource = function (ressource, ressourceId, ressourceName, fileType) {
      return $http.get(configService.apiPath + ressource + "/export/" + fileType + "/" + ressourceId, {responseType: 'arraybuffer'})
        .success(function (data) {
          var now = new Date();
          var todayUTCISO = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate())).toISOString();
          var todayStr = todayUTCISO.slice(0, 10).replace(/-/g, '');
          var fileName = todayStr + "_QUALI-T_EXPORT_" + ressource.toUpperCase() + "_" + ressourceName + "." + fileType;
          saveAs(new Blob([data]), fileName);
          return data;
        })
        .error(function (data, status) {
          // data is arraybuffer, so there must be conversion
          var alert = alertService.createError(status, String.fromCharCode.apply(null, new Uint8Array(data)));
        });
    }

    apiService.getUsers = function () {
      return $http.get(configService.apiPath + "user")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.deleteUser = function (userId) {
      return $http.delete(configService.apiPath + "user/" + userId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    apiService.updateUser = function (userId, username, selectedRoles) {
      return $http.put(configService.apiPath + "user", {
        id: userId,
        username: username,
        selectedRoles: selectedRoles
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }
    apiService.createUser = function (username, password, selectedRoles) {
      return $http.post(configService.apiPath + "user", {
        username: username,
        password: password,
        selectedRoles: selectedRoles
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }
    apiService.getRoles = function () {
      return $http.get(configService.apiPath + "roles")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }
    apiService.importCatalog = function (catalogJson) {
      return $http.post(configService.apiPath + "catalog/import", catalogJson)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alertService.createError(status, data);
        });
    }

    return apiService;
  });
