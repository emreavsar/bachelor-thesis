'use strict';

/**
 * @ngdoc service
 * @name qualitApp.alerts
 * @description
 * # alerts
 * Factory in the qualitApp.
 */
angular.module('qualitApp')
  .factory('alerts', function ($alert) {
    return {
      createSuccess: function (content) {
        var alert = $alert({
          title: '<i class="fa fa-check"></i> Success',
          content: content,
          type: 'success',
          show: true,
          container: "#alerts-container",
          duration: 3,
          html: true
        });
        return alert;
      },

      createError: function (status, content) {
        var alert = $alert({
          title: '<i class="fa fa-exclamation-triangle"></i> Error',
          content: content,
          type: 'error',
          show: true,
          container: "#alerts-container",
          duration: 5,
          html: true
        });
        return alert;
      },

      /**
       * Creates a local error message (without a status) and displays it.
       * Accepts an array or a string as the content of the alert box
       * @param content
       * @returns {*}
       */
      createLocalWarning: function (content) {
        var message = "";

        // if array was passed
        if (Object.prototype.toString.call(content) === '[object Array]') {
          _.forEach(content, function (n) {
            message += n + "<br/>";
          });
        } else {
          message = content;
        }
        var alert = $alert({
          title: '<i class="fa fa-exclamation-triangle"></i> Error',
          content: message,
          type: 'error',
          show: true,
          container: "#alerts-container",
          duration: 5,
          html: true
        });
        return alert;
      }
    }
  });
