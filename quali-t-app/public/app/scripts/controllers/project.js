'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ProjectCtrl
 * @description
 * # ProjectCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ProjectCtrl', function ($scope, $http, $alert) {
    $scope.currentStep = 1;
    $scope.model = "Project";
    $scope.qas = new Array();
    $scope.selection = new Array();
    $scope.currentEditedElement = null;
    $scope.name = "";
    $scope.customer = "";
    $scope.qpList = new Array();
    $scope.selectionqp = new Array()
    $scope.catalog = "";
    $scope.qas = new Array();
    $scope.currentCategoriesFilter = new Array();

    $scope.nextStep = function () {
      var isLastStep = false;
      if ($scope.currentStep == 1) {
        $scope.choose($scope.name, $scope.customer, $scope.selection);
      } else if ($scope.currentStep == 2) {
        $scope.createProject()
        isLastStep = true;
      } else {
        $scope.currentStep = 1; // restart
      }
      if (!isLastStep) {
        $scope.currentStep++;
      }
    }

    $scope.back = function (currentStep) {
      $scope.currentStep = currentStep - 1;
    }

    $scope.createProject = function () {
      console.log("Data: " + $scope.name, $scope.customer, $scope.selectionqp, $scope.catalog, $scope.selection);
      $scope.qas = $scope.selection;
      for (var i in $scope.qas) {
        delete $scope.qas[i].categories;
      }
      $http.post('/api/project', {
        name: $scope.name,
        customer: $scope.customer,
        qps: $scope.selectionqp,
        qas: $scope.qas,
        catalog: $scope.catalog
      }).
        success(function (data, status, headers, config) {
          console.dir(status + " data: " + data);
          console.dir(data);

          var alert = $alert({
            title: 'Congratulations!',
            content: 'Your Project was created successfully.',
            container: "#alerts-container",
            type: 'success',
            show: true
          });

          //$scope.success.push(data);
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
          //$scope.errors.push(data);
        });
    }

    $scope.loadQAs = function (catalog) {
      console.log("Selected Catalog: " + catalog);
      $scope.catalog = catalog;
      $http.get('/api/qa/catalog=' + catalog)
        .success(function (data) {
          console.log("success" + data);
          $scope.qas = new Array();

          $(data).each(function(index, val){
            var qa = data[index].qa;
            $scope.qas.push(qa);
          });
        })
        .error(function (data, status) {
          console.log("error" + status)
        });

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

    $scope.choose = function (name, customer, selectionqp) {
      // TODO emre: validation of first step's values -> if ok, go to next step

      // TODO emre: save the image somewhere localy / temporarly
      $scope.selectionqp = selectionqp;
      $scope.selection = [];
      console.log(name, customer, selectionqp)
    }

    $scope.switchCurrentEditedElement = function (qa) {
      $scope.currentEditedElement = qa;
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
      // TODO emre: some filter functions are in catalog.js and project.js maybe this could be refactored into filter.js
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
  });
