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
    if($scope.context=='editproject'){
      $scope.isTextMode = false;
    } else if($scope.context=='editcatalog'){
      $scope.isTextMode = true;
    }
    $scope.tooltipsEditMode = "Change either the quality attribute itself (text) or set the values (variables)";
    // when qa itself changes (isTextMode)
    $scope.qaNew = $scope.qa;

    /**
     * Helper function. Workaround to hide modal through controller.
     * @param hideFunction
     */
    $scope.bindHide = function (hideFunction) {
      $scope.hideModal = hideFunction;
    }

    /**
     * Updates the quality attribute instance. Either the text or the values in the variable
     */
    $scope.update = function () {
      var instance = $scope.qa;
      // delete not needed information
      delete instance.qualityPropertyStatus;
      delete instance.template;

      // update QAs text
      if ($scope.isTextMode) {
        instance.description = qaNew.description;
      } else {
        // update QAs instance values
        var qaValues = new Array();

        // get all inputs, selects etc. where variable can be inserted by user
        var varInputs = $(".modal .qa:visible").find("[data-for-variable]");
        for (var i = 0; i < varInputs.length; i++) {
          var varInput = varInputs[i];
          var valueId = $(varInput).data("value-id");
          var valueVarIndex = $(varInput).data("value-varindex");
          var valueUserInput = "";

          var isExtendable = $(varInput).parent().hasClass("isExtendable");
          var ignore = false;

          // if input, get value
          if ($(varInput).is("input")) {
            if (isExtendable) {
              // if the input text is an extendable and has a value, take it otherwise the dropdown will be taken
              if ($(varInput).val() != "") {
                valueUserInput = $(varInput).val();
              } else {
                ignore = true;
              }
            } else {
              valueUserInput = $(varInput).val();
            }
          } else if ($(varInput).is("select")) {// if select get selected option
            // check if extendable
            if (isExtendable) {
              // check if the input text has a value inside, if yes -> ignore this one
              if ($(varInput).parent().find("input").val() == "") {
                valueUserInput = $(varInput).find(":selected").val();
              } else {
                ignore = true;
              }
            } else {
              valueUserInput = $(varInput).find(":selected").val();
            }
          }

          if (!ignore) {
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
        }

        instance.values = qaValues;
      }
      apiService.updateQaInstance(instance);
      $scope.hideModal();
    }
  });
