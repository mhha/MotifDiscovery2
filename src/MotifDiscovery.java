

public class MotifDiscovery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[][] dataSeqs = DataIO.ReadDataSp(Res.drFiles);
		double localOptProfile[][] = Meme.EM(5, dataSeqs);
		PrintResult(localOptProfile);
	}
	
	public static void PrintResult(double[][] profile){
		for(double[] profileLine: profile){
			String result = "";
			for(double profileEle : profileLine) result += profileEle + ", ";
			System.out.println(result);
		}
	}
 }
