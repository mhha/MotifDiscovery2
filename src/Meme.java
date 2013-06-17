import java.util.Random;

public class Meme {
	
	//static Integer MAX_LENGTH_OF_SEQ ;
	static Integer NUM_OF_BASE = 4;
	static Integer MAX_ITERATION = 1000; 
	
	public static double[][] EM(Integer width, char[][] dataSeqs){
		
		
		int numOfSeq = dataSeqs.length;
		int[] seqLength = new int[numOfSeq];
		int maxLengthOfSeq = 0;
		for(int i = 0; i< numOfSeq ; i++){
			seqLength[i] = dataSeqs[i].length;
			if(maxLengthOfSeq<seqLength[i])maxLengthOfSeq=seqLength[i];		
		}
		Res.PutBasesVal();
		double[][] profile = new double[NUM_OF_BASE][width+1]; 
		double[][] hiddenZ = getRndHiddenZ(numOfSeq, width, seqLength, maxLengthOfSeq);
				
		int iteration = 0;
		do{
			iteration++;
			//M-step : estimate profile from hiddenZ
			profile = M_Step(profile, hiddenZ, numOfSeq, width, seqLength, dataSeqs);
			//E-step : estimate hiddenZ from profile
			hiddenZ = E_Step(profile, hiddenZ, numOfSeq, width, seqLength, dataSeqs);			
		}while(iteration < MAX_ITERATION); // terminal condition ( delta-profile < epsilon) || (1000-times)
		return profile;
	}
	//E-step : estimate hiddenZ from profile
	private static double [][] E_Step( double[][] profile, double[][] hiddenZ, int numOfSeq, int width, int[] seqLength, char[][] dataSeq){
		for(int i = 0; i< numOfSeq; i++){
			double backgroundZ = 1.0;
			for(int j = 0; j<seqLength[i]; j++)backgroundZ *= profile[Res.basesMap.get(dataSeq[i][j])][0]*(double)NUM_OF_BASE;
			double sum = 0.0;
			for(int j = 0; j<seqLength[i]-width; j++){
				hiddenZ[i][j] = backgroundZ;
				for(int k = 0; k < width; k++){
					hiddenZ[i][j] /= profile[Res.basesMap.get(dataSeq[i][j+k])][0];
					hiddenZ[i][j] *= profile[Res.basesMap.get(dataSeq[i][j+k])][k+1];
				}
				sum += hiddenZ[i][j];
			}
			for(int j = 0; j<seqLength[i]-width; j++)hiddenZ[i][j] /= sum;
		}
		return hiddenZ;
	}
	//M-step : estimate profile from hiddenZ
	private static double [][] M_Step(double[][] profile, double[][] hiddenZ, int numOfSeq, int width, int[] seqLength, char[][] dataSeq){
		for(double[] profileLine : profile)for(double profileEle : profileLine)profileEle = 0.0;		
		for(int i = 0; i <numOfSeq; i++){
			for(int j = 0; j < seqLength[i]; j++){				 
				for(int k = 1; k <= width; k++){
					if(j-k+1 >= 0 && j-k+1 <= seqLength[i] - width ) {
						profile[Res.basesMap.get(dataSeq[i][j])][0] += (1.0-hiddenZ[i][j-k+1]);
						profile[Res.basesMap.get(dataSeq[i][j])][k] += hiddenZ[i][j-k+1];	
					}
		}	}	}
		for(int j = 0; j < width+1; j++) {
			double sum = 0.0;
			for(int i = 0; i < NUM_OF_BASE; i++)sum += profile[i][j];
			for(int i = 0; i < NUM_OF_BASE; i++)profile[i][j] /=sum;
		}
		return profile;
	}
	
	private static double[][] getRndHiddenZ(int numOfSeq, int width, int[] seqLength, int maxLengthOfSeq){
		
		double[][] hiddenZ = new double[numOfSeq][maxLengthOfSeq-width+1];
		Random randomGenerator = new Random();
		for(int i = 0; i< numOfSeq; i++){
			double total = 0.0;
			for(int j = 0; j < seqLength[i]-width+1; j++) total += hiddenZ[i][j] = randomGenerator.nextDouble();
			for(int j = 0; j < seqLength[i]-width+1; j++)hiddenZ[i][j] /= total; //normalization
		}
		return hiddenZ;
	}
	
	/*
	 * discarded
	 * 
	private static double[] GetInitBackgroundDNA(List<String> dataSet){
		int[] baseCnts = new int[NUM_OF_BASE];
		for(int baseCnt : baseCnts) baseCnt = 0;
		for(String dataSeq : dataSet){
			for(char base : dataSeq.toCharArray()){
				switch(base){
					case 'A' : baseCnts[0]++;
					case 'T' : baseCnts[1]++;
					case 'G' : baseCnts[2]++;
					case 'C' : baseCnts[3]++;					
				}
			}
		}
		int total = 0;
		for(int baseCnt : baseCnts) total += baseCnt;
		double[] background = new double[4];
		for(int i = 0; i < 4; i++) background[i] = (double)baseCnts[i]/(double)total;
		return background; 
	}
 	private static Hashtable<Character, Double> GetInitBackground(List<String> dataSet){
		Hashtable<Character, Double> baseCntsTable = new Hashtable();
		for(String dataSeq : dataSet){
			for(char base : dataSeq.toCharArray()){
				if(baseCntsTable.containsKey(base)){
					baseCntsTable.put(base, baseCntsTable.get(base)+1.0);
				}else{
					baseCntsTable.put(base, 0.0);
				}
			}
		}
		Double total = 0.0;
		Enumeration<Double> e = baseCntsTable.elements();
		while(e.hasMoreElements()){
			total += e.nextElement();			
		}
		Enumeration<Character> k = baseCntsTable.keys();
		while(k.hasMoreElements()){
			Character key = k.nextElement();
			baseCntsTable.put(key, (Double)baseCntsTable.get(key)/total);
		}
		return baseCntsTable;
	}
	*
	*/
}


