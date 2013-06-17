

public class MotifDiscovery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[][] dataSeqs = DataIO.ReadDataSp(Res.drFiles);
		Meme meme = new Meme(5, dataSeqs);
		meme.Run();
		meme.PrintProfile();
	}
	
 }
