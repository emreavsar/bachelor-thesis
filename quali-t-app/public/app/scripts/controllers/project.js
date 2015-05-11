'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectCtrl
 * @description
 * # ProjectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectCtrl', function ($scope, $http, alerts, $state) {
    $scope.currentStep = 0;
    $scope.model = "Project";
    $scope.filteredCatalogQas = new Array();
    $scope.catalogQas = new Array();
    $scope.selection = new Array();
    $scope.selectionAdditional = new Array();
    $scope.newQaInEdit = false;
    $scope.name = "";
    $scope.customer = "";
    $scope.qpList = new Array();
    $scope.catalog = "";
    // local unbinded variable => used for not loading the same catalog twice (see loadQAs)
    $scope.selectedCatalog = "";
    $scope.variables = new Array();
    $scope.currentCategoriesFilter = new Array();
    $scope.selectedQualityProperties = new Array();
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


      $http.post('/api/project', {
        name: $scope.name,
        customer: $scope.selectedCustomer.id,
        qualityProperties: $scope.selectedQualityProperties,
        qualityAttributes: selectedCatalogQAs,
        additionalQualityAttributes: selectedAdditionalQas
      }).
        success(function (data, status, headers, config) {
          alerts.createSuccess("Your Project was created successfully.");
          $state.go('editProject', {
            projectId: data.id
          });
        }).
        error(function (data, status, headers, config) {
          alerts.createError(status, data);
        });
    }

    $scope.loadQAs = function (catalog, selectedCatalog) {
      if (selectedCatalog != catalog) {
        $scope.catalog = catalog;
        $scope.selectedCatalog = catalog;
        $http.get('/api/qa/catalog/' + catalog.id)
          .success(function (data) {
            $scope.catalogQas = new Array();
            $scope.variables = new Array();

            _.forEach(data, function (value, key) {
              $scope.catalogQas.push(value);
              $scope.variables[value.qa.id] = value.vars;
            });
          })
          .error(function (data, status) {
            alerts.createError(status, data);
          });
      }
    }

    $http.get('/api/qp')
      .success(function (data) {
        $scope.qpList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $http.get('/api/customer')
      .success(function (data) {
        $scope.customerList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $http.get('/api/catalog')
      .success(function (data) {
        $scope.catalogList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $http.get('/api/cat')
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

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
        if(!fullfiesFilter) {
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
  });
