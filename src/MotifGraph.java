
public class MotifGraph {
	
	static int[][] hammingDist;
	static int numOfNode;
	
	public MotifGraph(char[][] dataSeq, int width, int[] seqLength){
		numOfNode = 0;
		for(int len : seqLength) numOfNode += len - width + 1;
		hammingDist = new int[numOfNode][numOfNode];
		
		GenGraph(dataSeq, width, seqLength);
	}
	
	public void GenGraph(char[][] dataSeq, int width, int[] seqLength){
		
	
	}
}
