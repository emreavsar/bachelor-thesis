'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, $http, alerts, taOptions, $sce, qaTextService) {
    $scope.qaText = "";
    $scope.qaTextHtml = "";
    $scope.taOptions = taOptions;
    $scope.previewHtml = "";

    // only for controlling of used variables
    $scope.usedVariables = new Array();

    /**
     * This watch is needed to bind the variables to the quality attributes text
     * For example when the user removes a variable by hand.
     */
    $scope.$watch('qaText', function (newValue, oldValue) {
      if (newValue != oldValue) {

        // TODO: emre cleanup here
        // verify if a quality attribute is not used twice (same ID)
        var variables = $scope.getVariables(newValue, false);
        var uniqueVariables = _.uniq(variables, 'varIndex');
        var difference = _.uniq(_.difference(variables, uniqueVariables), 'varIndex');

        // multiple usage of same variable (for examplecopy paste) -> reset and send a warning
        if (difference.length > 0) {
          var multipleUsedVariableName = "";
          $(difference).each(function (index, value) {
            multipleUsedVariableName += value.type + "_" + value.varIndex;
            if (index + 1 < difference.length) {
              multipleUsedVariableName += ", ";
            }
          });
          var alert = alerts.createLocalWarning('Multiple usage of a variable is not permitted: ' + multipleUsedVariableName, 'body ');
        }
        else {
          $scope.taOptions.variables = $scope.getVariables(newValue, true);
          $scope.usedVariables = $scope.getVariables(newValue, false);
          $scope.previewHtml = $scope.qaPreview();
        }
      }
    });

    $scope.getSelectedCategories = function () {
      var selectedCategories = new Array();
      $scope.selectedCategories = new Array();
      $("#categories input[type='checkbox']:checked").each(function () {
        selectedCategories.push({
          id: $(this).data('id'),
          name: $(this).data('name')
        });
      });
      return selectedCategories;
    }

    $scope.removeVariable = function (variable, key) {
      delete $scope.taOptions.variables[key];
      $scope.qaText = $scope.getUpdatedQaText($scope.qaText, variable);
    }

    /**
     * Returns the quality attributes text after removing a given variable
     * @param qaText
     * @param variableToRemove
     * @returns {*}
     */
    $scope.getUpdatedQaText = function (qaText, variableToRemove) {
      var updatedQaText = qaText;

      var textToDelete = "%VARIABLE_" + variableToRemove.type + "_" + variableToRemove.number + "%";
      updatedQaText = updatedQaText.replace(textToDelete, "");

      return updatedQaText;
    }

    /**
     * Shows a preview of the qa-text. Replaces all variables with html elements (for ex. enum => <select/>)
     * @returns {string} HTML
     */
    $scope.qaPreview = function () {
      var variables = $scope.getVariables($scope.qaText, true);
      var descContainerHtml = "";

      // these are all text tokens (normal text split by variables)
      var tokens = ($scope.qaText.split(/%VARIABLE_(FREETEXT|FREENUMBER|ENUMTEXT|ENUMNUMBER){1}_\d*%/g) || []);
      // these are all variables in text (%VARIABLES_TYPE_NUMBER_%)
      var varTokens = ($scope.qaText.match(/%VARIABLE_(FREETEXT|FREENUMBER|ENUMTEXT|ENUMNUMBER){1}_\d*%/g) || []);

      // TODO emre: can be refactored to a custom directive, used in catalog, project and here
      var qaContainer = $("<div/>", {
        class: "qa well row"
      });

      var categories = $("<div/>", {
        class: "categories"
      }).appendTo(qaContainer);

      var selectedCategories = $scope.getSelectedCategories();
      if (selectedCategories.length == 0) {
        $("<span/>", {
          class: "label label-default",
          text: "No category selected"
        }).appendTo(categories);
      } else {

        for (var j = 0; j < selectedCategories.length; j++) {
          $("<span/>", {
            class: "label label-default",
            text: selectedCategories[j].name
          }).appendTo(categories);
        }
      }

      var descContainer = $("<div/>", {
        class: "col-sm-12"
      }).appendTo(qaContainer);


      var nextToken = 0;
      for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];

        // if its a token, do special htmlizing
        if ($scope.isVariable(token)) {
          var variable = variables[varTokens[nextToken]];
          nextToken++;

          if (variable.type == "FREETEXT") {
            descContainerHtml += "<input type='text' placeholder='' />";
          } else if (variable.type == "FREENUMBER") {
            if (variable.min != undefined && variable.max != undefined) {
              var placeholderText = "Value must be between " + variable.min + " and " + variable.max;
              var inputSize = placeholderText.length;
            } else {
              var placeholderText = "";
              var inputSize = 10; // default for numbers
            }
            descContainerHtml += "<input type='text' placeholder='" + placeholderText + "' size='" + inputSize + "'/>";
          } else if (variable.type == "ENUMTEXT" || variable.type == "ENUMNUMBER") {
            descContainerHtml += "<select class='form-control'>";
            descContainerHtml += "<option class='form-option' value=''>Select a value</option>";
            if (variable.values == undefined) {
              var localWarning = alerts.createLocalWarning("Have you inserted the variable (" + qaTextService.getVariableString(variable) + ") manually?");
            } else {

              for (var j = 0; j < variable.values.length; j++) {
                var selectedAttr = "";
                // if there was a default value, make selection
                if (variable.defaultValue != undefined) {
                  selectedAttr = (variable.values[j] == variable.defaultValue ? "selected" : "");
                }
                descContainerHtml += "<option class='form-option' " + selectedAttr + ">" + variable.values[j] + "</option>";
              }
              descContainerHtml += "</select>";
              if (variable.extendable) {
                var extendablePlaceholderText = "or add a new value";
                if (variable.min != undefined && variable.max != undefined) {
                  extendablePlaceholderText += " (between " + variable.min + " and " + variable.max + ")";
                }
                descContainerHtml += " or <input type='text' placeholder='" + extendablePlaceholderText + "'' size='" + extendablePlaceholderText.length + "'' />";
              }
            }
          }
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
      return qaTextService.isVariable(strToTest);
    }


    /**
     * Does the parsing of the quality attribute string and prepares the object structure with variables sent to the backend
     * @param qaText
     * @param associativeArr
     * @returns {Array}
     */
    $scope.getVariables = function (qaText, associativeArr) {
      var variables = new Array();

      var tokens = (qaText.match(/%VARIABLE_(FREETEXT|FREENUMBER|ENUMTEXT|ENUMNUMBER){1}_\d*%/g) || []);
      for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        var options = new RegExp(/(FREETEXT|FREENUMBER|ENUMTEXT|ENUMNUMBER)_(\d+)/g).exec(token);
        var type = options[1];
        var varIndex = options[2];

        // these are the basic values for all variable types
        var variable = {
          type: type,
          varIndex: varIndex
        }

        // qa type specific content, will not be saved in the string -> only setable with ui-interactions
        // check if has key
        var varOptions = $scope.taOptions.variables[token];
        if (varOptions == undefined) {
          var varOptions = {
            min: '',
            max: '',
            defaultValue: '',
            extendable: '',
          };
        }

        // add options as key-value to the variable object
        _.forEach(varOptions, function (value, key) {
          variable[key] = value;
        });

        if (associativeArr) {
          variables[qaTextService.getVariableString(variable)] = variable;
        } else {
          variables.push(variable);
        }
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
        },
        catalogQa: {
          variables: $scope.getVariables(qaText, false)
        }
      }, {headers: {'Content-Type': 'application/json'}}).
        success(function (data, status, headers, config) {
          $scope.qaText = "";
          $("#categories input[type='checkbox']").removeAttr("checked");
          var alert = alerts.createSuccess('QA created successfully');
        }).
        error(function (data, status, headers, config) {
          var alert = alerts.createError(status, data);
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
