exports.config = {
  // The address of a running selenium server.
  seleniumPort: 4444,
  seleniumServerJar: '../node_modules/protractor/selenium/selenium-server-standalone-2.45.0.jar',
  //directConnect: true,

  // Spec patterns are relative to the location of this config.
  specs: [
    'e2e/*_spec.js'
  ],

  framework: 'jasmine2',

  capabilities: {
    'browserName': 'chrome',
    'chromeOptions': {'args': ['--disable-extensions']}
  },


  // A base URL for your application under test. Calls to protractor.get()
  // with relative paths will be prepended with this.
  baseUrl: 'http://localhost:9000/app/index.html#/',

  jasmineNodeOpts: {
    isVerbose: false,
    showColors: true,
    includeStackTrace: true,
    defaultTimeoutInterval: 60000
  }
};
