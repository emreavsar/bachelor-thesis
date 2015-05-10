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

    return qaTextService;
  });
