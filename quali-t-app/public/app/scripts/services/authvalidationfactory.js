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
      },

      validatePasswords: function (newPassword, passwordRepeat) {
        var errors = new Array();

        if (newPassword == undefined) {
          errors.push("Please provide a new password.");
        }
        if (passwordRepeat == undefined) {
          errors.push("Please repeat the provided password.");
        }

        // if something was undefined no more validation needed, return here.
        if (errors.length > 0) {
          return errors;
        }

        if (newPassword.length < 8) {
          errors.push("Password must be longer than 8 characters.");
        }
        if (newPassword != passwordRepeat) {
          errors.push("Password must be equal!");
        }

        return errors;
      }
    };
  })
;
