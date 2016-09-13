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


/**
 * author Jian Xiang (jx5c@virginia.edu)
 * title: coding test: Word search in honeycomb structure
 * The program runs with two arguments: 
 * 		first argument is the file specifying the data in a honeycomb structure; 
 * 		second argument is the file specifying the words to be searched
 * Output: words that can be found in the honeycomb, printed in sorted order;
 * Sample run: 
 * 		java HoneyComb honeycomb.txt dictionary.txt
 * 
 * Implementation details:
 *      The honeycomb is viewed as several layers.
 *      A class "CellLocation" is used to record the layer and index of each cell node in the honeycomb  
 * 		Two main data structures are used. One hashmap and one list:
 * 		The list is used to store the honeycomb data, every element in the list stores one level of data; this list corresponds to the file which specifies the honeycomb data
 * 		The hashmap is used to store mapping between a character in the honeycomb and all its appearances in the honeycomb (character : list of Cell); this map makes the word searching easier;
 */
public class HoneyComb {
	
	//a map storing elements in every level 
	private List<char[]> levelToChars = null;
	//a map storing every character in all its appearances in the HoneyComb; for searching purposes
	private Map<Character, ArrayList<CellLocation>> honeyCombData = new HashMap<Character, ArrayList<CellLocation>>();
	
	/**
	 * return the character in the honeycomb, given the cell location
	 * @param cell the location of the cell, including the layer and index
	 * @return the character in the given location
	 */
	private char getChar(CellLocation cell){
		//deal with the layer goes beyond the limit
		if(cell.level >= levelToChars.size()){
			return '#';
		}
		//deal with the negative index; happens when getting neighbors of cell with index=0;
		if(cell.idx < 0){
			cell.idx += levelToChars.get(cell.level).length;  
		}else{
		//deal with the out of range index; happens when getting neighbors of cell with index=layer_length-1;
			cell.idx = cell.idx%levelToChars.get(cell.level).length;
		}
		return levelToChars.get(cell.level)[cell.idx];	
	}
	
	/**
	 * dfs the word in the honeycomb with backtracking
	 * @param word the word to be searched
	 * @param cell the current cell position
	 * @param visited the cell positions which have been visited
	 * @param pos the index of the word to be compared
	 * @return if the word can be found in the honeycomb or not;
	 */
	private boolean dfs(String word, CellLocation cell, ArrayList<CellLocation> visited, int pos){
		if(pos == word.length()){
			return true;
		}
		//get the neighbors
		CellLocation[] neighbors = getNeighbors(cell);
		for(CellLocation neighbor : neighbors){
			char nChar = getChar(neighbor);
			if(!visited.contains(neighbor) && nChar == word.charAt(pos)){
				visited.add(neighbor);
				//if a neighbor equals to the word[pos]; go deeper
				if(dfs(word, neighbor, visited, pos+1)){
					return true;
				}
				visited.remove(visited.size()-1);
			}
		}	
		return false;
	}
	
	/**
	 * function to search for words; 
	 * starting with all cell whose values equal to the first character of the word  
	 * @param wordsFile the file which contains the words to be searched
	 * @return a sorted list of words found in the honeycomb 
	 */
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
					ArrayList<CellLocation> startingCells = honeyCombData.get(startChar);
					for(CellLocation startingCell : startingCells){
						ArrayList<CellLocation> visited = new ArrayList<CellLocation>();
						visited.add(startingCell);
						//if the honeycomb has the first character of the word. start dfs searching
						if(dfs(word, startingCell, visited, 1)){
							results.add(word);
							//found one, no need to search any more
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
	
	/**
	 * get the positions of the cells around the given cell
	 * @param cell the position of a cell
	 * @return an array of cells' positions
	 */
	private CellLocation[] getNeighbors(CellLocation cell){		
		if(cell.level == 0 && levelToChars.size()>1){
			//for the center middle cell, if the honeycomb has more than one layers
			return new CellLocation[]{	new CellLocation(1, 0),
								new CellLocation(1, 1),
								new CellLocation(1, 2),
								new CellLocation(1, 3),
								new CellLocation(1, 4),
								new CellLocation(1, 5)};
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
			return new CellLocation[]{
					new CellLocation(cell.level, cell.idx+1),
					new CellLocation(cell.level, cell.idx-1),
					new CellLocation(cell.level-1, cornerIdxInside),
					new CellLocation(cell.level+1, cornerIdxOutside-1),
					new CellLocation(cell.level+1, cornerIdxOutside),
					new CellLocation(cell.level+1, cornerIdxOutside+1)};
		}
		// if the cell is not at the corner , then the neighbors are
		// two neighbors are at the current level
		// two neighbors are at the outside level
		// two neighbors are at the inside level 
		return new CellLocation[]{
				new CellLocation(cell.level, cell.idx+1),
				new CellLocation(cell.level, cell.idx-1),
				new CellLocation(cell.level+1, cornerIdxOutside + offsetFromCorner),
				new CellLocation(cell.level+1, cornerIdxOutside + offsetFromCorner + 1),
				new CellLocation(cell.level-1, cornerIdxInside + offsetFromCorner),
				new CellLocation(cell.level-1, cornerIdxInside + offsetFromCorner -1)};
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
						honeyCombData.put(chars[k], new ArrayList<CellLocation>());
					}
					honeyCombData.get(chars[k]).add(new CellLocation(i,k));
				}
				levelToChars.add(chars);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		if(args.length < 2){
			System.err.println("MISSING ARGUMENTS!");
			System.err.println("Needs TWO arguments: ");
			System.err.println("       First Argument: file of honeycomb data");
			System.err.println("       Second Argument: file of the words to be searched");
			System.err.println("       Example: java HoneyComb honeycomb.txt dictionary.txt");
			return;
		}
		HoneyComb hc = new HoneyComb(args[0]);
		ArrayList<String> results = hc.searchingWords(args[1]);
		for(String str : results){
			System.out.println(str);
		}
	}
}

class CellLocation{
	int level; 
	int idx;
	public CellLocation(int l, int i){
		level = l;
		idx = i;
	}
}