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

    $http.get('/api/cat')
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

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
      console.log("delete button was clicked");
      alert("implement me");
    }

    $scope.edit = function (clickedCat) {
      $scope.openModalView("edit", clickedCat);
    }

    $scope.updateSubCategory = function (name, icon, parent) {
      // TODO corina, please implement this (cheers emre)
      alert("implement me!");
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
          $http.get('/api/cat')
            .success(function (data) {
              $scope.catList = data;
            })
            .error(function (data, status) {
              console.log(status)
            });
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }

    $scope.createSuperCat = function (name, icon) {
      // simulate a clicked element
      var clickedElement = $("<div/>", {
        'data-id': '',
        'data-name': ''
      });

      $scope.add(clickedElement);
    }
  });
