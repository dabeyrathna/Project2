package com.isr.www;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Normalize{
	
	public Map<String, ArrayList<String>> newsGroupMap = new HashMap<String, ArrayList<String>>();
	public Map<String,Integer> maxCountMap = new HashMap<String, Integer>();
	public static String[] stopwords = {"a","articl", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};

	public Normalize(String fileName) {
		readDataset(fileName);
		constructMaxFrequecyMap();
	}
	
	public void readDataset(String fileName){
		Scanner readDataFile = null;

		try{
			readDataFile = new Scanner(new File(fileName));

			while(readDataFile.hasNext()){	

				String[] line = readDataFile.nextLine().split("\\s",2);

				if(newsGroupMap.containsKey(line[0]))
					newsGroupMap.get(line[0]).add(line[1]);
				else{
					ArrayList<String> s = new ArrayList<String>();
					s.add(line[1]);
					newsGroupMap.put(line[0], s);
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Check whether all the files are existing in current directory..");
			System.out.println("End...!!");
		}
		finally{
			readDataFile.close();
		}
	}
	
	public double getLogTwo(double num){
		return Math.log(num) / Math.log(2);
	}

	public void checkWordCountVariations(){
		int count = 0;
		int total = 0;
		int c = 0;
		for (Map.Entry<String,ArrayList<String>> entry : newsGroupMap.entrySet()) {
			for (String l : entry.getValue()) {
				count = l.split("\\s").length;	
				System.out.println("Count per line = "+count);
				total += count;
				count = 0;					
			}
			System.out.println("\nNews Group "+ ++c +" Total word count ==>  "+ total+"\n\n");
			total = 0;
		}
	}
	
	public void logTf(String keyWord){
		double logTf= 0d;
		Map<String,Double> frequencyMap = new HashMap<String, Double>();
		int rowTfCount = 0;
		for (Map.Entry<String,ArrayList<String>> entry : newsGroupMap.entrySet()) {
			for (String l : entry.getValue()) {
				for (String wordSet : l.split("\\s")) {
					if(keyWord.equals(wordSet)){
						rowTfCount++;
						break;
					}
				}
				
			}
			if(rowTfCount!=0){
			logTf = 1+getLogTwo(rowTfCount);
			}else{
				logTf=0d;	
			}
			//System.out.print("In the NewsGroup "+ entry.getKey()+" the logTf for " +keyWord +" is :: ");
			//System.out.println(logTf);
			frequencyMap.put(entry.getKey(),logTf);
			rowTfCount=0;
			logTf=0d;
			Object[] rwfArray = frequencyMap.entrySet().toArray();
			int rank=1;
			Arrays.sort(rwfArray, new Comparator() {
			    public int compare(Object element1, Object element2) {
			        return ((Map.Entry<String, Double>) element2).getValue()
			                   .compareTo(((Map.Entry<String, Double>) element1).getValue());
			    }
			});
			
			System.out.println("The order of ranking for each news groups with log of raw frequency count is as follows::");
			
			for (Object element : rwfArray) {
			    System.out.println("The log frequency count for "+ keyWord +" in the newsgroup "+((Map.Entry<String, Double>) element).getKey() + " is :: "
			            + ((Map.Entry<String, Double>) element).getValue() + "and the rank is "+ rank++); }
			
		//	ranking(frequencyMap,keyWord);
			
		}
		
		
		// display 20 newsgroup list according to rank + values
	}
	
	public void doubleLogTf(String keyWord){
		// display 20 newsgroup list according to rank + values
	}
	
	public void ranking(Map frequencyMap,String keyWord){
		Object[] rwfArray = frequencyMap.entrySet().toArray();
		int rank=1;
		Arrays.sort(rwfArray, new Comparator() {
		    public int compare(Object element1, Object element2) {
		        return ((Map.Entry<String, Integer>) element2).getValue()
		                   .compareTo(((Map.Entry<String, Integer>) element1).getValue());
		    }
		});
		
		System.out.println("The order of ranking for each news groups with raw frequency count is as follows::");
		
		for (Object element : rwfArray) {
		    System.out.println("The raw frequency count for "+ keyWord +" in the newsgroup "+((Map.Entry<String, Integer>) element).getKey() + " is :: "
		            + ((Map.Entry<String, Integer>) element).getValue() + "and the rank is "+ rank++); }
		
	}
	
	public void rowTf(String keyWord){
		Map<String,Integer> frequencyMap = new HashMap<String, Integer>();
		int rowTfCount = 0;
		for (Map.Entry<String,ArrayList<String>> entry : newsGroupMap.entrySet()) {
			for (String l : entry.getValue()) {
				for (String wordSet : l.split("\\s")) {
					if(keyWord.equals(wordSet)){
						rowTfCount++;
						break;
					}
				}
				
			}
			
		//	System.out.print("In the NewsGroup "+ entry.getKey()+" the rwf for " +keyWord +" is :: ");
		//	System.out.println(rowTfCount);
			frequencyMap.put(entry.getKey(), rowTfCount);
			rowTfCount=0;
		}
		ranking(frequencyMap,keyWord);
	}
		// display 20 newsgroup list according to rank + values

	
	public void constructMaxFrequecyMap(){
		Map<String,Integer> maxMap = new HashMap<String, Integer>();

		for (Map.Entry<String,ArrayList<String>> entry : newsGroupMap.entrySet()) {
			for (String l : entry.getValue()) {
				nextWord: for (String wordSet : l.split("\\s")) {
					String temp = wordSet.toLowerCase();
					for (String w : stopwords) {
						if(w.equals(temp)) 
							break nextWord;
					}
					if(maxMap.containsKey(temp))
						maxMap.put(temp, (maxMap.get(temp) + 1));
					else
						maxMap.put(temp, 1); 
				}
			}
			System.out.print("News group is :  "+ entry.getKey()+" ==== ");
			maxCountMap.put(entry.getKey(), getMax(maxMap));
			maxMap.clear();
		}
		System.out.println(maxCountMap.size());
	}
	
	public int getMax(Map<String, Integer> maxMap) {
		int max = 0;
		int temp = 0;
		String m ="";
		for (Map.Entry<String, Integer> entry : maxMap.entrySet())
		{
			temp = entry.getValue();                          ////////////// we can use this method do find most frequent words in several newsgroup. for now just max 
		    if (temp > max){
		        max = temp;
		        m = entry.getKey();
		    }
		}
		System.out.println(m + " ====   "+max);
		return max;
	}
}

public class NewsGroup {

	public static void main(String[] args) {
		Normalize normalize = new Normalize("newsGroups.txt");
		normalize.rowTf("polit");
		normalize.logTf("polit");
		
	}

}
