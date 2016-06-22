package rs.elfak.genetics;

import java.util.List;
import java.net.URL;
import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.util.URI;

import rs.elfak.genetics.searcher.IndexMaker;
import rs.elfak.genetics.searcher.IndexThread;
import rs.elfak.genetics.searcher.Search;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*Search searchEngine = new Search("/Volumes//Disc 2 /Projekti/PI_Data/Data/Paragraphed/", false);
		
		List<String> tmp = searchEngine.searchByCategory("Z22 (computer) - Wikipedia, the free encyclopedia", Search.TITLE, true);
		
		if(tmp != null){
			for(int i = 0; i < tmp.size(); i++){
				System.out.println(tmp.get(i));
			}
		}*/
		
		Search searchEngine = new Search("/Volumes/Disc 2 /Projekti/PI_Data/Data/Paragraphed/", false);
		int sum = searchEngine.indexCorpus("/Volumes/Disc 2 /Projekti/PI_Data/Data/Crawler_Data/", "/Volumes/Disc 2 /Projekti/PI_Data/Index/");
		System.out.println("Par: " + sum);

		
	}

}
