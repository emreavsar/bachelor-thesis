'use strict';

/**
 * @ngdoc service
 * @name qualitApp.authValidationFactory
 * @description
 * # authValidationFactory
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('authValidationFactory', function () {

    return {
      validateRegistration: function (username, password, passwordRepeat) {
        var errors = new Array();

        if (password != passwordRepeat) {
          errors.push("Passwords must be equal!");
        }

        return errors;
      }
    };
  });
