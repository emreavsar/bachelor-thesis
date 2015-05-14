'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:EditQaCtrl
 * @description
 * # EditQaCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('EditQaCtrl', function ($scope, apiService) {
    $scope.isTextMode = false;
    $scope.tooltipsEditMode = "Change either the quality attribute itself (text) or set the values (variables)";

    /**
     * Helper function. Workaround to hide modal through controller.
     * @param hideFunction
     */
    $scope.bindHide = function (hideFunction) {
      $scope.hideModal = hideFunction;
    }

    $scope.update = function () {
      // update QAs text
      if ($scope.isTextMode) {

      } else {
        // update QAs instance values
        var qaValues = new Array();

        // get all inputs, selects etc. where variable can be inserted by user
        var varInputs = $(".modal .qa:visible").find("[data-for-variable]");
        for (var i = 0; i < varInputs.length; i++) {
          var varInput = varInputs[i];
          var valueId = $(varInput).data("value-id");
          var valueVarIndex =  $(varInput).data("value-varindex");
          var valueUserInput = "";

          // if input, get value
          if ($(varInput).is("input")) {
            valueUserInput = $(varInput).val();
          } else if ($(varInput).is("select")) {// if select get selected option
            valueUserInput = $(varInput).find(":selected").val();
          }

          var value = {
            varIndex: valueVarIndex,
            value: valueUserInput
          }

          // add id only if new
          if (valueId != "") {
            value.id = valueId;
          }

          qaValues.push(value);
        }

        var instance = $scope.qa;
        instance.values = qaValues;
        // delete not needed information
        delete instance.qualityPropertyStatus;
        delete instance.template;

        apiService.updateQaInstance(instance);
      }
      //$scope.hideModal();
    }
  });
