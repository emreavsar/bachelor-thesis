'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CustomerCtrl
 * @description
 * # CustomerCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CustomerCtrl', function ($scope, $http, $modal) {
    $scope.name = "";
    $scope.address = "";
    $scope.errors = new Array();
    $scope.success = new Array();

    $scope.createCustomer = function (name, address) {
      $scope.errors = new Array();
      $scope.success = new Array();

      $scope.model = "Customer";
      $http.post('/api/customer', {
        name: name,
        address: address
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }
    $scope.loadCustomer = function () {
      $http.get('/api/customer')
        .success(function (data) {
          $scope.customerList = data;
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.loadCustomer();


    $scope.delete = function (customer) {
      var conf = confirm('This will delete all projects etc. of the customer, u sure?');
      if (conf) {
        alert("deleted" + customer.id);
      }
      $http.delete('/api/customer/' + customer.id)
        .success(function (data) {
          $scope.customerList = data;
          $scope.loadCustomer();
        })
        .error(function (data, status) {
          console.log(status)
        });
    }

    $scope.editCustomer = function (customer) {
      $http.put('/api/customer', {
        id: customer.id,
        name: customer.name,
        address: customer.address
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadCustomer();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
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


      // in modal <button ng-click=callBackFunction(customer) />

    }


    $scope.addCustomer = function (customer) {
      $http.post('/api/customer', {
        name: customer.name,
        address: customer.address
      }).
        success(function (data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.loadCustomer();
          $scope.success.push(data);
        }).
        error(function (data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }

    $scope.add = function (customer) {
      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.customer = customer;
      modalScope.type = 'add';
      modalScope.callBackFunction = $scope.addCustomer;

      var modal = $modal({
        scope: modalScope,
        template: "templates/customer-modal.tpl.html"
      });


      // in modal <button ng-click=callBackFunction(customer) />

    }


  });
