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

    // TODO emre: this could be refactored maybe?

    apiService.register = function (username, password, passwordRepeated) {
      return $http.post('/api/auth/register', {
        username: username,
        password: password,
        passwordRepeated: passwordRepeated
      })
        .success(function (data) {
          $scope.registrationDone = true;
        }).error(function (data) {
          alerts.createError(status, data);
        });
    }

    apiService.changePassword = function (currentPassword, newPassword, newPasswordRepeated) {
      return $http.post(this.apiPath + "auth/changePw", {
        currentPassword: currentPassword,
        newPassword: newPassword,
        newPasswordRepeated: newPasswordRepeated
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alerts.createError(status, data);
      });
    }

    apiService.getFavorites = function () {
      return $http.get(this.apiPath + "myfavorites")
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

    apiService.deleteCategory = function (categoryId) {
      return $http.delete(this.apiPath + "cat/" + categoryId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.updateSubCategory = function (name, icon, id) {
      return $http.put(this.apiPath + "cat", {
        name: name,
        icon: icon,
        id: id
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alerts.createError(status, data);
      });
    }

    apiService.createSubCategory = function (name, icon, parent) {
      return $http.post(this.apiPath + "cat", {
        name: name,
        icon: icon,
        parent: parent
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
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

    apiService.createCustomer = function (name, address) {
      return $http.post(this.apiPath + "customer", {
        name: name,
        address: address
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Error at creating customer.");
        });
    }

    apiService.updateCustomer = function (id, name, address) {
      return $http.put(this.apiPath + "customer", {
        id: id,
        name: name,
        address: address
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.deleteCustomer = function (id) {
      return $http.delete(this.apiPath + "customer/" + id)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
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

    apiService.deleteQualityPropery = function (qpId) {
      return $http.delete(this.apiPath + "qp/" + qpId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Quality Properties were not found.");
        });
    }

    apiService.createQualityPropery = function (name, description) {
      return $http.post(this.apiPath + "qp", {
        name: name,
        description: description
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        alerts.createError(status, "Quality Properties were not found.");
      });
    }

    apiService.updateQualityPropery = function (id, name, description) {
      return $http.put(this.apiPath + "qp", {
        id: id,
        name: name,
        description: description
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
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

    apiService.cloneCatalogQa = function (catalogQaId) {
      return $http.post(this.apiPath + "/qa/clone/" + catalogQaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }
    apiService.removeQa = function (qaId) {
      return $http.delete(this.apiPath + "qa/" + qaId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.removeCatalogQa = function (catalogQaId) {
      return $http.delete(this.apiPath + "catalog/qa/" + catalogQaId)
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

    apiService.createCatalog = function (selectedQualityAttributes, name, description, image) {
      return $http.post(this.apiPath + "catalog", {
        selectedQualityAttributes: selectedQualityAttributes,
        name: name,
        description: description,
        image: image
      }).
        success(function (data, status, headers, config) {
          return data;
        }).
        error(function (data, status, headers, config) {
          alerts.createError(status, data);
        });
    }

    apiService.deleteCatalog = function (catalogId) {
      return $http.delete(this.apiPath + "catalog/" + catalogId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, "Catalog was not found.");
        });
    }


    apiService.updateCatalog = function (catalog) {
      return $http.put(this.apiPath + "catalog", catalog)
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

    apiService.getProjects = function () {
      return $http.get(this.apiPath + "project")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.deleteProject = function (projectId) {
      return $http.delete(this.apiPath + "project/" + projectId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.addCatalogQa = function (catalogId, qaId, variables) {
      return $http.post(this.apiPath + "catalog/qa", {
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

    apiService.updateQaFromCatalog = function (data) {
      return $http.put(this.apiPath + "catalog/qa", data)
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

    apiService.getCatalog = function (catalogId) {
      return $http.get(this.apiPath + "catalog/" + catalogId)
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

    apiService.getEditableCatalogs = function () {
      return $http.get(this.apiPath + "catalog/editable")
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

    apiService.exportToIssueTracker = function (projectId, selectedQas, exportAsRaw) {
      return $http.post(this.apiPath + "jira/export", {
        projectId: projectId,
        selectedQas: selectedQas,
        exportAsRaw: exportAsRaw
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

    apiService.getMyTasks = function () {
      return $http.get(this.apiPath + "mytasks")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          alerts.createError(status, data);
        });
    }

    apiService.toggleTask = function (taskId) {
      return $http.post(this.apiPath + "mytasks/toggle", {
        taskId: taskId
      }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.search = function (searchArgument) {
      return $http.get(this.apiPath + "search/" + searchArgument)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.loadJIRAConnections = function () {
      return $http.get(this.apiPath + "jiraconnection")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.deleteJiraConnection = function (jiraconnectionId) {
      return $http.delete(this.apiPath + "jiraconnection/" + jiraconnectionId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.addJIRAConnection = function (hostAddress, username, password) {

      return $http.post(this.apiPath + "jiraconnection", {
        hostAddress: hostAddress,
        username: username,
        password: password
      }).success(function (data) {
        return data;
      })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.editJIRAConnection = function (id, hostAddress, username, password) {
      return $http.put(this.apiPath + "jiraconnection", {
        id: id,
        hostAddress: hostAddress,
        username: username,
        password: password
      }).success(function (data) {
        return data;
      }).error(function (data, status) {
        var alert = alerts.createError(status, data);
      });
    }

    apiService.exportRessource = function (ressource, ressourceId, ressourceName, fileType) {
      return $http.get(this.apiPath + ressource + "/export/" + fileType + "/" + ressourceId, {responseType: 'arraybuffer'})
        .success(function (data) {
          // create a download link and click on it to download the file
          var downloadLink = document.createElement('a');
          downloadLink.href = window.URL.createObjectURL(new Blob([data]));
          var now = new Date();
          var todayUTCISO = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate())).toISOString();
          var todayStr = todayUTCISO.slice(0, 10).replace(/-/g, '');
          downloadLink.download = todayStr + "_QUALI-T_EXPORT_" + ressource.toUpperCase() + "_" + ressourceName + "." + fileType;
          downloadLink.click();

          return data;
        })
        .error(function (data, status) {
          // data is arraybuffer, so there must be conversion
          var alert = alerts.createError(status, String.fromCharCode.apply(null, new Uint8Array(data)));
        });
    }

    apiService.getUsers = function () {
      return $http.get(this.apiPath + "user")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.deleteUser = function (userId) {
      return $http.delete(this.apiPath + "user/" + userId)
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    apiService.updateUser = function (userId, username, selectedRoles) {
      return $http.put(this.apiPath + "user", {
        id: userId,
        username: username,
        selectedRoles: selectedRoles
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }
    apiService.createUser = function (username, password, selectedRoles) {
      return $http.post(this.apiPath + "user", {
        username: username,
        password: password,
        selectedRoles: selectedRoles
      })
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }
    apiService.getRoles = function () {
      return $http.get(this.apiPath + "roles")
        .success(function (data) {
          return data;
        })
        .error(function (data, status) {
          var alert = alerts.createError(status, data);
        });
    }

    return apiService;
  });
