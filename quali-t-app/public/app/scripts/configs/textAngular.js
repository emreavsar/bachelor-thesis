angular.module('qualitApp')
  .config(function ($provide) {

    // this demonstrates how to register a new tool and add it to the default toolbar
    $provide.decorator('taOptions', ['taRegisterTool', '$delegate', function (taRegisterTool, taOptions) { // $delegate is the taOptions we are decorating
      taRegisterTool('test', {
        buttontext: 'Test',
        action: function () {
          alert('Test Pressed')
        }
      });
      taOptions.toolbar[1].push('test');
      taRegisterTool('colourRed', {
        iconclass: "fa fa-square red",
        action: function () {
          this.$editor().wrapSelection('forecolor', 'red');
        }
      });
      // add the button to the default toolbar definition
      taOptions.toolbar[1].push('colourRed');

      taOptions.toolbar = [
        ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
        ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
        ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
        ['html', 'insertImage','insertLink', 'wordcount', 'charcount']
      ];

      return taOptions;
    }]);


  });
