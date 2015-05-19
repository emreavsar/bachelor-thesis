'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectCtrl
 * @description
 * # ProjectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectCtrl', function ($scope, alerts, $state, apiService) {
    $scope.currentStep = 0;
    $scope.model = "Project";
    $scope.filteredCatalogQas = new Array();
    $scope.catalogQas = new Array();
    $scope.selection = new Array();
    $scope.selectionAdditional = new Array();
    $scope.newQaInEdit = false;
    $scope.name = "";
    $scope.jiraKey = "";
    $scope.customer = "";
    $scope.qpList = new Array();
    $scope.catalog = "";
    // local unbinded variable => used for not loading the same catalog twice (see loadQAs)
    $scope.selectedCatalog = "";
    $scope.currentCategoriesFilter = new Array();
    $scope.selectedQualityProperties = new Array();
    $scope.selectedJiraConnection = null;
    $scope.selectedCustomer = {};

    // qa's which are created on the fly
    $scope.additionalQas = new Array();

    $scope.newItem = {
      id: '',
      description: ''
    }

    $scope.addNew = function (newItem) {
      // TODO implement categories
      var qa = {
        description: newItem.description,
        categories: []
      }

      $scope.additionalQas.push(qa);
      $scope.toggleSelection(qa, $scope.selectionAdditional);

      $scope.newItem = {
        id: '',
        description: ''
      }
    }


    $scope.nextStep = function () {
      var isLastStep = false;
      if ($scope.currentStep == 0) {
        var errors = new Array();
        // check required fields
        if ($scope.name == "") {
          errors.push("You must set a project name.");
        }
        if ($scope.selectedQualityProperties.length == 0) {
          errors.push("You must select at least one quality property for a project.");
        }
        if ($scope.selectedCustomer.id == undefined) {
          errors.push("You must select the customer of the project.");
        }

        if (errors.length > 0) {
          var err = alerts.createLocalWarning(errors);
          return false;
        }
      }
      if ($scope.currentStep == 1) {
        $scope.create()
        isLastStep = true;
      } else {
        $scope.currentStep = 0; // restart
      }
      if (!isLastStep) {
        $scope.currentStep++;
      }
    }

    $scope.back = function (currentStep) {
      $scope.currentStep = currentStep - 1;
    }

    $scope.create = function () {

      var selectedCatalogQAs = new Array();
      var selectedAdditionalQas = new Array();

      for (var i in $scope.selection) {
        var qa = {
          // this is the id of catalogQA != id of id
          id: $scope.selection[i].id,
          description: $scope.selection[i].qa.description
        };
        selectedCatalogQAs.push(qa);
      }

      for (var i in $scope.selectionAdditional) {
        var qa = {
          id: $scope.selectionAdditional[i].id,
          description: $scope.selectionAdditional[i].description
        };
        selectedAdditionalQas.push(qa);
      }

      var promise = apiService.createProject({
        name: $scope.name,
        jiraKey: $scope.jiraKey,
        jiraConnection: $scope.selectedJiraConnection,
        customer: $scope.selectedCustomer.id,
        qualityProperties: $scope.selectedQualityProperties,
        qualityAttributes: selectedCatalogQAs,
        additionalQualityAttributes: selectedAdditionalQas
      });
      promise.then(function (payload) {
        alerts.createSuccess("Your Project was created successfully.");
        $state.go('editProject', {
          projectId: payload.data.id
        });
      });
    }

    $scope.loadQAs = function (catalog, selectedCatalog) {
      if (selectedCatalog != catalog) {
        $scope.catalog = catalog;
        $scope.selectedCatalog = catalog;
        var promise = apiService.getQAsOfCatalog(catalog.id);
        promise.then(function (payload) {
          $scope.catalogQas = payload.data;
        });
      }
    }

    $scope.filter = function (clickedElement, isRoot) {

      $scope.currentCategoriesFilter = new Array();
      var checkbox = $(clickedElement).find("input[type='checkbox']");

      // check for subcategories
      var children;
      if (isRoot) {
        children = $(clickedElement).parent().parent().parent().parent().find("input[type='checkbox']");
      } else {
        children = $(clickedElement).parent().parent().find("input[type='checkbox']");
      }

      if (checkbox.prop('checked')) {

        // check for subcategories
        $(children).each(function (index, val) {
          // first child is the clicked one, ignore this one
          if (index != 0) {
            $(this).prop('checked', 'checked');
          }
        })

      } else {
        $(children).each(function (index, val) {
          // first child is the clicked one, ignore this one
          if (index != 0) {
            $(this).prop('checked', '');
          }
        });
      }

      $("#filter input[type='checkbox']:checked").each(function () {
        $scope.currentCategoriesFilter.push($(this).data('id'));
      });

      $scope.$apply();
    }

    $scope.filterByCategories = function (catalogQa) {
      // TODO emre: some filter functions are in catalog.js and project.js maybe this could be refactored into filter.js
      // if there is a filter set
      if ($scope.currentCategoriesFilter.length > 0) {
        var categoryIds = $scope.categoryIdsOfQa(catalogQa.qa);
        var fullfiesFilter = false;
        for (var i = 0; i < $scope.currentCategoriesFilter.length; i++) {
          var categoryFilter = $scope.currentCategoriesFilter[i];

          for (var j = 0; j < categoryIds.length && !fullfiesFilter; j++) {
            if (categoryFilter == categoryIds[j]) {
              fullfiesFilter = true;
            }
          }
        }
        if (!fullfiesFilter) {
          var indexInArr = $scope.selection.indexOf(catalogQa);
          if (indexInArr > -1) {
            $scope.selection.splice(indexInArr, 1);
          }
        }
        return fullfiesFilter;
      } else {
        return true;
      }
    }

    $scope.categoryIdsOfQa = function (qa) {
      var ids = new Array();
      for (var i = 0; i < qa.categories.length; i++) {
        ids.push(qa.categories[i].id);
      }
      return ids;
    }


    $scope.toggleSelection = function (selectedObject, fromSelectionArray) {
      var indexInArr = fromSelectionArray.indexOf(selectedObject);
      if (indexInArr > -1) {
        fromSelectionArray.splice(indexInArr, 1);
      } else {
        fromSelectionArray.push(selectedObject);
      }
    }

    $scope.init = function () {
      var promiseInit = apiService.getQualityProperties();

      promiseInit.then(
        function (payload) {
          $scope.qpList = payload.data;
          return apiService.getCustomers();
        }).then(
        function (payload) {
          $scope.customerList = payload.data;
          return apiService.getCatalogs();
        }).then(
        function (payload) {
          $scope.catalogList = payload.data;
          return apiService.getCategories();
        }).then(
        function (payload) {
          $scope.catList = payload.data;
          return apiService.getJiraInstances();
        }).then(
        function (payload) {
          $scope.jiraInstances = payload.data;
        });
    }
  });
