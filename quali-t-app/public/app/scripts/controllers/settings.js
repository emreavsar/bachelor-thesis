'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:SettingsCtrl
 * @description
 * # SettingsCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
  .controller('SettingsCtrl', function ($scope, apiService, alerts, authValidationFactory) {
    $scope.currentPassword = "";
    $scope.newPassword = "";
    $scope.newPasswordRepeated = "";

    $scope.changePasswords = function (currentPassword, newPassword, newPasswordRepeated) {
      var errors = authValidationFactory.validatePasswords(newPassword, newPasswordRepeated);

      if (errors.length == 0) {
        var changePwPromise = apiService.changePassword(currentPassword, newPassword, newPasswordRepeated);
        changePwPromise.then(function (payload) {
          $scope.currentPassword = "";
          $scope.newPassword = "";
          $scope.newPasswordRepeated = "";
          var alert = alerts.createSuccess(data);
        });

      } else {
        var alert = alerts.createError("Client error", errors.join(" "))
      }

    }
  });

