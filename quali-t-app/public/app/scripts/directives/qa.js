'use strict';

/**
 * @ngdoc directive
 * @name qualitApp.directive:qa
 * @description
 * # qa
 * Directive to replace content with custom text.
 */
angular.module('qualitApp')
  .directive('qa', function (qaTextService, $modal, apiService, alerts, $state) {
    return {
      template: '',
      restrict: 'E',
      scope: {
        qa: '=',
        catalogQa: '=',
        variables: '=',
        qualityproperties: '=',
        values: '=',
        jiraUrl: '=',
        context: '@',
        editable: '=',
        updateQaFunction: '&',
        toggleQpStatusFunction: '&'
      },
      link: function postLink(scope, element, attrs) {
        scope.isEditable = function (type) {
          // default (if no parameter was given) editable is set to true
          var editable = (scope.editable != undefined ? scope.editable : true);

          if (editable == true) {
            return editable;
          }

          // in project mode is everything editable
          if (context == "editproject" || context == "editqa") {
            return true;
          } else if (context == "createproject") { // in create project everything is disabled
            return false;
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


        scope.getQpHtml = function (qualityPropertyStatuses, qaId) {
          var qpsTable = $("<table/>", {
            class: 'quality-properties qp-table'
          });
          var qpsTr = $("<tr/>").appendTo(qpsTable);

          for (var i = 0; i < qualityPropertyStatuses.length; i++) {
            var qualityPropertyStatus = qualityPropertyStatuses[i];

            var qpsTd = $("<td/>", {}).appendTo(qpsTr);

            var qpCheckbox = $("<input/>", {
              type: 'checkbox',
              checked: qualityPropertyStatus.status,
              title: 'Toggle status of quality property: '
              + qualityPropertyStatus.qp.name + ' - ' + qualityPropertyStatus.qp.description,
              'data-qa-id': qaId,
              'data-qp-id': qualityPropertyStatus.id
            }).appendTo(qpsTd);

            $(qpCheckbox).click(function () {
              if (scope.toggleQpStatusFunction() != undefined) {
                scope.toggleQpStatusFunction()($(this).data('qa-id'), $(this).data('qp-id'), $(this).is(':checked'));
              }
            });
          }

          return qpsTable;
        }

        scope.getQaHtml = function (qa, variables, values) {
          var descriptionParts = qaTextService.splitVariables(qa.description);
          var qaHtml = "";
          var qaVars = _.sortBy(variables, ['varIndex']);
          var values = _.sortBy(values, ['varIndex']);
          var qaVarIndex = 0;

          for (var i = 0; i < descriptionParts.length; i++) {
            var descriptionPart = descriptionParts[i];
            if (qaTextService.isVariable(descriptionPart)) {
              var variable = qaVars[qaVarIndex];
              var isEditable = scope.isEditable(variable.type);

              // save value data in the input/select
              var value = values[qaVarIndex]

              if (value != undefined) {
                var varOptions = {
                  "data-for-variable": qaTextService.getVariableString(variable),
                  "data-value-id": value.id,
                  "data-value-varIndex": value.varIndex,
                  "data-value-current": value.value
                };
                var value = " value='" + value.value + "'";
              } else {
                // TODO check if this is needed or not!
                var varOptions = {};
                var varOptions = {
                  "data-for-variable": qaTextService.getVariableString(variable),
                  "data-value-id": "",
                  "data-value-varIndex": variable.varIndex,
                  "data-value-current": ""
                };
                var value = " value=''";
              }

              if (!isEditable) {
                varOptions.disabled = "disabled";
              }

              if (variable.type == "FREETEXT") {
                varOptions.type = "text";
                if (isEditable) {
                  varOptions.value = varOptions["data-value-current"];
                }
                var variableContainer = $("<input/>", varOptions);
              } else if (variable.type == "FREENUMBER") {
                if (variable.valRange != undefined) {
                  var placeholderText = "Value must be between " + variable.valRange.min + " and " + variable.valRange.max;
                  varOptions.placeHolder = placeholderText;
                  varOptions.size = placeholderText.length;
                } else {
                  varOptions.size = 10; // default for numbers
                }

                if (isEditable) {
                  varOptions.value = varOptions["data-value-current"];
                }

                varOptions.type = "text";
                var variableContainer = $("<input/>", varOptions);
              } else if (variable.type == "ENUMTEXT" || variable.type == "ENUMNUMBER") {
                var extendableCSSClass = "";
                if (variable.extendable) {
                  extendableCSSClass = "isExtendable"
                }

                var variableContainer = $("<p/>", {
                  class: extendableCSSClass
                });

                varOptions.class = "form-control";
                var select = $("<select/>", varOptions).appendTo(variableContainer);

                var firstOption = $("<option/>", {
                  class: "form-option",
                  value: "",
                  text: "Select a value"
                }).appendTo(select);

                for (var j = 0; j < variable.values.length; j++) {
                  var selectOption = {
                    class: "form-option",
                    value: variable.values[j].value,
                    text: variable.values[j].value
                  };
                  // if there was a default value or a selection was done, preselect
                  if (isEditable) {

                    if (varOptions["data-value-current"] != "") {
                      if (variable.values[j].value == varOptions["data-value-current"]) {

                        selectOption.selected = "selected";
                      }
                    } else if (variable.values[j].default) {
                      selectOption.selected = "selected";
                    }
                  }

                  var option = $("<option/>", selectOption).appendTo(select);
                }

                if (variable.extendable) {
                  var extendablePlaceholderText = " or add a new value";

                  if (variable.min != undefined && variable.max != undefined) {
                    extendablePlaceholderText += " (between " + variable.min + " and " + variable.max + ")";
                  }

                  varOptions.class = "";
                  varOptions.type = "text";
                  varOptions.placeholder = extendablePlaceholderText;
                  varOptions.size = extendablePlaceholderText.length;
                  var extensionField = $("<input/>", varOptions).appendTo(variableContainer);
                }
              }

              qaHtml += variableContainer[0].outerHTML;
              qaVarIndex++;
            } else {
              // only append text
              qaHtml += descriptionPart;
            }
          }

          if (context == "editproject") {
            var qaHtmlDivClass = "col-sm-11";
          }
          var qaHtmlDiv = $("<div/>", {
            class: qaHtmlDivClass,
            html: qaHtml
          });

          return qaHtmlDiv;
        }

        var qa = scope.qa;
        var categories = (qa.categories != undefined ? qa.categories : new Array());
        var variables = (scope.variables != undefined ? scope.variables : new Array());
        var qualityproperties = (scope.qualityproperties != undefined ? scope.qualityproperties : new Array());
        var values = (scope.values != undefined ? scope.values : new Array());

        // sort by qp id
        qualityproperties = _.sortBy(qualityproperties, function (n) {
          return n.qp.id
        });
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

        var qaDivCSSClass = "col-sm-12";
        if (context == "editproject") {
          qaDivCSSClass = "col-sm-8";
        } else if (context == "editcatalog") {
          qaDivCSSClass = "col-sm-11";
        } else if (context == "createproject" || context == "catalog") {
          qaDivCSSClass = "col-sm-10";
        }
        var qaDiv = $("<div/>", {
          class: qaDivCSSClass
        }).appendTo(element);

        // show the text with variables inside
        var qaHtml = "";
        if (context == "editproject") {
          qaHtml = qaTextService.getPopulatedQa(qa, values);
        } else if (context == "editcatalog") {
          qaHtml = qa.description;
        } else {
          qaHtml = scope.getQaHtml(qa, variables, values);
        }
        var qaDescSpan = $("<span/>", {
          class: "description",
          html: qaHtml
        }).appendTo(qaDiv);


        if (context == "editproject" || context == "editcatalog") {
          // in editcatalog there is a catalogQa provided
          var catalogQa = (scope.catalogQa != undefined ? scope.catalogQa : {});

          var actions = $("<div/>", {
            class: "col-sm-1 actions"
          });

          var editBtn = $("<i/>", {
            title: "Edit quality attribute",
            class: "fa fa-cog pointer"
          }).appendTo(actions);

          editBtn.click(function () {
            if (context == "editproject") {
              var modalScope = scope.$new(true);
              modalScope.qa = qa;
              modalScope.context = context;

              var modal = $modal({
                title: "Edit qa",
                scope: modalScope,
                template: "templates/edit-qa-modal.tpl.html"
              });
            } else if (context == "editcatalog") {
              // go to edit mode of qa
              $state.go('editQA', {
                catalogQa: catalogQa.id
              });

              // move textangular box to current edited qa
              //$(".currentEditedQa").detach().appendTo("#qa-" + catalogQa.id + " .textangular-box");
              //$(".currentEditedQa, .textangular-box ").removeClass("hidden");

              // set the textangular's properties to currentEditedQas description
              //$(".currentEditedQa div[id^='taTextElement']").html(catalogQa.qa.description);
            }
          });

          var cloneBtn = $("<i/>", {
            title: "Clone quality attribute",
            class: "fa fa-files-o pointer"
          }).appendTo(actions);

          cloneBtn.click(function () {
            var promiseRemove;
            if (context == "editproject") {
              promiseRemove = apiService.cloneQaInstance(qa.id);
            } else if (context == "editcatalog") {
              promiseRemove = apiService.cloneCatalogQa(catalogQa.id);
            }
            promiseRemove.then(
              function (payload) {
                alerts.createSuccess("Quality Attribute was successfully cloned.");
                if (scope.updateQaFunction() != undefined) {
                  // reload information
                  scope.updateQaFunction()();
                }
              });
          });

          var deleteBtn = $("<i/>", {
            title: "Delete quality attribute",
            class: "fa fa-trash-o pointer"
          }).appendTo(actions);

          deleteBtn.click(function () {
            var promiseRemove;
            if (context == "editproject") {
              promiseRemove = apiService.removeQaInstance(qa.id);
            } else if (context == "editcatalog") {
              promiseRemove = apiService.removeQa(qa.id);
            }
            promiseRemove.then(
              function (payload) {
                alerts.createSuccess("Quality Attribute was successfully removed.");
                if (scope.updateQaFunction() != undefined) {
                  // reload information
                  scope.updateQaFunction()();
                }
              });
          });

          if (context == "editproject") {
            var checkbox = $("<input/>", {
              type: "checkbox",
              title: "Toggle export to issue tracking system",
              class: "col-sm-1 export-checkbox",
              "data-qainstanceid": qa.id
            }).prependTo(qaDescSpan);

            var qaCheckboxDiv = $("<div/>", {
              class: "col-sm-3",
              html: scope.getQpHtml(qualityproperties, qa.id)
            });

            var validationWarnings = $("<div/>", {
              class: "col-sm-1 pull-right validation-warnings hidden",
              title: "This quality attribute has validation warnings, click here to get more information."
            });

            var validationWarningsInfoIcon = $("<i/>", {
              class: "fa fa-exclamation-triangle"
            }).appendTo(validationWarnings);

            var issueTrackerInfo = $("<div/>", {
              class: "issue-tracker-info col-sm-12",
              html: " <i class='fa fa-link'></i> JIRA Issue: "
            });

            // add link to jira issue
            var jiraUrl = (scope.jiraUrl != undefined ? scope.jiraUrl : "");
            if (qa.jiraKey != undefined && qa.jiraKey != null && qa.jiraKey != "") {
              var linkToIssue = $("<a/>", {
                href: jiraUrl + "/browse/" + qa.jiraKey,
                target: "_blank",
                text: qa.jiraKey
              }).appendTo(issueTrackerInfo);
            } else {
              var noJiraText = $("<span/>", {
                text: "Not exported to JIRA",
                class: "no-issue-text"
              }).appendTo(issueTrackerInfo);
            }
          }

          // the order here is important!
          if (context == "editproject") {
            $(qaCheckboxDiv).appendTo(element);
          }

          $(actions).appendTo(element);

          if (context == "editproject") {
            $(validationWarnings).appendTo(element);
            $(issueTrackerInfo).appendTo(element);
            $(validationWarnings).appendTo(element);
          } else if (context == "editcatalog") {
            $("<div/>", {
              class: "textangular-box hidden"
            }).appendTo(element);
          }
        } else if (context == "createproject" || context == "catalog") {

          var qaCheckboxDiv = $("<div/>", {
            class: "col-sm-2"
          }).appendTo(element);

          var qaCheckboxDiv = $("<i/>", {
            class: "fa fa-check checkbox"
          }).appendTo(qaCheckboxDiv);

        }
      }
    }
  });
