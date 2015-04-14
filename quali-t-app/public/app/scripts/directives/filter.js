'use strict';

/**
 * @ngdoc directive
 * @name qualitApp.directive:filter
 * @description
 * # filter
 * Directive to replace content with custom text.
 */
angular.module('qualitApp')
  .directive('filter', function ($location) {
    return {
      scope: {
        hideCheckbox: '&',
        callback: '&',
        categories: '&'
      },
      template: '<div class="panel-group id=" id="filter"></ul>',
      restrict: 'E',

      link: function postLink(scope, element, attrs) {
        scope.createPanelHeading = function (category) {
          var panel = $('#category-' + category.name);
          var panelHeading = $('<div/>', {
            class: 'panel-heading'
          }).appendTo(panel);

          var h4 = $('<h4/>', {
            class: 'panel-title'
          }).appendTo(panelHeading);

          var h4Link = $('<a/>', {
            'data-toggle': "collapse",
            'data-parent': "#accordion",
            href: $location.absUrl() + "#collapse-" + category.name,
            text: category.name,
            onClick: 'return false;'
          }).appendTo(h4);

          var h4Icon = $('<i/>', {
            class: category.icon + ' category-icon'
          }).prependTo(h4Link);
        }

        scope.createPanelBody = function (category, isFirstCategory) {
          var panel = $('#category-' + category.name);

          var collapsePanel = $('<div/>', {
            id: "collapse-" + category.name,
            class: "panel-collapse collapse"
          }).appendTo(panel);

          if (isFirstCategory) {
            $(collapsePanel).addClass("in");
          }

          var ul = $('<ul/>', {
            class: 'list-group',
            css: {
              'padding-left': '0px'
            }
          }).appendTo(collapsePanel);


          for (var i = 0; i < category.categories.length; i++) {
            var subCategory = category.categories[i];
            var paddingLeft = $(ul).css('padding-left').split('px')[0] * 1 + 15 + "px";
            scope.createPanelLink(subCategory, ul, paddingLeft);
          }
        }

        scope.createPanelLink = function (category, container, paddingLeft) {
          var subCategories = new Array();
          if (category.categories != undefined) {
            subCategories = category.categories;
          }

          var li = $('<li/>', {
            class: 'list-group-item',
            'data-id': category.id,
            css: {
              'padding-left': paddingLeft
            },
            text: category.name
          }).appendTo(container);


          $(li).click(function (e) {
            scope.callback()(this);
          });

          if (!scope.hideCheckbox()) {
            var checkBox = $('<input/>', {
              type: 'checkbox'
            }).prependTo(li);

            $(checkBox).click(function (e) {
              e.stopPropagation();
            });
          }

          if (subCategories.length > 0) {
            var ul = $('<ul/>', {
              class: 'list-group'
            }).appendTo(container);

            for (var i = 0; i < subCategories.length; i++) {
              var subCat = subCategories[i];
              var paddingLeft = paddingLeft.split('px')[0] * 1 + 15 + "px";
              scope.createPanelLink(subCat, ul, paddingLeft);
            }
          }
        }

        var categories = scope.categories();
        for (var i = 0; i < categories.length; i++) {
          var category = categories[i];

          var panel = $('<div/>', {
            id: 'category-' + category.name,
            title: category.name,
            class: 'category panel panel-default'
          }).appendTo('#filter');
          scope.createPanelHeading(category);
          scope.createPanelBody(category, i == 0);
        }
      }

    };
  });
