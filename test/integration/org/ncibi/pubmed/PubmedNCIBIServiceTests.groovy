package org.ncibi.pubmed

import grails.test.mixin.TestFor

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.transmart.search.PubmedNCIBIService

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PubmedNCIBIService)
class PubmedNCIBIServiceTests {

	def expectedValues = []
	def PubmedNCIBIService pubmedNCIBIService
	def PubmedQueryExamplar examplar
				
	@Before
	void setUp() {
		examplar = new PubmedQueryExamplar() 
		expectedValues = examplar.expectedValues
		pubmedNCIBIService = new PubmedNCIBIService()
	}

	@After
	void tearDown() {
	}
	
	@Test
    void testDocFromServer() {
		int geneid = examplar.geneid
		int length = examplar.length
		
		def results = []
		results = pubmedNCIBIService.getPubmedResultsByGene(geneid,length)
		log.info("PubmedNCIBIServiceTests.testDocFromServer - "
				+ "length parameter = " + length + "; "
				+ "results size = " + results.size())
    }

	@Test
	void testSupportClass() {
		int geneid = examplar.geneid
		int length = examplar.length
		
		def RenderUsingSax r = new RenderUsingSax()
		
		r.processDocument(geneid,length)

		def results = []
		results = r.arrayResults;
		
		println(results.size())
		
		Assert.assertTrue(results != null)
		Assert.assertTrue(results.size() > 0)

		for (def a in results){
			def int n = new Integer(a.pmid).intValue()
			Assert.assertTrue expectedValues.indexOf(n) > -1
		}
		
		//for debugging
//		print "def results = []\n"
//		print "def part = []\n"
//		for (def a in results){
//			print "part.pmid = " + a.pmid + "\n"
//			print "part.paragraph = \"" + a.pargraph + "\"\n"
//			print "results.add(part)"
//		}
	}
}
