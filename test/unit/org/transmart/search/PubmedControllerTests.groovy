package org.transmart.search

import grails.test.mixin.TestFor

import org.junit.Before
import org.junit.Test

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PubmedController)
class PubmedControllerTests {
	
	def PubmedController controller
	def FakePubmedServiceForTest fk

	def expectedValues
	
	@Before
	void setUp() {
	  fk = new FakePubmedServiceForTest();
	  fk.setUpFakeData();

	  expectedValues = fk.expectedValues
	  
	  PubmedNCIBIService pubmedNCIBIService = fk

	  mockController(PubmedController)
	  controller = new PubmedController()
	  controller.setPubmedNCIBIService(pubmedNCIBIService)
	}
	
	@Test
    void testIndex() {
		controller.params.length = fk.inputLength
		controller.params.geneids = fk.inputGeneid // currently, actual value is ignored in test
		controller.index()
		def results = model.doclets
		
		assertTrue(results != null)
		assertTrue(results.size() > 0)
		
		// for debugging
//		for (doclet in results) {
//			println(doclet)
//		}

		for (def a in results){
			def int n = new Integer(a.pmid).intValue()
			assertTrue expectedValues.indexOf(n) > -1
		}

    }
}
