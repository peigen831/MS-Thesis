package crawler;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.nodes.Document;

import helper.FileIO;

public class LocalDriver {
	public static void main(String args[]){
		FileIO io = new FileIO ();
		
		HashSet<String> set = new HashSet<String>();
		
		ArrayList<String> list =new ArrayList<String>();

		int nDoc = 0;
		for(int i = 81000; i < 81050; i++){
			
			try{
				Document doc = io.readFile(FileIO.dirRaw + i);
				
				HTMLInterpreter interpreter = new HTMLInterpreter();
				
				String[] tags = interpreter.getTags(doc);
				
				for(String s : tags)
				{
					s = s.toLowerCase();
					set.add(s);
					list.add(s);
				}

				nDoc++;
				}catch(Exception e){
			}
		}
		System.out.println("Number of doc: " + nDoc);
		System.out.println("Unique tags: " + set.size());
		System.out.println("Total tags: " + list.size());
		System.out.println("Average tag per article: " + (list.size() / nDoc*1.0));
	}
}
