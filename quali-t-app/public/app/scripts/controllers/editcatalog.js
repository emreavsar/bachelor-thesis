'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:EditCatalogCtrl
 * @description
 * # EditCatalogCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('EditCatalogCtrl', function ($scope, $stateParams, apiService, alerts, $modal) {
    $scope.catalogId = $stateParams.catalogId;
    $scope.catalog = {};
    $scope.catalogQas = {};

    $scope.popover = {
      title: "Export this catalog as",
      buttons: [
        {
          title: "JSON",
          clickFunction: function () {
            $scope.export("json");
          },
          icon: "fa fa-file-o"
        }
      ]
    };

    $scope.export = function (fileType) {
      var exportPromise = apiService.exportRessource("catalog", $stateParams.catalogId, $scope.catalog.name, fileType);
        exportPromise.then(function (payload) {
        console.log("remove this");
      });
    }

    $scope.save = function () {
      // get only needed information
      var catalog = {
        id: $scope.catalog.id,
        name: $scope.catalog.name,
        description: $scope.catalog.description,
        catalogQas: $scope.catalogQas
      };

      var promiseSave = apiService.updateCatalog(catalog);
      promiseSave.then(
        function (payload) {
          alerts.createSuccess("Catalog was successfully updated.");
          $scope.qualityAttributesToUpdate = new Array();
          $scope.project = payload.data;
        });
    }

    $scope.init = function () {
      var initPromise = apiService.getCatalog($scope.catalogId);
      initPromise.then(function (payload) {
        $scope.catalog = payload.data;
      });

      $scope.loadCatalogQas();
    }

    $scope.loadCatalogQas = function () {
      var loadPromise = apiService.getQAsOfCatalog($scope.catalogId);
      loadPromise.then(function (payload) {
        $scope.catalogQas = payload.data;
      });
    }

    $scope.addQaToCatalog = function() {
      var modalScope = $scope.$new(true);
      var modal = $modal({
        scope: modalScope,
        template: "templates/add-qa-modal.tpl.html"
      });
    }
  });
