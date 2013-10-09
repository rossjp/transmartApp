package org.transmart.search

class FakePubmedServiceForTest extends PubmedNCIBIService {
	
	def fakeData
	
	def expectedValues = [
			7828893,
			11850830,
			16078733
		]


	def setUpFakeData() {
		def results = []
		def part = [:]
		part.pmid = "7828893"
		part.paragraph = "We have cloned and expressed in vaccinia virus a cDNA encoding an ubiquitous 501-amino-acid (aa) phosphoprotein that corresponds to protein <a class='gene' href='pubmedSite?geneid=11137'>IEF SSP 9502</a> (79,400 Da, pI 4.5) in the master 2-D-gel keratinocyte protein database [Celis et al., Electrophoresis 14 (1993) 1091-1198].Database searching indicated that <a class='gene' href='pubmedSite?geneid=11137'>IEF SSP 9502</a> is a putative human homologue of the Saccharomyces cerevisiae periodic Trp protein, <a class='gene' href='pubmedSite?geneid=11137'>PWP1</a> , a polypeptide that may play a regulatory role in cell growth and/or transcription."
		results.add(part)
		part.pmid = "11850830"
		part.paragraph = "The transcript encoding endonuclein, the human homolog of yeast <a class='gene' href='pubmedSite?geneid=11137'>PWP1</a> , was previously found up-regulated in pancreatic cancer tissue."
		results.add(part)
		part.pmid = "16078733"
		part.paragraph = "DNA sequencing of the coding exons of six candidate genes (CRY1, <a class='gene' href='pubmedSite?geneid=11137'>PWP1</a> , ASCL4, PRDM4, KIAA0789 and CMKLR1) on the basis of their location in the critical overlap interval, failed to detect any mutation in DSAP patients."
		results.add(part)
		fakeData = results;
	}
	
	@Override
	def getPubmedResultsByGene(geneid) {
		return fakeData;
	}
}
