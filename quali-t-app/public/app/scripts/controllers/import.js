'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('ImportCtrl', function ($scope, apiService, alertService) {
    $scope.importedCatalogId;
    $scope.importedCatalogName;

    $scope.import = function () {
      var importfile = document.getElementById("importFile").files[0];
      var reader = new FileReader();

      reader.onloadend = function (e) {
        var data = e.target.result;
        if ($scope.isJsonString(data)) {
          var catalogJson = JSON.parse(data);
          var importPromise = apiService.importCatalog(catalogJson);
          importPromise.then(function (payload) {
            alertService.createSuccess("Catalog successfully imported");
            $scope.importedCatalogId = payload.data.id;
            $scope.importedCatalogName = payload.data.name;
          });
        } else {
          alertService.createLocalWarning("Uploaded file is not valid JSON. Try to upload a valid JSON file");
        }
      }
      reader.readAsText(importfile);
    }

    $scope.isJsonString = function (str) {
      try {
        JSON.parse(str);
      } catch (e) {
        return false;
      }
      return true;
    }
  });
