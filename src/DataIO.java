import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataIO {
	static int MAX_LENGTH_OF_SEQ;
	public static char[][] ReadDataSp(String[] files ){
		List<String> dataSet = new ArrayList<String>();
		MAX_LENGTH_OF_SEQ = 0;
		for(String fileName : files){
			dataSet.addAll(ReadDataSeq(Res.filePath + fileName+Res.extensionOfFile));	
		}
		return ConvertDataSeqs(dataSet); 
	}
	private static List<String> ReadDataSeq(String fileName){
		List<String> dataSet = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String s;
			while ((s = in.readLine()) != null) {
				if(s.startsWith(">")){
				}else{
					dataSet.add(s);
					if(s.length() > MAX_LENGTH_OF_SEQ) MAX_LENGTH_OF_SEQ = s.length();
				}
			}
			in.close();
		} catch (IOException e) {
		    System.err.println(e); // 에러가 있다면 메시지 출력
		    System.exit(1);
		}		
		//Collections.sort(dataSet);
		return dataSet;		
	}
	private static char[][] ConvertDataSeqs(List<String> listString){
		char[][] dataSeqs = new char[listString.size()][MAX_LENGTH_OF_SEQ];
		for(int i = 0; i < listString.size(); i++){
			/*char[] s = listString.get(i).toCharArray();
			for(int j = 0; j < s.length; j++){
				dataSeqs[i][j] = s[j];
			}*/
			dataSeqs[i] = listString.get(i).toCharArray();
		}
		return dataSeqs;
	}
}
