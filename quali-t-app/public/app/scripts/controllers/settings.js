'use strict';

/**
 * @ngdoc function
 * @name qualitApp.controller:SettingsCtrl
 * @description
 * # SettingsCtrl
 * Controller of the qualitApp
 */
angular.module('qualitApp')
    .controller('SettingsCtrl', function ($scope, $http, alerts) {
        $scope.currentPassword;
        $scope.newPassword;
        $scope.newPasswordRepeated;

        $scope.changePasswords = function (currentPassword, newPassword, newPasswordRepeated) {
            // TODO client side validation of the form required here!


            $http.post('/api/auth/changePw', {
                currentPassword: currentPassword,
                newPassword: newPassword,
                newPasswordRepeated: newPasswordRepeated
            })
                .success(function (data, status, headers, config) {
                    $scope.currentPassword = "";
                    $scope.newPassword = "";
                    $scope.newPasswordRepeated = "";
                    var alert = alerts.createSuccess(data, "#alerts-container");
                })
                .error(function (data, status, headers, config) {
                    var alert = alerts.createError(status, data, "#alerts-container");
                });
        }
    });

