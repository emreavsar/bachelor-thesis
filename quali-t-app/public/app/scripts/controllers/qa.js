'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:QACtrl
 * @description
 * # QACtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('QACtrl', function ($scope, apiService, alerts, taOptions, $sce, qaTextService, $stateParams, $state, $modal) {
    $scope.qaText = "";
    $scope.qaTextHtml = "";
    $scope.taOptions = taOptions;
    $scope.previewHtml = "";
    $scope.catalogQa = null;
    $scope.catalogId = null;
    $scope.isEditMode = false;

    // only for controlling of used variables
    $scope.usedVariables = new Array();

    /**
     * This watch is needed to bind the variables to the quality attributes text
     * For example when the user removes a variable by hand.
     */
    $scope.$watch('qaText', function (newValue, oldValue) {
      if (newValue != oldValue) {

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

    $scope.selectCategories = function (categories) {
      _.forEach(categories, function (n) {
        var checkbox = $("filter input[type='checkbox'][data-id='" + n.id + "']").prop("checked", "checked");
      });
    }

    $scope.editVariable = function (variable) {
      var superType = qaTextService.getSupertype(variable);
      var subType = qaTextService.getSubType(variable);

      // create new isolated scope for modal view
      var modalScope = $scope.$new(true);
      modalScope.variable = variable;
      modalScope.type = superType;
      modalScope.subType = subType;
      modalScope.editor = null;
      modalScope.savedSelection = null;

      if (superType == 'ENUM') {
        modalScope.enumList = new Array();
      }

      var modal = $modal({
        scope: modalScope,
        template: "templates/add-var-modal.tpl.html"
      });
    }

    $scope.$on('variablesUpdated', function (event, arg) {
      $scope.taOptions.variables[qaTextService.getVariableString(arg)] = arg;
      if ($stateParams.catalogId != undefined) {
        $scope.usedVariables = $scope.getVariables($scope.qaText, false);
      }
    });


    $scope.removeVariable = function (variable) {
      delete $scope.taOptions.variables[qaTextService.getVariableString(variable)];
      $scope.qaText = $scope.getUpdatedQaText($scope.qaText, variable);
    }

    /**
     * Returns the quality attributes text after removing a given variable
     * @param qaText
     * @param variableToRemove
     * @returns {*}
     */
    $scope.getUpdatedQaText = function (qaText, variableToRemove) {
      return qaText.replace(qaTextService.getVariableString(variableToRemove), "");
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
            // already persisted
            if ("valRange" in variable && variable.valRange != undefined && variable.valRange != null && Object.keys(variable.valRange).length !== 0) {
              var placeholderText = "Value must be between " + variable.valRange.min + " and " + variable.valRange.max;
              var inputSize = placeholderText.length;
            }
            else if (variable.min != undefined && variable.max != undefined) { // not peristed yet
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

                var value;
                // if already persisted
                if (typeof(variable.values[j]) == "object") {
                  value = variable.values[j].value;
                } else {
                  value = variable.values[j];
                }

                // if there was a default value, make selection
                if (variable.defaultValue != undefined) {
                  if (typeof(variable.values[j]) == "object") {
                    selectedAttr = (variable.values[j].default ? "selected" : "");
                  } else {
                    selectedAttr = (variable.values[j] == variable.defaultValue ? "selected" : "");
                  }
                }
                descContainerHtml += "<option class='form-option' " + selectedAttr + ">" + value + "</option>";
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
        if ($stateParams.catalogQa != undefined) {
          // find the variable with the varindex
          // check if $scope.taOptions.variables is normal array or associative array

          if ($scope.taOptions.variables.length == 0) {
            var varOptions = $scope.taOptions.variables[token];
          } else {
            var varOptions = _.findWhere($scope.taOptions.variables, {'varIndex': parseInt(varIndex)});
          }
        } else {
          var varOptions = $scope.taOptions.variables[token];
        }
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

    $scope.init = function () {
      $scope.qaText = "";
      $("#categories input[type='checkbox']").removeAttr("checked");

      if ($stateParams.catalogQa != undefined) {
        $scope.catalogQa = $stateParams.catalogQa;
      }
      if ($stateParams.catalogId != undefined) {
        $scope.catalogId = $stateParams.catalogId;
      }
      $scope.isEditMode = $state.$current.name == "editQA";

      var promiseInit = apiService.getCategories();
      promiseInit.then(
        function (payload) {
          $scope.catList = payload.data;

          if ($stateParams.catalogQa != undefined) {
            return apiService.getCatalogQa($stateParams.catalogQa);
          }
        })
        .then(function (payload) {
          if ($stateParams.catalogQa != undefined) {
            $scope.catalogQa = payload.data;
            $scope.qaText = $scope.catalogQa.qa.description;
            $scope.taOptions.variables = $scope.catalogQa.variables;

            $scope.selectCategories($scope.catalogQa.qa.categories);
          }
        });
    }

    $scope.createOrUpdate = function (qaText) {
      var data = {
        qa: {
          description: qaText,
        }
      };

      if ($stateParams.catalogQa != undefined) {
        data.catalogQa = $scope.catalogQa;
        var qaId = $scope.catalogQa.qa.id;
        delete data.catalogQa["qa"];
        data.qa.id = qaId;
        if ($stateParams.catalogId == undefined || $stateParams.catalogId == "") {
          data.qa.categories = $scope.getSelectedCategories();
          // TODO refactor standard catalog id into a configuration class
          data.catalog = -6000;
        } else {
          data.catalog = $stateParams.catalogId;
          delete data.qa["description"];
        }
        delete data.catalogQa["qaInstances"];
        delete data.catalogQa["variables"];
        // update variables
        data.catalogQa.variables = $scope.getVariables(qaText, false);
      } else {
        data.qa.categories = $scope.getSelectedCategories();
        data.catalogQa = {
          variables: $scope.getVariables(qaText, false)
        }
      }

      var promiseCreateOrUpdate;
      if ($stateParams.catalogQa != undefined) {
        // if catalog is given, only change variables
        if ($stateParams.catalogId != undefined && $stateParams.catalogId != "") {
          promiseCreateOrUpdate = apiService.updateQaFromCatalog(data);
        } else {
          // else change the catalog itself
          promiseCreateOrUpdate = apiService.updateQa(data);
        }
      } else {
        promiseCreateOrUpdate = apiService.createQa(data);
      }
      promiseCreateOrUpdate.then(
        function (payload) {
          if ($stateParams.catalogQa != undefined) {
            var alert = alerts.createSuccess('Quality Attribute Template updated successfully');
            if ($stateParams.catalogId != undefined && $stateParams.catalogId != "") {
              $state.go("editCatalog", {
                catalogId: $stateParams.catalogId
              });
            }
          } else {
            // if a redirect to edit catalog is desired, add qa to catalog also
            if ($stateParams.catalogId == undefined || $stateParams.catalogId == "") {
              var alert = alerts.createSuccess('Quality Attribute Template created successfully');
            } else {
              return apiService.addCatalogQa($stateParams.catalogId, payload.data.id, data.catalogQa.variables);
            }
          }
          $scope.taOptions.lastUsedVariableNumber = 0;

          // redirect to showQA if no catalogId was given in parameter
          if ($stateParams.catalogId == undefined || $stateParams.catalogId == "") {
            $state.go("showQA");
          }
        }).then(function (payload) {
          if (payload != undefined) {
            // only when qa is added to catalog and needs a redirect
            var alert = alerts.createSuccess('Quality Attribute Template created successfully and added to Catalog');
            $state.go("editCatalog", {
              catalogId: $stateParams.catalogId
            });
          }
        });
    }
  });
