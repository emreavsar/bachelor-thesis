angular.module('qualitApp')
  .config(function ($provide) {

    $provide.decorator('taOptions', ['taRegisterTool', '$delegate', '$modal', '$injector',
      function (taRegisterTool, taOptions, $modal, $injector) {
        /**
         * TODO EMRE: CLEAN UP THE CODE HERE AND IN qa.js -> duplicate code
         */

          // all variables after adding them are saved here
        taOptions.variables = new Array();

        /**
         * Opens the modal view for adding a new variable to the quality attribute
         * @param type
         * @param editor
         */
        var openNewVariableModal = function (type, editor) {
          var rScope = $injector.get('$rootScope');
          var modalScope = rScope.$new(true);
          modalScope.type = type;
          modalScope.editor = editor;
          modalScope.savedSelection = rangy.saveSelection();
          modalScope.add = add;
          modalScope.addToList = addToList;
          modalScope.removeFromList = removeFromList;

          if (type == 'ENUMTEXT' || type == 'ENUMNUMBER') {
            modalScope.enumList = new Array();
          }

          // create new isolated scope for modal view
          var modal = $modal({
            scope: modalScope,
            template: "templates/add-var-modal.tpl.html"
          });
        }

        var add = function (editor, savedSelection, type, enumList, rangeMinValue, rangeMaxValue, defaultValue) {
          // one can restore the selection after the focus got lost (only needed when a modal view got opened)
          //if (type != "FREETEXT") {
            rangy.restoreSelection(savedSelection);
          //}

          var currentVariableNumber = ((editor.html.match(/%VARIABLE_/g) || []).length) - 1;
          var nextVarNumber = currentVariableNumber + 1;
          var enumText = "%VARIABLE_" + type + "_" + nextVarNumber + "%";

          var options = {};
          if (type == "FREENUMBER") {
            options.min = rangeMinValue;
            options.max = rangeMaxValue;
          } else if (type == "ENUM") {
            options.defaultValue = defaultValue;
          } else if (type == "ENUMPLUS") {
            options.defaultValue = defaultValue;
            options.min = rangeMinValue;
            options.max = rangeMaxValue;
          }

          taOptions.variables.push(getVariableObject(type, options));

          if (type == "FREETEXT" || type == "FREENUMBER") {
            editor.focussed = true;
            editor.wrapSelection("insertText", enumText + " ", false);
          }
        };

        var getVariableObject = function (type, options) {
          var variable = {
            type: type,
            number: taOptions.variables.length
          }

          // customer type specific content
          if (type == "FREENUMBER") {
            variable.min = options.min;
            variable.max = options.max;

          } else if (type == "ENUM") {
            variable.default = options.default;

          } else if (type == "ENUMPLUS") {
            variable.default = options.default;
            variable.min = options.min;
            variable.max = options.max;
          }
          return variable;
        }

        var addToList = function (type, enumElement, enumList) {
          enumList.push(enumElement);
          enumElement = "";
        }

        var removeFromList = function (type, enumElement, enumList) {
          enumList.splice(enumList.indexOf(enumElement));
        }

        taRegisterTool('addVarFreeText', {
          iconclass: "fa fa-font",
          buttontext: "Free Text Variable",
          action: function () {

            add(this.$editor(), rangy.saveSelection(), 'FREETEXT', new Array(), this.$editor(), null, null, null);
          }
        });

        taRegisterTool('addVarFreeNumber', {
          iconclass: "fa fa-list-ol",
          buttontext: "Free Number Variable",
          action: function () {
            openNewVariableModal('FREENUMBER', this.$editor());
          }
        });

        taRegisterTool('addVarEnumText', {
          iconclass: "fa fa-list-ol",
          buttontext: "Enum Text Variable",
          action: function () {
            openNewVariableModal('ENUMNUMBER', this.$editor());
          }
        });

        taRegisterTool('addVarEnumNumber', {
          iconclass: "fa fa-list-ol",
          buttontext: "Enum Number Variable",
          action: function () {
            openNewVariableModal('ENUMTEXT', this.$editor());
          }
        });

        taOptions.toolbar = [
          ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
          ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
          ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
          ['html', 'insertImage', 'insertLink', 'wordcount', 'charcount'],
          ['addVarFreeText', 'addVarFreeNumber', 'addVarEnumText', 'addVarEnumNumber']
        ];

        return taOptions;
      }
    ])
    ;
  });
