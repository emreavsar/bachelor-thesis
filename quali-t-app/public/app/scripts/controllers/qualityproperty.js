'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QualitypropertyCtrl
 * @description
 * # QualitypropertyCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QualitypropertyCtrl', function ($scope, apiService, $modal) {
    $scope.name = "";
    $scope.description = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadQualityProperties = function () {
      var loadPromise = apiService.getQualityProperties();
      loadPromise.then(function (payload) {
        $scope.qpList = payload.data;
      });
    }


    $scope.delete = function (qp) {
      var conf = confirm('This will delete all projects etc. of the qp, u sure?');
      if (conf) {
        var deletePromise = apiService.deleteQualityPropery(qp.id);
        deletePromise.then(function (payload) {
          $scope.qpList = payload.data;
          $scope.loadQualityProperties();
        });
      }
    }

    $scope.editQP = function (qp) {
      var updatePromise = apiService.updateQualityPropery(qp.id, qp.name, qp.description);

      updatePromise.then(function (payload) {
        $scope.loadQualityProperties();
        $scope.success.push(payload.data);
        // todo add success alert
      });
    }

    $scope.edit = function (qp) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.qp = qp;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editQP;

      var modal = $modal({
        scope: modalScope,
        template: "templates/qualityproperty-modal.tpl.html"
      });
    }

    $scope.addQP = function (qp) {
      var createPromise = apiService.createQualityPropery(qp.name, qp.description);

      createPromise.then(function (payload) {
        $scope.qpList = payload.data;
        $scope.loadQualityProperties();
        // todo add success alert
      });
    }

    $scope.add = function (qp) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.qp = qp;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.addQP;

      var modal = $modal({
        scope: modalScope,
        template: "templates/qualityproperty-modal.tpl.html"
      });
    }

    $scope.init = function () {
      $scope.loadQualityProperties();
    }
  });
