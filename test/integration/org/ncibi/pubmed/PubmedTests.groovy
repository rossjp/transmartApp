package org.ncibi.pubmed

import static org.junit.Assert.*
import org.junit.*

class PubmedTests {
	
	def expectedValues

    @Before
    void setUp() {
		expectedValues = [
			7828893,
			11850830,
			16078733
		]
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSomething() {
		def GENE_SYMBOL = 11137
		def LIMIT = 3
		
		def RenderUsingSax r = new RenderUsingSax()
		
		r.processDocument(GENE_SYMBOL,LIMIT)

		def results = r.arrayResults;

		assertNotNull results
		
		for (def a in results){
			def int n = new Integer(a.pmid).intValue()
			assertTrue expectedValues.indexOf(n) > -1
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
