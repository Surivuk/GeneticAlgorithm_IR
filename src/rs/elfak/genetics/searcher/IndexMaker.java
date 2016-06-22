package rs.elfak.genetics.searcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexMaker{
	
	public static ArrayList<Integer> numberOfDoc = new ArrayList<>();
	public static ArrayList<Integer> parCount = new ArrayList<>();
	public static int paragraphCounter = 0;
	
	private static List<Integer> prepearDir(String docDir){
		List<Integer> ret = new ArrayList<>();
		File folder = new File(docDir);	
		if(folder != null && folder.isDirectory()){
			int len = folder.list().length;
			int part = Math.round(len / 4);
			int tmpPointer = 0;
			while(tmpPointer <= len){
				ret.add(tmpPointer);
				tmpPointer += part;
			}
			ret.add(len - 1);
		}
		return ret;
	}
	
	public static void start(String docDir, String indexDir){
		List<Integer> indexList = prepearDir(docDir);
			
		for(int i = 0; i < indexList.size(); i++){
			System.out.println(indexList.get(i) + ", " + indexList.size());
			if(i != indexList.size() - 1)
				new IndexThread(docDir, indexDir, indexList.get(i), indexList.get(i + 1), i).run();
			else
				new IndexThread(docDir, indexDir, indexList.get(i - 1), indexList.get(i), i).run();
		}
	}
}
