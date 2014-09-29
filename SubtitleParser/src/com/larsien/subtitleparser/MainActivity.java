package com.larsien.subtitleparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends Activity {

	final String TAG = "MainActivity"; 
	List<String> wordList = new ArrayList<String>(); 
	TextView tv ; 
	StringBuilder scriptStrings = new StringBuilder();
	Document doc; 
	boolean ScriptIsKorean  = false;
	boolean ScriptIsEnglish = false; 
	Button bt ; 
	String filePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("test", "test");
		onInit();
		loadSMI();
		initJsoup(); 
		checkScriptLanguage();
		getScriptText(); 
	}
	public void getScriptText(){
		StringBuilder texts = new StringBuilder(); 
		if (ScriptIsEnglish) {
			Elements rows = doc.select("P.ENCC");
			for(Element row : rows) {
				System.out.println(row.text());
				texts.append(row.text()+"\n");
			}
		}
		if (ScriptIsKorean) {
			Elements rows = doc.select("P.KRCC");
			for(Element row : rows) {
				System.out.println(row.text());
				texts.append(row.text()+"\n");
				}
		}
		////////test
		Elements rows = doc.select("SYNC");
		for(Element row : rows) {
			System.out.println(row.attr("start"));
			texts.append(row.attr("start")+"\n");
		}
		//////////
		tv.setText(texts);
	}
	 
	public void checkScriptLanguage() {
		
		Elements rows = doc.select("STYLE");
		for(Element row : rows){
			System.out.println(row.text());
		}
		String checkLanguageStrings = rows.toString().toLowerCase();
		
		if (checkLanguageStrings.contains("ko-kr")) {
			ScriptIsKorean = true; 
			Log.d(TAG, "한국어");
		}
		if (checkLanguageStrings.contains("en-en")) {
			ScriptIsEnglish = true;
			Log.v(TAG, "영어");
		}
	}
	public void initJsoup() {
		doc = Jsoup.parse(scriptStrings.toString());
	}
	public void onInit() {
		tv = (TextView)findViewById(R.id.main_text);
		bt = (Button)findViewById(R.id.bt); 
		bt.setOnClickListener(new OnClickLisetner{
			@Override
			public void onClick() {
				 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	             intent.setType("file/*");
	             startActivityForResult(intent,PICKFILE_RESULT_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case PICKFILE_RESULT_CODE:
			if (resultCode == RESULT_OK) {
				filePath = data.getData().getPath();
			}
			break;

		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void loadSMI() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		ArrayList<String> list = new ArrayList<String>();
		
		scriptStrings.append(""); 
		try {
//			is = getAssets().open("test.smi");
			is = new FileInputStream(filePath);
			isr = new InputStreamReader(is, "euc-kr");
			br = new BufferedReader(isr);
			String temp2 ="";
			int i =0; 
			while (true){
				temp2 = br.readLine();
				scriptStrings.append(temp2); 
//				wordList.add(temp);
				i++;
				if(i > 100)
					break; 
				
//				if (temp2 == null)
//					break;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(scriptStrings);
		
		
			
//		list.add(br.read)
		return ; 
	}

}
