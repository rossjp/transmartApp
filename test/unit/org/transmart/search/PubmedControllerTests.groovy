package org.transmart.search

import grails.test.mixin.*

import org.junit.Before

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PubmedController)
class PubmedControllerTests {
	
	def PubmedController controller

	def expectedValues
	
	@Before
	void setUp() {
	  FakePubmedServiceForTest fk = new FakePubmedServiceForTest();
	  fk.setUpFakeData();

	  expectedValues = fk.expectedValues
	  
	  PubmedNCIBIService pubmedNCIBIService = fk

	  mockController(PubmedController)
	  controller = new PubmedController()
	  controller.setPubmedNCIBIService(pubmedNCIBIService)
	}
	
    void testIndex() {
		controller.params.length = "21"
		controller.params.geneids = "-1" // actual value is ignored in test
		controller.index()
		def results = model.doclets
		
		assertNotNull(results)
		assertTrue(!results.empty)
		
		for (def a in results){
			def int n = new Integer(a.pmid).intValue()
			assertTrue expectedValues.indexOf(n) > -1
		}

		// for debugging
//		for (doclet in results) {
//			println(doclet)
//		}
    }
}
