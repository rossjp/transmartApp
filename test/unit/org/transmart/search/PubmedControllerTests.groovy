package org.transmart.search

import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PubmedController)
class PubmedControllerTests {
	
	def pubmedNCIBIService

	@Before
	void setUp() {
	  pubmedNCIBIService = new FakePubmedServiceForTest();
	}
	
    void testSomething() {
		
    }
}
