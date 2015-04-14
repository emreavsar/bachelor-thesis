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

    $scope.getCategories = function () {
      // todo emre: getting the filter objects from backend
      var categories = new Array();
      var subCatFunctionality = {
        name: 'Functionality',
        icon: 'fa fa-cog',
        categories: [
          {
            id: 500,
            name: 'Suitability',
            categories: [
              {
                id: 555,
                name: 'something!',
                icon: 'fa fa-user',
                categories: [
                  {
                    id: 500,
                    name: 'Suitability 1',
                    categories: [
                      {
                        id: 555,
                        name: 'something!',
                        icon: 'fa fa-user'
                      }
                    ]
                  },
                  {
                    id: 500,
                    name: 'Suitability 2',
                    categories: [
                      {
                        id: 555,
                        name: 'something!',
                        icon: 'fa fa-user'
                      }
                    ]
                  },
                ]
              }
            ]
          },
          {
            id: 501,
            name: 'Accuracy'
          },
          {
            id: 502,
            name: 'Interoperability'
          },
          {
            id: 503,
            name: 'Security'
          },
          {
            id: 504,
            name: 'Functionality Compliance'
          }
        ]
      }

      var subCatUsability = {
        name: 'Usability',
        icon: 'fa fa-heart',
        categories: [
          {
            id: 600,
            name: 'Understandability',
            icon: 'fa fa-heart',
            categories: [
              {
                id: 555,
                name: 'something!',
                icon: 'fa fa-user'
              }
            ]
          },
          {
            id: 601,
            name: 'Learnability'
          },
          {
            id: 602,
            name: 'Attractiveness'
          },
          {
            id: 603,
            name: 'Usability Compliance'
          }
        ]
      }

      categories.push(subCatFunctionality);
      categories.push(subCatUsability);
      return categories;
    }
    $scope.nextStep = function () {
      var isLastStep = false;
      if ($scope.currentStep == 0) {
        $scope.choose($scope.name, $scope.image);
      } else if ($scope.currentStep == 1) {
        $scope.customize();
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
      // TODO implement me
    }

    $scope.createCatalog = function () {
      // TODO implement me
      $http.post('/api/catalog', {
        qualityAttributes: $scope.selection
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

    $scope.filter = function (clickedElement) {
      var checkbox = $(clickedElement).find("input[type='checkbox']");
      var checkVal = checkbox.prop('checked');
      if (checkVal) {
        checkbox.prop('checked', '');
      } else {
        checkbox.prop('checked', 'checked');
      }

      var id = $(clickedElement).prop('data-id');
    }
  });
