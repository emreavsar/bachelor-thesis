'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:EditProjectCtrl
 * @description
 * # EditprojectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('EditProjectCtrl', function ($scope, $state, $stateParams, apiService, favoritesService, alerts, $popover) {
    $scope.projectId = $stateParams.projectId;
    $scope.project = {};
    $scope.favoriteProjects = new Array();
    $scope.selectedCustomer = {};
    $scope.customerList = new Array();
    $scope.selectedQualityProperties = new Array();
    $scope.qualityPropertiesList = new Array();
    $scope.qualityAttributesToUpdate = new Array();
    $scope.jiraConnections = new Array();
    $scope.isProjectFavorite = false;
    $scope.exportAsRawModel = {
      value: false
    };
    $scope.tooltipsSave = "Saves the project and shows warnings (statistics & fuzzyness) if there are any.";
    $scope.tooltipsValidate = "Validate the project's quality attributes for statistics and " +
    "fuzzyness will show you open issues and suggesstions.";
    $scope.tooltipsExportToIssueTracker = "Export selected quality attributes to issue tracking system.";
    $scope.tooltipsExport = "Select the quality attributes you want to export to issue tracking system.";
    $scope.tooltipsExportRaw = "JIRA does not understand HTML as QUALI-T does. You can choose between exporting in " +
    "raw format (remove html formatting) or not (keep html formatting).";
    $scope.tooltipsExportJiraKey = "Put the project key of the project you want to export in the quality attributes." +
    " The project key is the first part of the string of the JIRA issue IDs. " +
    "For example: Project Key for the JIRA: QUALI-123 would be QUALI.";
    $scope.hasValidationWarnings = null;

    $scope.popover = {
      title: "Export this project as",
      buttons: [
        {
          title: "PDF",
          clickFunction: function () {
            $scope.export("pdf");
          },
          icon: "fa fa-file-pdf-o"
        },
        {
          title: "XML",
          clickFunction: function () {
            $scope.export("xml");
          },
          icon: "fa fa-file-code-o"
        }
      ]
    };

    $scope.export = function (type) {
      var exportPromise = apiService.exportProject($stateParams.projectId, type);
      exportPromise.then(function (payload) {
        // create a download link and click on it to download the file
        var downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(new Blob([payload.data]));
        var now = new Date();
        var todayUTCISO = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate())).toISOString();
        var todayStr = todayUTCISO.slice(0, 10).replace(/-/g, '');
        downloadLink.download = "QUALI-T_EXPORT_" + todayStr + "_project_" + $scope.project.name + "." + type;
        downloadLink.click();
      });
    }


    $scope.checkIsFavorite = function (projectId, favoriteProjects) {
      return favoritesService.isProjectFavorite(projectId, favoriteProjects);
    }

    $scope.addToFavorites = function (projectId) {
      $scope.changeState(projectId, true);
    }

    $scope.removeFromFavorites = function (projectId) {
      $scope.changeState(projectId, false);
    }

    $scope.changeState = function (projectId, isFavorite) {
      var promiseQualityProperties = apiService.changeState(projectId, isFavorite);
      promiseQualityProperties.then(
        function (payload) {
          $scope.favoriteProjects = payload.data.favorites;
          $scope.isProjectFavorite = $scope.checkIsFavorite($scope.projectId, $scope.favoriteProjects);
        });
    }

    $scope.addNewQas = function () {
      // TODO
    }

    $scope.toggleQpStatus = function (qaId, qpId, isChecked) {
      var qa = _.filter($scope.project.qualityAttributes, {id: qaId})[0];

      // search for the quality property, and replace status
      _.forEach(qa.qualityPropertyStatus, function (n, key) {
        if (n.id == qpId) {
          n.status = isChecked;
        }

        // any change needs to be persisted, save temporarly in array
        var index = $scope.qualityAttributesToUpdate.indexOf(qa);
        if (index != -1) { // replace if two changes on same object was done
          $scope.qualityAttributesToUpdate.splice(index, 1);
        }
        $scope.qualityAttributesToUpdate.push(qa);
      });
    }

    /**
     * Helper function. Prepares the quality attributes to send to backend
     * @returns {Array}
     */
    $scope.getQAToUpdate = function () {
      var qasToUpdate = new Array();

      _.forEach($scope.qualityAttributesToUpdate, function (n) {
        // cleanup
        delete n["template"];
        delete n["values"];
        qasToUpdate.push(n);
      });

      return qasToUpdate;
    }

    $scope.save = function () {
      // get only needed information
      var project = {
        id: $scope.project.id,
        name: $scope.project.name,
        jiraKey: $scope.project.jiraKey,
        jiraConnection: $scope.project.jiraConnection,
        customer: $scope.selectedCustomer.id,
        qualityProperties: $scope.selectedQualityProperties,
        qualityAttributes: $scope.getQAToUpdate()
      };

      var promiseSave = apiService.updateProject(project);
      promiseSave.then(
        function (payload) {
          alerts.createSuccess("Project was successfully updated.");
          $scope.qualityAttributesToUpdate = new Array();
          $scope.project = payload.data;
        });
    }

    $scope.validate = function () {
      $(".qa .validation-warnings").addClass("hidden");
      var promiseValidate = apiService.validateProjectQas($scope.project.id);
      promiseValidate.then(
        function (payload) {
          var validationWarnings = payload.data;
          $scope.hasValidationWarnings = !_.isEmpty(validationWarnings);

          _.forEach(validationWarnings, function (n, key) {
            var warningScope = $scope.$new(true);
            warningScope.title = "Validation Errors:";
            warningScope.warnings = n;

            $popover($("#qa-" + key + " .validation-warnings"), {
              scope: warningScope,
              trigger: 'click',
              placement: 'bottom',
              template: "templates/warning-popover.tpl.html",
              autoClose: true
            });

            $("#qa-" + key + " .validation-warnings").removeClass("hidden");
          });
        });

    }

    $scope.exportToIssueTracker = function () {
      var selectedQaIds = new Array();
      $(".export-checkbox:checked").each(function () {
        selectedQaIds.push($(this).data('qainstanceid'));
      });
      selectedQaIds;

      var promiseExport = apiService.exportToIssueTracker($scope.project.id, selectedQaIds, $scope.exportAsRawModel.value);

      promiseExport.then(
        function (payload) {
          var reloadPromise = apiService.getProject($scope.projectId);
          reloadPromise.then(function (payload) {
            $scope.setProject(payload);
          })

        });
    }

    $scope.setProject = function (payload) {
      $scope.project = payload.data;
      $scope.selectedCustomer = $scope.project.projectCustomer;
      $scope.selectedQualityProperties = $scope.project.qualityProperties;
      $scope.isProjectFavorite = $scope.checkIsFavorite($scope.projectId, $scope.favoriteProjects);

      // sort by qp id
      for (var i = 0; i < $scope.project.qualityAttributes.length; i++) {
        var qa = $scope.project.qualityAttributes[i];

        qa = _.sortBy(qa.qualityPropertyStatus, function (n) {
          return n.id;
        });

      }
    }

    $scope.init = function () {
      var promiseInit = apiService.getFavorites();
      promiseInit.then(
        function (payload) {
          $scope.favoriteProjects = payload.data;
          return apiService.getProject($scope.projectId);
        }).then(
        function (payload) {
          $scope.setProject(payload);

          return apiService.getCustomers();
        }).then(
        function (payload) {
          $scope.customerList = payload.data;
          return apiService.getQualityProperties();
        }).then(
        function (payload) {
          $scope.qualityPropertiesList = payload.data;
          return apiService.getJiraInstances();
        }).then(
        function (payload) {
          $scope.jiraConnections = payload.data;
        }
      );
    }
  }
)
;
