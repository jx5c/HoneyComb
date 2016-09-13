import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;


public class HoneyComb {
	
	//a map storing every character in all its appearances in the HoneyComb 
	private Map<Character, ArrayList<Cell>> honeyCombData = new HashMap<Character, ArrayList<Cell>>();
	//a map storing elements in every level 
	private List<char[]> levelToChars = null;
//	private int totalCount = 0;
	
	private char getChar(Cell cell){
		if(cell.level >= levelToChars.size()){
			return '#';
		}
		//deal with the negative index;
		if(cell.idx < 0){
			cell.idx += levelToChars.get(cell.level).length;  
		}
		return levelToChars.get(cell.level)[cell.idx%levelToChars.get(cell.level).length];	
	}
	
	private boolean dfs(String word, Cell cell, ArrayList<Cell> visited, int pos){
		if(pos == word.length()){
			return true;
		}
		Cell[] neighbors = getNeighbors(cell);
		for(Cell neighbor : neighbors){
			char nChar = getChar(neighbor);
			if(!visited.contains(neighbor) && nChar == word.charAt(pos)){
				visited.add(neighbor);
				if(dfs(word, neighbor, visited, pos+1)){
					return true;
				}
				visited.remove(visited.size()-1);
			}
		}	
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
				if(word.length() == 0){
					continue;
				}
				char startChar = word.charAt(0);
				if(honeyCombData.containsKey(startChar)){
					ArrayList<Cell> startingCells = honeyCombData.get(startChar);
					for(Cell startingCell : startingCells){
						ArrayList<Cell> visited = new ArrayList<Cell>();
						visited.add(startingCell);
						if(dfs(word, startingCell, visited, 1)){
							results.add(word);
							break;
						}	
					}	
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.sort(results);
		return results;
	}
	
	private Cell[] getNeighbors(Cell cell){
		
		if(cell.level == 0 && levelToChars.size()>1){
			//for the first cell, if has more than one layers
			return new Cell[]{	new Cell(1, 0),
								new Cell(1, 1),
								new Cell(1, 2),
								new Cell(1, 3),
								new Cell(1, 4),
								new Cell(1, 5)};
		}
		int levelLength = levelToChars.get(cell.level).length;
		int edgeLength = levelLength / 6;
		int offsetFromCorner = cell.idx % edgeLength;
		int cornerPos = cell.idx / edgeLength;
		int cornerIdxOutside = (edgeLength+1)*cornerPos;
		int cornerIdxInside = (edgeLength-1)*cornerPos;
		
		if(offsetFromCorner == 0){
			//if the cell is at the corner , then the neighbors are
			// two neighbors are at the current level
			// three neighbors are at the outside level
			// one neighbor is at the inside level
			return new Cell[]{
					new Cell(cell.level, cell.idx+1),
					new Cell(cell.level, cell.idx-1),
					new Cell(cell.level-1, cornerIdxInside),
					new Cell(cell.level+1, cornerIdxOutside-1),
					new Cell(cell.level+1, cornerIdxOutside),
					new Cell(cell.level+1, cornerIdxOutside+1)};
		}
		// if the cell is not at the corner , then the neighbors are
		// two neighbors are at the current level
		// two neighbors are at the outside level
		// two neighbors are at the inside level 
		return new Cell[]{
				new Cell(cell.level, cell.idx+1),
				new Cell(cell.level, cell.idx-1),
				new Cell(cell.level+1, cornerIdxOutside + offsetFromCorner),
				new Cell(cell.level+1, cornerIdxOutside + offsetFromCorner + 1),
				new Cell(cell.level-1, cornerIdxInside + offsetFromCorner),
				new Cell(cell.level-1, cornerIdxInside + offsetFromCorner -1)};
	}
	
	public HoneyComb(String honeyCombFile){
		try {
			InputStream ins = new FileInputStream(new File(honeyCombFile));
			Scanner scanner = new Scanner(ins);
			int totalLevelCount = Integer.parseInt(scanner.nextLine());
			levelToChars = new ArrayList<char[]>();
			for(int i=0;i<totalLevelCount;i++){
				String content = scanner.nextLine();
				char[] chars = content.toCharArray();
				if(chars.length!= 1 && chars.length % 6 != 0){
					System.out.println("WRONG INPUT FOR LEVEL: "+i);
					return;
				}
				for(int k=0;k<chars.length;k++){
					if(!honeyCombData.containsKey(chars[k])){
						honeyCombData.put(chars[k], new ArrayList<Cell>());
					}
					honeyCombData.get(chars[k]).add(new Cell(i,k));
				}
				levelToChars.add(chars);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
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