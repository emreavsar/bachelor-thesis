'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QualitypropertyCtrl
 * @description
 * # QualitypropertyCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QualitypropertyCtrl', function ($scope, $http, $modal) {
    $scope.name = "";
    $scope.description = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.loadQualityProperties = function () {
      $http.get('/api/qp')
        .success(function (data) {
          $scope.qpList = data;
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.loadQualityProperties();


    $scope.delete = function (qp) {
      var conf = confirm('This will delete all projects etc. of the qp, u sure?');
      if (conf) {
        alert("deleted" + qp.id);
      }
      $http.delete('/api/qp/' + qp.id)
        .success(function (data) {
          $scope.qpList = data;
          $scope.loadQualityProperties();
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.editQP = function (qp) {
      $http.put('/api/qp', {
        id: qp.id,
        name: qp.name,
        description: qp.description
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadQualityProperties();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
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


      // in modal <button ng-click=callBackFunction(customer) />

    }


    $scope.addQP = function (qp) {
      $http.post('/api/qp', {
        name: qp.name,
        description: qp.description
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadQualityProperties();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
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


      // in modal <button ng-click=callBackFunction(customer) />

    }


  });
