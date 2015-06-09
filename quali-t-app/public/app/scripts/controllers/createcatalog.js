'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CreateCatalogCtrl
 * @description
 * # CreateCatalogCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CreateCatalogCtrl', function ($scope, apiService, alertService, $state) {
    $scope.name = "";
    $scope.description = "";
    $scope.image = "";
    $scope.currentStep = 0;
    $scope.qas = new Array();
    //$scope.variables = new Array();
    $scope.selection = new Array();
    $scope.currentEditedElement = null;
    $scope.currentCategoriesFilter = new Array();


    $scope.nextStep = function () {
      var isLastStep = false;
      if ($scope.currentStep == 0) {
        $scope.choose($scope.name, $scope.image);
      } else if ($scope.currentStep == 1) {
        $scope.createCatalog();
        isLastStep = true;
      } else {
        $scope.currentStep = 0; // restart
      }
      if (!isLastStep) {
        $scope.currentStep++;
      }
    }

    $scope.back = function (currentStep) {
      if (currentStep == 1) {
        $scope.selection = new Array();
      }
      $scope.currentStep = currentStep - 1;
    }

    $scope.toggleSelection = function (qa) {
      var indexInArr = $scope.selection.indexOf(qa);
      if (indexInArr > -1) {
        $scope.selection.splice(indexInArr, 1);
      } else {
        $scope.selection.push(qa);
      }
    }


    $scope.switchCurrentEditedElement = function (qa) {
      $scope.currentEditedElement = qa;
    }

    $scope.choose = function (name, image) {
      // load all qas from standard catalog
      var promise = apiService.getStandardCatalogQas();
      promise.then(function (payload) {
        _.forEach(payload.data, function (value, key) {
          $scope.qas.push(value);
        });
      });
    }

    $scope.getSelectedQas = function () {
      var selectedQualityAttributes = new Array();
      _.forEach($scope.selection, function (value, key) {
        var qa = {
          id: value.qa.id,
          variables: value.vars
          //variables: $scope.variables[value.id]
        };
        selectedQualityAttributes.push(qa);
      });
      return selectedQualityAttributes;
    }

    $scope.createCatalog = function () {
      for (var i in $scope.selection) {
        delete $scope.selection[i].categories;
      }

      var createPromise = apiService.createCatalog($scope.getSelectedQas(), $scope.name, $scope.description, $scope.image);
      createPromise.then(function (payload) {
        alertService.createSuccess("Catalog " + payload.data.name + " created successfully.");
        $state.go('editCatalog', {
          catalogId: payload.data.id
        });
      });
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

    $scope.init = function () {
      var initPromise = apiService.getCategories();
      initPromise.then(function (payload) {
        $scope.catList = payload.data;
      });
    }


  });
