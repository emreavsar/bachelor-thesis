angular.module('qualitApp')
  .config(function ($provide) {

    $provide.decorator('taOptions', ['taRegisterTool', '$delegate', '$modal', '$injector',
      function (taRegisterTool, taOptions, $modal, $injector) {
        // all variables after adding them are saved here
        taOptions.variables = new Array();
        taOptions.lastUsedVariableNumber = 0;

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

          if (type == 'ENUM') {
            modalScope.enumList = new Array();
          }

          // create new isolated scope for modal view
          var modal = $modal({
            scope: modalScope,
            template: "templates/add-var-modal.tpl.html"
          });
        }

        // TODO emre: is this desired in every textangular instance or not?
        taRegisterTool('addVarFree', {
          iconclass: "fa fa-font",
          buttontext: "Freetext Variable",
          action: function () {
            openNewVariableModal('FREE', this.$editor());
          }
        });

        taRegisterTool('addVarEnum', {
          iconclass: "fa fa-list-ol",
          buttontext: "Dropdown Variable",
          action: function () {
            openNewVariableModal('ENUM', this.$editor());
          }
        });

        taOptions.toolbar = [
          ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
          ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'clear'],
          ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
          ['html', 'insertImage', 'insertLink', 'wordcount', 'charcount'],
          ['addVarFree', 'addVarEnum']
        ];

        return taOptions;
      }
    ])
    ;
  });
