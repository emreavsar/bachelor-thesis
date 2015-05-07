'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:AddvarmodalCtrl
 * @description
 * # AddvarmodalCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('AddVarModalCtrl', function ($scope, $modal, taOptions) {
    $scope.enumElementText = "";
    $scope.enumList = new Array();
    $scope.subType;
    $scope.type;
    $scope.taOptions = taOptions;
    //taOptions.variables = taOptions.variables;

    $scope.tooltipsExtendable = "By checking this box you allow the users to use custom values at creating a new project. " +
    "If not checked, the user has to take one of the specified values";
    $scope.tooltipsRange = "Numeric values can be limited to a range (minimum and maximum value).";
    $scope.tooltipsDefaultvalue = "By checking this box you enable a default value for the dropdown. " +
    "This default value will be automatically selected at creation of a new project.";

    /**
     * Used in modal view to add a new element to the enum (type equals enumtext or enumnumber)
     * @param type
     * @param enumElement
     */
    $scope.addToList = function (type, enumElement) {
      // list of enums is unique
      if (!_.contains($scope.enumList, enumElement)) {
        // check type
        var isNumeric = !_.isNaN(parseInt(enumElement)) && _.isNumber(parseInt(enumElement));
        if (type == "NUMBER" && isNumeric || type == "TEXT") {
          $scope.enumList.push(enumElement)
          $scope.enumElementText = "";
        }
      }
    }

    /**
     * Used in modal view to remove a new element from the the enum (type equals enumtext or enumnumber)
     * @param type
     * @param enumElement
     */
    $scope.removeFromList = function (enumElement) {
      $scope.enumList.splice($scope.enumList.indexOf(enumElement), 1);
    }

    $scope.$watch('subType', function (newValue, oldValue) {
      if (newValue != oldValue) {
        // clear list of elements if user changes the type
        $scope.enumList = new Array();
        $scope.enumElementText = "";
      }
    });

    $scope.add = function (editor, savedSelection, type, subType, enumList, rangeMinValue, rangeMaxValue, defaultValue, isExtendable) {

      rangy.restoreSelection(savedSelection);
      var elementText = "%VARIABLE_" + type + subType + "_" + $scope.taOptions.lastUsedVariableNumber + "%";
      var fullType = type + subType;

      var options = $scope.getOptions(type, subType, enumList, rangeMinValue, rangeMaxValue, defaultValue, isExtendable);
      $scope.taOptions.variables[elementText]=$scope.getVariableObject(fullType, options);

      editor.focussed = true;
      editor.wrapSelection("insertText", elementText, false);

      $scope.hideModal();
    }

    /**
     * Helper function. Prepares a javascript object which fills the properties
     * of a variable based on the type of the variable
     * @param type
     * @param subType
     * @param enumList
     * @param rangeMinValue
     * @param rangeMaxValue
     * @param defaultValue
     * @param isExtendable
     * @returns options {}
     */
    $scope.getOptions = function (type, subType, enumList, rangeMinValue, rangeMaxValue, defaultValue, isExtendable) {
      var options = {};
      if (type == "FREE" && subType == "NUMBER") {
        options.min = rangeMinValue;
        options.max = rangeMaxValue;
      } else if (type == "ENUM") {
        options.defaultValue = defaultValue;
        options.values = enumList;
        options.extendable = isExtendable;
        if (subType == "NUMBER") {
          options.min = rangeMinValue;
          options.max = rangeMaxValue;
        }
      }
      return options;
    }

    /**
     * Prepares a object with all information which conforms structural what the backend needs.
     * @param type
     * @param options
     * @returns {{type: *, number: *}}
     */
    $scope.getVariableObject = function (type, options) {
      var variable = {
        type: type,
        number: $scope.taOptions.lastUsedVariableNumber
      }

      // add options as key-value to the variable object
      _.forEach(options, function (value, key) {
        variable[key] = value;
      });

      $scope.taOptions.lastUsedVariableNumber = $scope.taOptions.lastUsedVariableNumber + 1;
      return variable;
    }

    /**
     * Helper function. Workaround to hide modal through controller.
     * @param hideFunction
     */
    $scope.bindHide = function (hideFunction) {
      $scope.hideModal = hideFunction;
    }


    /**
     * Helper function. Function makes checks in the view easier
     * by calling this function rather than evaluating expressions.
     * @returns {boolean}
     */
    $scope.isFree = function () {
      return $scope.type == "FREE";
    }

    /**
     * Helper function. Function makes checks in the view easier
     * by calling this function rather than evaluating expressions.
     * @returns {boolean}
     */
    $scope.isEnum = function () {
      return $scope.type == "ENUM";
    }

    /**
     * Helper function. Function makes checks in the view easier
     * by calling this function rather than evaluating expressions.
     * @returns {boolean}
     */
    $scope.isText = function () {
      return $scope.subType == "TEXT";
    }

    /**
     * Helper function. Function makes checks in the view easier
     * by calling this function rather than evaluating expressions.
     * @returns {boolean}
     */
    $scope.isNumeric = function () {
      return $scope.subType == "NUMBER";
    }
  });
