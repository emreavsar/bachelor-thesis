'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CustomerCtrl
 * @description
 * # CustomerCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CustomerCtrl', function ($scope, apiService, $modal, ngParamService) {
    $scope.name = "";
    $scope.address = "";
    $scope.errors = new Array();
    $scope.success = new Array()
    $scope.customerList = new Array();

    $scope.createCustomer = function (name, address) {
      $scope.errors = new Array();
      $scope.success = new Array();
      $scope.model = "Customer";

      var createPromise = apiService.createCustomer(name, address);
      createPromise.then(function (payload) {
        // todo add success alert
        $scope.loadCustomer();
      });
    }

    $scope.loadCustomer = function () {
      var loadPromise = apiService.getCustomers();
      loadPromise.then(function (payload) {
        var data = payload.data;
        $scope.customerList = data;
        $scope.tableParams = ngParamService.getDefaultNgParams(data);
      });
    }


    $scope.delete = function (customer) {
      var conf = confirm('This will delete all projects etc. of the customer, u sure?');
      if (conf) {
        var deletePromise = apiService.deleteCustomer(customer.id);
        deletePromise.then(function (payload) {
          $scope.customerList = payload.data;
          $scope.loadCustomer();
          // todo add success alert
        });
      }
    }

    $scope.editCustomer = function (customer) {
      var updatePromise = apiService.updateCustomer(customer.id, customer.name, customer.address);
      updatePromise.then(function (payload) {
        $scope.customerList = payload.data;
        $scope.loadCustomer();
        // todo add success alert
      });
    }

    $scope.edit = function (customer) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.customer = customer;
      modalScope.type = 'edit';
      modalScope.callBackFunction = $scope.editCustomer;

      var modal = $modal({
        scope: modalScope,
        template: "templates/customer-modal.tpl.html"
      });
    }

    $scope.add = function (customer) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.customer = customer;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.createCustomer;

      var modal = $modal({
        scope: modalScope,
        template: "templates/customer-modal.tpl.html"
      });
    }

    $scope.init = function () {
      $scope.loadCustomer();
    }
  });
