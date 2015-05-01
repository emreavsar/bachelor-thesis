'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CategoryCtrl
 * @description
 * # CategoryCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CategoryCtrl', function ($scope, $http, $modal) {
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadCategories = function () {
      $http.get('/api/cat')
        .success(function (data) {
          $scope.catList = data;
        })
        .error(function (data, status) {
          console.log(status)
        });
    }
    $scope.loadCategories();

    $scope.openModalView = function (type, clickedCat) {
      var parentId = $(clickedCat).data('id');
      var name = $(clickedCat).data('name');

      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.type = type;
      modalScope.category = {
        name: name,
        parentId: parentId
      }
      modalScope.updateSubCategory = $scope.updateSubCategory;
      modalScope.createSubCategory = $scope.createSubCategory;

      var modal = $modal({
        scope: modalScope,
        template: "templates/category-modal.tpl.html"
      });
    }

    $scope.add = function (clickedCat) {
      $scope.openModalView("add", clickedCat);
    }

    $scope.delete = function (clickedCat) {
      $http.delete('/api/cat/' + $(clickedCat).attr("data-id")
      ).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);
          alert("category deleted");

          // reload when categories has changed
          $scope.loadCategories();
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });


    }

    $scope.edit = function (clickedCat) {
      $scope.openModalView("edit", clickedCat);
    }

    $scope.updateSubCategory = function (name, icon, id) {
      $http.put('/api/cat', {
        name: name,
        icon: icon,
        id: id
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);

          // reload when categories has changed
          $scope.loadCategories();
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }

    $scope.createSubCategory = function (name, icon, parent) {
      $http.post('/api/cat', {
        name: name,
        icon: icon,
        parent: parent
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);

          // reload when categories has changed
          $scope.loadCategories();
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }

    $scope.createSuperCat = function (name, icon) {
      console.log(name);
      console.dir(name);
      // simulate a clicked element
      var clickedElement = $("<div/>", {
        'data-id': '',
        'data-name': ''
      });

      $scope.add(clickedElement);
    }
  });
