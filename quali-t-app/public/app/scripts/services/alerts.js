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
          title: 'Oh yeah!',
          content: content,
          type: 'success',
          show: true,
          container: "#alerts-container",
          duration: 5
        });
        return alert;
      },

      createError: function (status, content) {
        var alert = $alert({
          title: status + ': Oh no!',
          content: content,
          type: 'error',
          show: true,
          container: "#alerts-container",
          duration: 5
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
          title: 'Ups, an error ocurred!',
          content: message,
          type: 'error',
          show: true,
          container: "#alerts-container",
          duration: 5
        });
        return alert;
      }
    }
  });
