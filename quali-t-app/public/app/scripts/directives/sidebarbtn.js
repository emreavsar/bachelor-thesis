'use strict';

/**
 * @ngdoc directive
 * @name qualitApp.directive:sidebarBtn
 * @description
 * # sidebarBtn
 * Directive to replace content with custom text.
 */
angular.module('qualitApp')
  .directive('sidebarBtn', function () {
    return {
      restrict: "A",
      link: function (scope, elem, attrs) {
        //On click
        $(elem).click(function () {
          // if sidebar close btn is visible
          if ($(this).hasClass('fa-angle-left')) {
            // save height to adjust it (keep the same height on the content-container)
            var heightContent = $(".main-content").outerHeight();
            var paddingSidebarBtn = $('.toggle-sidebar-btn').css('padding');

            // close sidebar
            $("#sidebar").addClass('col-sm-0');
            $(".main-content").addClass('col-sm-12');
            $(".main-view").removeClass("main-view-opened");

            // change btn to open
            $(this).removeClass('fa-angle-left').addClass('fa-angle-right').addClass('toggle-sidebar-btn-closed');

            // fix height and some positioning
            $(".main-content").css('min-height', heightContent);
            $(".toggle-sidebar-btn-closed").css('padding', paddingSidebarBtn);
          } else {
            // hide sidebar
            $("#sidebar").removeClass('col-sm-0');
            $(".main-content").removeClass('col-sm-12');
            $(".main-view").addClass("main-view-opened");

            // change btn to open
            $(this).removeClass('fa-angle-right').addClass('fa-angle-left').removeClass('toggle-sidebar-btn-closed');
            $(".main-content").css('min-height', '');
          }
        });
      }
    }
  });
