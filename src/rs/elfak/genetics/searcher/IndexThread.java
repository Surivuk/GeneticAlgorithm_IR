package rs.elfak.genetics.searcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	
	private List<String> readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String document = new String(encoded, encoding);
        String document2 = document.replaceAll("\n{2}", "\n");
        String lines[] = document2.split("\\r?\\n");
        List<String> ret = new ArrayList<>();
        for(int i = 0; i < lines.length; i++){
        	if(!lines[i].isEmpty() && !lines[i].equals("\r"))
        		if(lines[i].contains(" ") && lines[i].length() >= 5)
        			ret.add(lines[i]);
        }
        return ret;
	}
  
	public int oneT(String docDir){
        try{
        	Directory dir = FSDirectory.open(new File(indexDir).toPath());
        	IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
        	config.setSimilarity(new ClassicSimilarity());
        	IndexWriter writer = new IndexWriter(dir, config);
        	
        	File folder = new File(docDir);
			int docCounter = 0;
			int parCount = 0;
			for (int iFile = 0; iFile < folder.listFiles().length; iFile++) {
				File fileEntry = folder.listFiles()[iFile];
				if (!fileEntry.isDirectory()) {
					if (fileEntry.getName().endsWith(".txt")) {
						try {
							List<String> paragraphed = readFile(fileEntry.getPath(), StandardCharsets.UTF_8);
							for (int i = 0; i < paragraphed.size(); i++) {
								String paragraphName = fileEntry.getName() + "#" + i;
								addDoc(paragraphName, paragraphed.get(i), writer);
								parCount++;
							}
							docCounter++;
						} 
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				IndexMaker.numberOfDoc.add(docCounter);

				writer.close();
				dir.close();
				return parCount;
			}
        }
        catch(Exception e){
           System.err.println(e.toString());
        }
        return -1;
	}
	
    private void addDocument(String docDir, int statIndex, int stopIndex, int threadNum){
        try{
        	boolean work = true;
			if(work){
				Directory dir = FSDirectory.open(new File(indexDir).toPath());
				IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
				config.setSimilarity(new ClassicSimilarity());
				IndexWriter writer = new IndexWriter(dir, config);
				
				File folder = new File(docDir);
				int docCounter = 0;
				for (int iFile = statIndex; iFile < stopIndex; iFile++) {
					File fileEntry = folder.listFiles()[iFile];
					if (!fileEntry.isDirectory()) {
						if (fileEntry.getName().endsWith(".txt")) {
							try {
								List<String> paragraphed = readFile(fileEntry.getPath(), StandardCharsets.UTF_8);
								for (int i = 0; i < paragraphed.size(); i++) {
									String paragraphName = fileEntry.getName() + "#" + i;
									addDoc(paragraphName, paragraphed.get(i), writer);
									IndexMaker.parCount.add(1);
								}
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
	
	public List<String> parseDocument(String docDir, String folderForParagraphs, boolean clean) {
		File folder = new File(docDir);
		File paraFolder = new File(folderForParagraphs);

		if (clean) {
			System.out.println("Cleaning started.");
			for (final File file : paraFolder.listFiles()) {
				file.delete();
				System.out.println("Document : " + file.getName() + " deleted.");
			}
		}
		System.out.println("Cleaning done.");

		ArrayList<String> list = new ArrayList<>();

		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				if (fileEntry.getName().endsWith(".txt")) {
					try {
						System.out.println("=================================================");
						List<String> paragraphed = readFile(fileEntry.getPath(), StandardCharsets.UTF_8);
						BufferedWriter bw = null;
						for (int i = 0; i < paragraphed.size(); i++) {

							String fileWhereToWrite = fileEntry.getName()
									.replaceAll(" - Wikipedia, the free encyclopedia", "#" + String.valueOf(i));

							File file = new File(folderForParagraphs + fileWhereToWrite);

							// if file doesn't exists, then create it
							if (!file.exists()) {
								file.createNewFile();
							}

							FileWriter fw = new FileWriter(file.getAbsoluteFile());
							bw = new BufferedWriter(fw);
							System.out.println("Paragraph : " + paragraphed.get(i));
							bw.write(paragraphed.get(i));
							bw.close();

						}
						System.out.println("Document : " + fileEntry.getName() + " split end written into : "
								+ folderForParagraphs);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		return list;
	}

}
