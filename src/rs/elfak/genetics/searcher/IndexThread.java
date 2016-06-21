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

public class IndexThread extends Thread{
	
    public static String CONTENT = "content";
    public static String TITLE = "title";
	
	private String docDir;
	private String indexDir;
	private final Analyzer analyzer;
	int startIndex;
	int stopIndex;
	int threadNumber;
	
	public IndexThread(String docDir, String indexDir, int startIndex, int stopIndex, int threadNumber){
		this.indexDir = indexDir;
		this.docDir = docDir;
		analyzer = new StandardAnalyzer();
		this.startIndex = startIndex;
		this.stopIndex = stopIndex;
		this.threadNumber = threadNumber;
	}
	
	public String getDocDir(){
		return this.docDir;
	}
	
	private String[] readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String document = new String(encoded, encoding);
        String document2 = document.replaceAll("\n{2}", "\n");
        String lines[] = document2.split("\n");
        return lines;
	}
  
	public void parseDocument(String docDir, int statIndex, int stopIndex, int threadNum){

	}
	
    private void addDocument(String docDir, int statIndex, int stopIndex, int threadNum){
        try{
        	boolean work = true;
			/*File dirPart = new File(indexDir + "/" + threadNum + "/");
			if (!dirPart.exists()) {
				if(dirPart.mkdir())
					work = true;
			}
			else
				work = true;*/
			
			if(work){
				Directory dir = FSDirectory.open(new File(indexDir).toPath());
				IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
				config.setSimilarity(new ClassicSimilarity());
				IndexWriter writer = new IndexWriter(dir, config);
				
				File folder = new File(docDir);
				int docCounter = 0;
				int paragraphCounter = 0;
				for (int iFile = statIndex; iFile < stopIndex; iFile++) {
					File fileEntry = folder.listFiles()[iFile];
					if (!fileEntry.isDirectory()) {
						if (fileEntry.getName().endsWith(".txt")) {
							try {
								String[] paragraphed = readFile(fileEntry.getPath(), StandardCharsets.UTF_8);
								paragraphCounter = 0;
								for (int i = 0; i < paragraphed.length; i++) {
									String paragraphName = fileEntry.getName() + "#" + i;
									addDoc(paragraphName, paragraphed[i], writer);
									paragraphCounter++;
								}
								IndexMaker.parCount.add(paragraphCounter);
								docCounter++;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				IndexMaker.numberOfDoc.add(docCounter);

				writer.close();
				dir.close();
			}
        }
        catch(Exception e){
           System.err.println(e.toString());
        }
    }
	
    private void addDoc(String paragraphName, String paragraphContent, IndexWriter writer) throws IOException{
    	Document doc = new Document();
		FieldType textType = new FieldType(TextField.TYPE_STORED);
		textType.setStored(true);
		textType.setStoreTermVectors(true);
		textType.setStoreTermVectorOffsets(true);
		doc.add(new Field(TITLE, paragraphName, textType));
		doc.add(new Field(CONTENT, paragraphContent, textType));
		writer.addDocument(doc);
    }
    
	@Override
	public void run() {
		addDocument(docDir, startIndex, stopIndex, threadNumber);
	}

}
