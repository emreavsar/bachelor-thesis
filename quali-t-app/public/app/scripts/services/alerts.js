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
      createSuccess: function (content, container) {
        var alert = $alert({
          title: 'Oh yeah!',
          content: content,
          placement: 'top-right',
          type: 'success',
          container: container,
          show: true
        });
        return alert;
      },

      createError: function (status, content, container) {
        var alert = $alert({
          title: status + ': Oh no!',
          content: content,
          placement: 'top-right',
          type: 'error',
          container: container,
          show: true,
        });
        return alert;
      }
    }
  });
