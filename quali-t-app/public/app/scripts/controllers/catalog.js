'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CatalogCtrl
 * @description
 * # CatalogCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CatalogCtrl', function ($scope, $http, $alert) {
    $scope.name = "";
    $scope.image = "";
    $scope.currentStep = 0;
    $scope.qas = new Array();
    $scope.selection = new Array();
    $scope.currentEditedElement = null;
    $scope.newItem = null;
    $scope.currentCategoriesFilter = new Array();

    $http.get("/api/cat")
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $scope.nextStep = function () {
      var isLastStep = false;
      if ($scope.currentStep == 0) {
        $scope.choose($scope.name, $scope.image);
      } else if ($scope.currentStep == 1) {
//        $scope.customize();
        $scope.createCatalog();
        isLastStep = true;
      } else if ($scope.currentStep == 2) {
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
      if (currentStep == 2) {
        $scope.selection = $scope.selectionqp;
      }
      $scope.currentStep = currentStep - 1;
    }

    $scope.toggleSelection = function (qa) {
      console.log("toggleling the selection for qa with id: " + qa.id);
      var indexInArr = $scope.selection.indexOf(qa);
      if (indexInArr > -1) {
        console.log("qa with id=" + qa.id + " is selected, will be deselected now");
        $scope.selection.splice(indexInArr, 1);
      } else {
        console.log("qa with id=" + qa.id + " was not selected, will be added to selection now");
        $scope.selection.push(qa);
      }
    }

    $scope.toggleNewItem = function () {
      // create a new item
      if ($scope.newItem == null) {
        $scope.newItem = {
          id: '',
          description: '',
          categories: {}
        }

        $scope.currentEditedElement = $scope.newItem;

      } else {
        // close new template area
        $scope.newItem = null;
      }
    }

    $scope.addNew = function (newItem) {
      // TODO validation
      // TODO implement categories

      var qa = {
        description: newItem.description,
        categories: []
      }

      $scope.selection.push(qa);

      $scope.newItem = null;
    }

    $scope.switchCurrentEditedElement = function (qa) {
      $scope.currentEditedElement = qa;
    }

    $scope.choose = function (name, image) {
      // TODO emre: validation of first step's values -> if ok, go to next step

      // TODO emre: save the image somewhere localy / temporarly

      // load all qa-s
      $http.get('/api/qa')
        .success(function (data) {
          $scope.qas = data;
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.customize = function () {
      console.log("create is clicked");
    }

    $scope.createCatalog = function () {

      for (var i in $scope.selection) {
        delete $scope.selection[i].categories;
      }
      $http.post('/api/catalog', {
        qualityAttributes: $scope.selection,
        name: $scope.name,
        image: $scope.image
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);

          var alert = $alert({
            title: 'Congratulations!',
            content: 'Your Catalog was created successfully.',
            container: "#alerts-container",
            type: 'success',
            show: true
          });
        }).
        error(function (data, status, headers, config) {
          console.log(status);

          var alert = $alert({
            title: 'Oh no!',
            content: 'An error occured. TODO more specific information',
            'data-container': "#alerts-container",
            type: 'error',
            show: true
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

    $scope.filterByCategories = function (qa) {
      // if there is a filter set
      if ($scope.currentCategoriesFilter.length > 0) {
        var categoryIds = $scope.categoryIdsOfQa(qa);
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
  });
