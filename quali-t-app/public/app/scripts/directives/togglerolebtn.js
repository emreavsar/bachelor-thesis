'use strict';

/**
 * @ngdoc directive
 * @name qualitApp.directive:toggleRoleBtn
 * @description
 * # toggleRoleBtn
 * Directive to replace content with custom text.
 */
angular.module('qualitApp')
  .directive('toggleRoleBtn', function () {
    return {
      restrict: "A",
      link: function (scope, elem, attrs) {
        //On click
        $(elem).click(function () {
          var roleChangerIsVisible = $("#role-changer").is(":visible");
          $("#role-changer").slideToggle('fast');

          if(roleChangerIsVisible) {
            $(this).find(".fa-angle-up").removeClass("fa-angle-up").addClass("fa-angle-down");
          } else {
            $(this).find(".fa-angle-down").removeClass("fa-angle-down").addClass("fa-angle-up");
          }
        });
      }
    }
  });
