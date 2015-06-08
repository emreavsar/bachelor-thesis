'use strict';

describe('catalog', function () {

  var testCatalogName = "A Test Catalog";
  var testCatalogDescription = "This will be added through integration tests";

  it('should create a new catalog', function () {
    browser.get('/#/authenticated/catalog/create');
    expect(browser.getLocationAbsUrl()).toMatch("/authenticated/catalog/create");

    // basic information about the catalog
    element(by.id("name")).sendKeys(testCatalogName);
    element(by.id("description")).sendKeys(testCatalogDescription);
    element(by.id("choose-qat-button")).click();

    // select a qa if there is one
    var qas = element.all(by.css('.qa'));
    qas.count().then(function (count) {
      var catalogContainsQas = count > 0;

      if (catalogContainsQas) {
        qas.get(0).click();
      }

      // create
      element(by.id("create-catalog-button")).click();

      // check
      expect(browser.getLocationAbsUrl()).toContain("/authenticated/catalog/edit");
      // if there were qas added to catalog, they should also appear in the created catalog
      if (catalogContainsQas) {
        expect(element.all(by.css("qa")).count()).toBeGreaterThan(0);
      } else {
        expect(element.all(by.css("qa")).count()).toBe(0);
      }
      expect(element(by.id("catalogName")).getAttribute('value')).toEqual(testCatalogName);
      expect(element(by.id("catalogDescription")).getAttribute('value')).toEqual(testCatalogDescription);
    });
  });
});
