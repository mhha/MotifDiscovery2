import java.util.Random;

public class Meme {
	
	static Integer NUM_OF_BASE = 4;
	static Integer MAX_ITERATION = 1000; 
	
	char[][]dataSeqs;
	
	int iteration;
	int numOfSeq;
	int[] seqLength;
	int maxLengthOfSeq;
	int numOfSubSeq;
	int width;
	
	double [][] profile;
	double [][] posZ;
	double [][] negZ;
	double posLambda;
	double[][] posP;
	double[][] negP;
	
	public void Run(){
		do{
			iteration++;
			M_Step();
			E_Step();			
		}while(iteration < MAX_ITERATION); // terminal condition ( delta-profile < epsilon) || (1000-times)
	}
	
	public Meme(Integer _width, char[][] _dataSeqs){
		this.width = _width;
		this.dataSeqs = _dataSeqs;
		
		numOfSeq = dataSeqs.length;
		seqLength = new int[numOfSeq];
		maxLengthOfSeq = 0;
		numOfSubSeq = 0;
		for(int i = 0; i< numOfSeq ; i++){
			seqLength[i] = dataSeqs[i].length;
			numOfSubSeq += seqLength[i]-width+1;
			if(maxLengthOfSeq<seqLength[i])maxLengthOfSeq=seqLength[i];		
		}
		Res.Init();
		iteration = 0;
		
		profile = new double[NUM_OF_BASE][width+1]; 
		posZ = getRndHiddenZ();
		negZ = getRndHiddenZ();
		posP = new double[numOfSeq][maxLengthOfSeq-width+1]; //pOfObservedDataGivenProfile
		negP = new double[numOfSeq][maxLengthOfSeq-width+1]; //pOfObservedDataGivenProfile
	}
	
	//E-step : estimate Z from profile, lambda
	private void E_Step(){
		for(int i = 0; i< numOfSeq; i++){
			for(int j = 0; j<seqLength[i]-width+1; j++){
				posP[i][j] = negP[i][j] = 1.0;
				for(int k = 0; k < width; k++){
					posP[i][j] *= profile[Res.basesMap.get(dataSeqs[i][j+k])][k+1];
					negP[i][j] *= profile[Res.basesMap.get(dataSeqs[i][j+k])][0];
				}
				double temp1, temp2, temp3;
				temp1 = posP[i][j] * posLambda;
				temp2 = negP[i][j] * (1.0 - posLambda);
				temp3 = temp1 + temp2;
				posZ[i][j] = temp1/temp3;
				negZ[i][j] = temp2/temp3;
	}	}	}
	
	//M-step : estimate profile, lambda from Z
	private void M_Step(){
		GetLambda();
		GetProfile();	
	}
	
	private void GetLambda(){
		for(int i = 0; i< numOfSeq; i++){
			for(int j = 0; j<seqLength[i]-width+1; j++) posLambda += posZ[i][j];
			posLambda /= (double)numOfSubSeq;
	}	}
	
	private void GetProfile(){
		for(double[] profileLine : profile)for(double profileEle : profileLine)profileEle = 0.0;	
		for(int i = 0; i <numOfSeq; i++){
			for(int j=0; j<seqLength[i]-width+1; j++){
				for(int k = 1; k <= width; k++)if(j-k+1 >= 0 && j-k+1 <= seqLength[i] - width ) {						
					profile[Res.basesMap.get(dataSeqs[i][j])][0] += negZ[i][j-k+1];
					profile[Res.basesMap.get(dataSeqs[i][j])][k] += posZ[i][j-k+1];	
		}	}	}
		for(int j = 0; j < width+1; j++) {
			double sum = 0.0;
			for(int i = 0; i < NUM_OF_BASE; i++)sum += profile[i][j];
			for(int i = 0; i < NUM_OF_BASE; i++)profile[i][j] /=sum;
	}	}
	
	private double[][] getRndHiddenZ(){
		double[][] hiddenZ = new double[numOfSeq][maxLengthOfSeq-width+1];
		Random randomGenerator = new Random();
		for(int i = 0; i< numOfSeq; i++){
			double total = 0.0;
			for(int j = 0; j < seqLength[i]-width+1; j++) total += hiddenZ[i][j] = randomGenerator.nextDouble();
			for(int j = 0; j < seqLength[i]-width+1; j++)hiddenZ[i][j] /= total; //normalization
		}
		return hiddenZ;
	}
	
	public void PrintProfile(){
		for(double[] profileLine: profile){
			String result = "";
			for(double profileEle : profileLine) result += profileEle + ", ";
			System.out.println(result);
		}
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


