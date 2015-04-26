'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:CategoryCtrl
 * @description
 * # CategoryCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('CategoryCtrl', function ($scope, $http) {
    $scope.errors = new Array();
    $scope.success = new Array();

    $http.get('/api/cat')
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
    $scope.deleteCat = function () {
      console.log("deleted called: ");
    }
    $scope.showDetail = function (clickedCat) {
      console.dir($(clickedCat));
      var button;
      $("button").click(function() {
        button = this.id;
      });
      var id = $(clickedCat).data('id');
      alert("category with id=" + id+ " was clicked");
      console.log(button);

      var num = null;
      $(".btn-group > button").on("click", function(){
        num = +this.innerHTML;
        alert("Value is " + num);
      });


      if (button == 'add'){
        console.log("add");
        scope.editCatStrap = {
          "title": "Title",
          "content": "Hello Modal<br />This is a multiline message!"
        };
      }

      var id = $(clickedCat).data('id');
      alert("category with id=" + id+ " was clicked");

    }

    $scope.createSuperCat = function (name) {
      $http.post('/api/cat', {
        name: name,
        parent: ""
      }).
        success(function(data, status, headers, config) {
          console.log(status + " data: " + data);
          $scope.success.push(data);
        }).
        error(function(data, status, headers, config) {
          console.log(status);
          $scope.errors.push(data);
        });
    }
  });
