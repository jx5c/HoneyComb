import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class HoneyComb {
	

	//a map storing every character in all its appearances in the HoneyComb 
	private Map<Character, ArrayList<Cell>> honeyCombData = new HashMap<Character, ArrayList<Cell>>();
	//a map storing elements in every level 
	private Map<Integer, ArrayList<Cell>> levelToChars = new HashMap<Integer, ArrayList<Cell>>();
	
	private int getLevel(int idx){
		return (idx-1)/6+1;
	}
	
	private boolean bfs(String word){
		
		return false;
	}
	
	public ArrayList<String> searchingWords(String wordsFile){
		ArrayList<String> results = new ArrayList<String>();
		InputStream ins;
		try {
			ins = new FileInputStream(new File(wordsFile));
			Scanner scanner = new Scanner(ins);
			while(scanner.hasNext()){
				String word = scanner.nextLine();
				if(word.length() > 0 && bfs(word)){
					results.add(word);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(results);
		return results;
	}
	
	private void getNeighbors(Cell cell){
		int level = getLevel(curIdx);
		if(curIdx == 0){
			int[] neighbors = new int[6];
			neighbors[0]=1;
			neighbors[1]=2;
			neighbors[2]=3;
			neighbors[3]=4;
			neighbors[4]=5;
			neighbors[5]=6;
			data.put(curIdx, neighbors);
		}else if((curIdx-1-(level-2)*6)%(level-1) == 0){
			//elements at six vertex: they have three neighbors in outside level; one neighbor in inside level
			int[] neighbors = new int[6];
			int offset = (curIdx-1-(level-2)*6)%(level-1);
			//three in outside level
		    int outsideMid = level*6+1+offset*(level);
			//one in inside level
		    int inside = (level-2)*6+1+offset*(level-2);
		}
	}
	
	public HoneyComb(String honeyCombFile){
		try {
			InputStream ins = new FileInputStream(new File(honeyCombFile));
			Scanner scanner = new Scanner(ins);
			int levelTotal = scanner.nextInt();
			int totalCharCount = levelTotal*(levelTotal+1)*3+1;
			honeyCombInArray = new char[totalCharCount];
			int idx = 0;
			for(int i=0;i<levelTotal;i++){
				String content = scanner.nextLine();
				setTheNeighbors(i,data);
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
		HoneyComb hc = new HoneyComb(args[0]);
		ArrayList<String> results = hc.searchingWords(args[1]);
		for(String str : results){
			System.out.println(str);
		}
	}

}
class Cell{
	int level; 
	int idx;
	public Cell(int l, int i){
		level = l;
		idx = i;
	}
}