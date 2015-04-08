// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2015-03-10 using
// generator-karma 0.9.0

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '../',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      // bower:js
      'bower_components/jquery/dist/jquery.js',
      'bower_components/angular/angular.js',
      'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
      'bower_components/angular-touch/angular-touch.js',
      'bower_components/angular-loading-bar/build/loading-bar.js',
      'bower_components/ui-router/release/angular-ui-router.js',
      'bower_components/angular-aria/angular-aria.js',
      'bower_components/angular-material/angular-material.js',
      'bower_components/lodash/dist/lodash.compat.js',
      'bower_components/restangular/dist/restangular.js',
      'bower_components/angular-strap/dist/angular-strap.js',
      'bower_components/angular-strap/dist/angular-strap.tpl.js',
      'bower_components/angular-gravatar/build/angular-gravatar.js',
      'bower_components/FroalaWysiwygEditor/js/froala_editor.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/block_styles.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/colors.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/media_manager.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/tables.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/video.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/font_family.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/font_size.min.js',
      'bower_components/FroalaWysiwygEditor/js/plugins/char_counter.min.js',
      'bower_components/angular-froala/src/angular-froala.js',
      'bower_components/rangy/rangy-core.min.js',
      'bower_components/rangy/rangy-cssclassapplier.min.js',
      'bower_components/rangy/rangy-selectionsaverestore.min.js',
      'bower_components/rangy/rangy-serializer.min.js',
      'bower_components/textAngular/src/textAngular.js',
      'bower_components/textAngular/src/textAngular-sanitize.js',
      'bower_components/textAngular/src/textAngularSetup.js',
      'bower_components/angular-mocks/angular-mocks.js',
      // endbower
      'app/scripts/**/*.js',
      'test/mock/**/*.js',
      'test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [
    ],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      'PhantomJS'
    ],

    // Which plugins to enable
    plugins: [
      'karma-phantomjs-launcher',
      'karma-jasmine'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
