'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QualitypropertyCtrl
 * @description
 * # QualitypropertyCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QualitypropertyCtrl', function ($scope, apiService, $modal, ngTableParams, $filter, alertService) {
    $scope.name = "";
    $scope.description = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadQualityProperties = function () {
      var loadPromise = apiService.getQualityProperties();
      loadPromise.then(function (payload) {
        $scope.qpList = payload.data;

        if ($scope.tableParams == undefined) {
          $scope.tableParams = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
              id: 'desc'
            }
          }, {
            total: function () {
              return $scope.qpList.length;
            },
            getData: function ($defer, params) {
              // use build-in angular filter
              var orderedData = params.sorting() ?
                $filter('orderBy')($scope.qpList, params.orderBy()) :
                $scope.qpList;
              params.total(orderedData.length);
              $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
          });
        }
        else {
          $scope.tableParams.reload();
        }
      });
    }


    $scope.delete = function (qp) {
      var conf = confirm('This will delete all states (in quality attributes inside projects) connected to this qp, are u sure you want to do this?');
      if (conf) {
        var deletePromise = apiService.deleteQualityPropery(qp.id);
        deletePromise.then(function (payload) {
          $scope.qpList = payload.data;
          $scope.loadQualityProperties();
          alertService.createSuccess("Qualityproperty successfully deleted");
        });
      }
    }

    $scope.editQP = function (qp) {
      var updatePromise = apiService.updateQualityPropery(qp.id, qp.name, qp.description);

      updatePromise.then(function (payload) {
        $scope.loadQualityProperties();
        $scope.success.push(payload.data);
        alertService.createSuccess("Qualityproperty successfully updated");
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
        alertService.createSuccess("Qualityproperty successfully created");
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
