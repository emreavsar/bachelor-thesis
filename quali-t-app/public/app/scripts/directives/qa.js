'use strict';

/**
 * @ngdoc directive
 * @name qualitApp.directive:qa
 * @description
 * # qa
 * Directive to replace content with custom text.
 */
angular.module('qualitApp')
  .directive('qa', function (qaTextService) {
    return {
      template: '',
      restrict: 'E',
      scope: {
        qa: '=',
        variables: '=',
        context: '@',
        editable: '='
      },
      link: function postLink(scope, element, attrs) {
        scope.isEditable = function (type) {
          // default (if no parameter was given) editable is set to true
          var editable = (scope.editable != undefined ? scope.editable : true);

          // in project mode is everything editable
          if (context == "project") {
            return true;
          } else { // in catalog mode there must be a check for type and the parameter (editable)
            if (editable == false) {
              return false;
            } else {
              if (type == "FREETEXT") {
                return false;
              } else if (type == "FREENUMBER" || type == "ENUMTEXT" || type == "ENUMNUMBER") {
                return true;
              }
            }
          }
        }

        scope.getQaHtml = function (qa, variables) {
          var descriptionParts = qaTextService.splitVariables(qa.description);
          var qaHtml = "";
          var qaVars = _.sortBy(variables[qa.id], ['varIndex']);
          var qaVarIndex = 0;
          for (var i = 0; i < descriptionParts.length; i++) {
            var descriptionPart = descriptionParts[i];
            if (qaTextService.isVariable(descriptionPart)) {
              var variable = qaVars[qaVarIndex];
              var disabled = "";
              var isEditable = scope.isEditable(variable.type);

              if(!isEditable) {
                  disabled = "disabled='disabled'";
              }

              if (variable.type == "FREETEXT") {
                qaHtml += "<input type='text' placeholder='' " + disabled + "/>";
              } else if (variable.type == "FREENUMBER") {
                if (variable.valRange != undefined) {
                  var placeholderText = "Value must be between " + variable.valRange.min + " and " + variable.valRange.max;
                  var inputSize = placeholderText.length;
                } else {
                  var placeholderText = "";
                  var inputSize = 10; // default for numbers
                }
                qaHtml += "<input type='text' placeholder='" + placeholderText + "' size='" + inputSize + "'" + disabled + "/>";
              } else if (variable.type == "ENUMTEXT" || variable.type == "ENUMNUMBER") {
                qaHtml += "<select class='form-control' " + disabled + ">";
                qaHtml += "<option class='form-option' value=''>Select a value</option>";
                for (var j = 0; j < variable.values.length; j++) {
                  var selectedAttr = "";
                  // if there was a default value, make selection
                  selectedAttr = (variable.values[j].default ? "selected" : "");
                  qaHtml += "<option class='form-option' " + selectedAttr + ">" + variable.values[j].value + "</option>";
                }
                qaHtml += "</select>";
                if (variable.extendable) {
                  var extendablePlaceholderText = "or add a new value";
                  if (variable.min != undefined && variable.max != undefined) {
                    extendablePlaceholderText += " (between " + variable.min + " and " + variable.max + ")";
                  }
                  qaHtml += " or <input type='text' placeholder='" + extendablePlaceholderText + "'' size='" + extendablePlaceholderText.length + "'' />";
                }
              }
              qaVarIndex++;
            } else {
              // only append text
              qaHtml += descriptionPart;
            }
          }

          var qaHtmlDiv = $("<div/>", {
            html: qaHtml
          });
          return qaHtmlDiv;
        }

        var qa = scope.qa;
        var categories = qa.categories;
        var variables = scope.variables;
        var context = (scope.context != undefined ? scope.context : "");

        var qaId = "qa-" + qa.id;

        var categoriesDiv = $("<div/>", {
          class: "categories"
        }).appendTo(element);

        for (var i = 0; i < categories.length; i++) {
          var category = categories[i];
          var categorySpan = $("<span/>", {
            class: "label label-default",
            text: category.name
          }).appendTo(categoriesDiv);
        }

        var qaDiv = $("<div/>", {
          class: "col-sm-10"
        }).appendTo(element);


        if (context == "catalog") {
          var qaDescSpan = $("<span/>", {
            class: "description",
            html: scope.getQaHtml(qa, variables)
          }).appendTo(qaDiv);
        } else {
          alert("not implemented for context: " + context);
        }

        var qaCheckboxDiv = $("<div/>", {
          class: "col-sm-2"
        }).appendTo(element);

        var qaCheckboxDiv = $("<i/>", {
          class: "fa fa-check checkbox"
        }).appendTo(qaCheckboxDiv);
      }
    }
      ;
  })
;
