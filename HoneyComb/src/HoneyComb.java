import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class HoneyComb {
	
	private Map<Integer, char[]> data = new HashMap<Integer, char[]>();
	private char[] honeyCombInArray = null;
	private String[] wordsToSearch;

	private int getLevel(int idx){
		return (idx-1)/6+1;
	}
	
	public HoneyComb(String honeyCombFile, String searchWordsFile){
		try {
			InputStream ins = new FileInputStream(new File(honeyCombFile));
			Scanner scanner = new Scanner(ins);
			int levelTotal = scanner.nextInt();
			int totalCharCount = levelTotal*(levelTotal+1)*3+1;
			honeyCombInArray = new char[totalCharCount];
			int idx = 0;
			for(int i=0;i<levelTotal;i++){
				String content = scanner.nextLine();
				data.put(i, content.toCharArray());
				for(char c : content.toCharArray()){
					honeyCombInArray[idx++] = c;
				}
				
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HoneyComb hc = new HoneyComb(args[0],args[1]);
		
	}

}
