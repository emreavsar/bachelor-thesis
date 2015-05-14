'use strict';

/**
 * @ngdoc service
 * @name qualitApp.qaTextService
 * @description
 * # qaTextService
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('qaTextService', function () {
    var qaTextService = {};
    // TODO emre: refactor to configservice
    qaTextService.variableTypes = new Array("FREETEXT", "FREENUMBER", "ENUMTEXT", "ENUMNUMBER");

    qaTextService.getRegex = function () {
      var regex = "%VARIABLE_(";

      for (var i = 0; i < this.variableTypes.length; i++) {
        if (i > 0) {
          regex += "|";
        }
        regex += this.variableTypes[i];
      }

      regex += "){1}_\\d*%";
      return regex;
    }

    qaTextService.splitVariables = function (description) {
      return (description.split(new RegExp(this.getRegex(), "g")) || []);
    }

    qaTextService.isVariable = function (str) {
      return this.variableTypes.indexOf(str) > -1;
    }

    qaTextService.getVariableString = function (variable) {
      return "%VARIABLE_" + variable.type + "_" + variable.varIndex + "%";
    }

    qaTextService.getPopulatedQa = function (qa, values) {
      var populatedStr = "";

      var parts = this.splitVariables(qa.description);
      var actualVariableIndex = 0;
      for (var i = 0; i < parts.length; i++) {
        var part = parts[i];
        if (this.isVariable(part)) {
          // if its a variable, check if there is a value set for it, if not add the variableString
          var value = _.findWhere(values, {'varIndex': actualVariableIndex});
          if (value != undefined) {
            populatedStr += value.value;
          } else {
            populatedStr += "<i>"+this.getVariableString({
              type: part,
              varIndex: actualVariableIndex
            }) + "</i>";
          }
          actualVariableIndex++;
        } else {
          populatedStr += part;
        }
      }

      return populatedStr;
    }

    return qaTextService;
  });
