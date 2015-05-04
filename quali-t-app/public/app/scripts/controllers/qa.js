'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, $http, alerts, taOptions, $sce) {
    $scope.qaText = "";
    $scope.qaTextHtml = "";
    $scope.taOptions = taOptions;

    $scope.getSelectedCategories = function () {
      var selectedCategories = new Array();
      $scope.selectedCategories = new Array();
      $("#categories input[type='checkbox']:checked").each(function () {
        selectedCategories.push({id: $(this).data('id')});
        selectedCategories.push({name: $(this).data('name')});
      });
      return selectedCategories;
    }

    /**
     * Shows a preview of the qa-text. Replaces all variables with html elements (for ex. enum => <select/>)
     * @returns {string} HTML
     */
    $scope.qaPreview = function () {
      var variables = $scope.getVariables($scope.qaText);
      var descContainerHtml = "";
      var nextVariableToUse = 0;
      var tokens = ($scope.qaText.split(/%VARIABLE_(FREETEXT|FREENUMBER|ENUM|ENUMPLUS){1}_\d*%/g) || []);


      // TODO emre: can be refactored to a custom directive, used in catalog, project and here
      var qaContainer = $("<div/>", {
        class: "qa well row"
      });

      var categories = $("<div/>", {
        class: "categories"
      }).appendTo(qaContainer);

      var selectedCategories = $scope.getSelectedCategories();
      for (var j = 0; j < selectedCategories.length; j++) {
        $("<span/>", {
          class: "label label-default",
          text: selectedCategories[j].name
        }).appendTo(categories);
      }

      var descContainer = $("<div/>", {
        class: "col-sm-12"
      }).appendTo(qaContainer);


      for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];

        // if its a token, do special htmlizing
        if ($scope.isVariable(token)) {
          var variable = variables[nextVariableToUse];

          if (variable.type == "FREETEXT") {
            descContainerHtml += "<input type='text' placeholder='' />";
          } else if (variable.type == "FREENUMBER") {
            if (variable.min != undefined) {
              var placeholderText = "Value must be between " + variable.min + " and " + variable.max;
              var inputSize = placeholderText.length;
            } else {
              var placeholderText = "";
              var inputSize = 10; // default for numbers
            }
            descContainerHtml += "<input type='text' placeholder='" + placeholderText + "' size='" + inputSize + "'/>";
          } else if (variable.type == "ENUM") {
            descContainerHtml += "enums not implemented yet";

          } else if (variable.type == "ENUMPLUS") {
            descContainerHtml += "enumplus not implemented yet";
          }

          // move to next variable
          nextVariableToUse++;
        } else {
          // only append
          descContainerHtml += token;
        }
      }
      descContainer.append(descContainerHtml);
      $scope.qaTextHtml = $sce.trustAsHtml($(qaContainer)[0].outerHTML);
      return $scope.qaTextHtml;
    }

    $scope.isVariable = function (strToTest) {
      return (strToTest == "FREETEXT" || strToTest == "FREENUMBER" || strToTest == "ENUM" || strToTest == "ENUMPLUS");
    }

    /**
     * Does the parsing of the quality attribute string and prepares the object structure with variables sent to the backend
     * @returns {Array}
     */
    $scope.getVariables = function (qaText) {
      var variables = new Array();

      var tokens = (qaText.match(/%VARIABLE_(FREETEXT|FREENUMBER|ENUM|ENUMPLUS){1}_\d*%/g) || []);
      for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        var type = token.match(new RegExp(/(FREETEXT|FREENUMBER|ENUM|ENUMPLUS){1}/g) || [])[0];

        // these are the basic values for all variable types
        var variable = {
          type: type,
          number: i
        }

        // qa type specific content, will not be saved in the string -> only setable with ui-interactions
        // check if has key
        if (Object.keys($scope.taOptions.variables).indexOf(i) && $scope.taOptions.variables[i] != undefined) {
          var varOptions = $scope.taOptions.variables[i];
        } else {
          var varOptions = {
            min: '',
            max: '',
            defaultValue: ''
          };
        }
        if (type == "FREENUMBER" || type == "ENUMPLUS") {
          variable.min = varOptions.min;
          variable.max = varOptions.max;
        } else if (type == "ENUM" || type == "ENUMPLUS") {
          variable.defaultValue = varOptions.defaultValue;
        }
        variables.push(variable);
      }

      return variables;
    }

    $http.get("/api/cat")
      .success(function (data) {
        $scope.catList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });

    $scope.createQA = function (qaText) {
      $http.post('/api/qa', {
        qa: {
          description: qaText,
          categories: $scope.getSelectedCategories(),
          variables: $scope.getVariables(qaText)
        }
      }, {headers: {'Content-Type': 'application/json'}}).
        success(function (data, status, headers, config) {
          $scope.qaText = "";
          var alert = alerts.createSuccess('QA created successfully', "#alerts-container");
        }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data, "#alerts-container");
        });
    }

    $http.get('/api/qa')
      .success(function (data) {
        $scope.qaList = data;
      })
      .error(function (data, status) {
        console.log(status)
      });
  });
