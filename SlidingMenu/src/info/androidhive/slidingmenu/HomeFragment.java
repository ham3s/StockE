package info.androidhive.slidingmenu;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class HomeFragment extends Fragment implements OnClickListener{
	

	public HomeFragment(){}
	public String YAHOO= "http://download.finance.yahoo.com/d/quotes.csv?s=";	
	private EditText textBox = null;

	// string array for stock information
	public String[] stockInfo = new String[7];
	TextView[] label = new TextView[7];

    private Button bnRetrieve = null;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);
        
        bnRetrieve = (Button) rootView.findViewById(R.id.bn_retrieve);
 
       
        textBox = (EditText) rootView.findViewById(R.id.edit_symbol);

		bnRetrieve = (Button) rootView.findViewById(R.id.bn_retrieve);
		label[0] = (TextView) rootView.findViewById(R.id.tv_symbol);
		label[1] = (TextView) rootView.findViewById(R.id.tv_company);
		label[2] = (TextView) rootView.findViewById(R.id.tv_exchange);
		label[3] = (TextView) rootView.findViewById(R.id.tv_volume);
		label[4] = (TextView) rootView.findViewById(R.id.tv_last);
		label[5] = (TextView) rootView.findViewById(R.id.tv_change);
		label[6] = (TextView) rootView.findViewById(R.id.tv_perc_change);

		bnRetrieve.setOnClickListener(new OnClickListener(){
	    @Override
		public void onClick(View arg0){
		    AsyncTaskRunner runner = new AsyncTaskRunner(); 
		    runner.execute();
			

		}});
		
		textBox.setOnTouchListener(new OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            v.onTouchEvent(event);
	            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	            if (imm != null) {
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	            }                
	            return true;
	        }
	    });
        return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.main, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}


	public void setInfo(TextView[] text, String[] s)
	{
		for (int x = 0; x<stockInfo.length;x++)
		{
		label[x].setText(stockInfo[x]);
		}
	}

   
	 private class AsyncTaskRunner extends AsyncTask<String, Void, String> {


		  @Override
		  protected String doInBackground(String... s) {
				HttpURLConnection connection = null;
			    String  symbol = textBox.getText().toString();
				String site= YAHOO+symbol+"&f=snxvl1c1p2&e=.csv";
				try {
					connection = (HttpURLConnection) new URL(site).openConnection();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
						InputStreamReader reader = new InputStreamReader(connection.getInputStream());
						StringBuilder sb = new StringBuilder();
						char[] buf = new char[1024];
						int read;
						while ((read = reader.read(buf)) != -1) {
							sb.append(buf, 0, read);
						}
						reader.close();
						
						return sb.toString();
						
						};			
				} catch (MalformedURLException e){
					textBox.setText("Error retrieving price: " +e.getMessage());
				} catch (IOException e) {
					textBox.setText("Error retrieving price: " +e.getMessage());
				}

				return "";
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		   */
		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
				stockInfo = result.split(",");
				setInfo(label, stockInfo);
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @see android.os.AsyncTask#onPreExecute()
		   */
		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
		   // example showing ProgessDialog
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		   */

		 }
	 @Override
	 public void onClick(View V)
	 {

	 }    }
 
 
     
     

