package info.androidhive.StockEProject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class PortfolioSellFragment extends Fragment implements OnClickListener{
	
	public PortfolioSellFragment(){}
	public String YAHOO= "http://download.finance.yahoo.com/d/quotes.csv?s=";	

	
	public static double cash = 2000;
	public static double price;

	public static String[] symbol = new String[9];
	public static String[] quantity = new String[9];
	public static TextView[] label = new TextView[9];
	public static TextView textBox;
	public static TextView[] stock = new TextView[9];
	public static TextView message;
	public static EditText txtQuantity;
	public static RadioButton[] radio = new RadioButton[9];
	public static boolean radio0ON = false;
	public static boolean radio1ON = false;
    private Button bnSell = null;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        
        textBox = (TextView) rootView.findViewById(R.id.edit_cash);
        textBox.setText(String.format("%.2f", cash));  
        
        txtQuantity = (EditText) rootView.findViewById(R.id.txt_quantity);

        message = (TextView) rootView.findViewById(R.id.lbl_message);
        
        radio[0] = (RadioButton) rootView.findViewById(R.id.radioStock1);
        radio[0].setEnabled(radio1ON);
        
        stock[0] = (TextView) rootView.findViewById(R.id.stock_1);
        stock[0].setText(quantity[0]);
        stock[1] = (TextView) rootView.findViewById(R.id.stock_2);
        stock[1].setText(quantity[1]);

		label[0] = (TextView) rootView.findViewById(R.id.stock1Name);
        label[0].setText(symbol[0]);
		label[1] = (TextView) rootView.findViewById(R.id.stock2Name);
        label[1].setText(symbol[1]);

		
		bnSell = (Button) rootView.findViewById(R.id.bn_sell);

		bnSell.setOnClickListener(new OnClickListener(){
		    @Override
		    
			public void onClick(View arg0){
			    AsyncTaskRunner runner = new AsyncTaskRunner(); 
			    runner.execute();
			    }});
		
        return rootView;
        
        
    }
	
	public String nameNo()
	{
		for (int x = 0; x<radio.length;x++)
		{
			if (radio[x].isChecked())
			{
				return label[x].getText().toString();
			}
		}
		return "";
	}
	public int stockNo()
	{
		int x;
		for (x = 0; x<radio.length;x++)
		{
			if (radio[x].isChecked())
			{
				return x;
			}
		}
		return x;
	}
	 private class AsyncTaskRunner extends AsyncTask<String, Void, String> {


		  @Override
		  protected String doInBackground(String... s) {
			  try{
				HttpURLConnection connection = null;
				String stock = nameNo();
				String site= YAHOO+stock+"&f=l1&e=.csv";
				
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
			  catch (Exception e)
			  {
				  
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
			  try{
				price = Double.parseDouble(result);
				int quantity = Integer.parseInt(txtQuantity.getText().toString());
				int x = stockNo();
				int availableQuantity = Integer.parseInt(stock[x].getText().toString());
				if (quantity<=availableQuantity && quantity > 0)
				{
					quantity = Integer.parseInt(txtQuantity.getText().toString());
					setCash(cash+(price*quantity));
			        textBox.setText(String.format("%.2f", cash)); 
			        
					message.setText("Stock sold.");
					  
					if (availableQuantity-quantity == 0)
					{
						stock[x].setText("");
						label[x].setText("");
						setRadioOn1False ();
					}
					else if (availableQuantity-quantity != 0)
					{
						int remaining = availableQuantity-quantity;
						stock[x].setText(String.valueOf(remaining));
					}
				}
				else
				{
					  message.setText("Cannot sell more than you own.");

				}
			  }
			  catch (Exception e)
			  {
				  message.setText("Choose a stock and specify how many.");
			  }
			  
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
	public static double getCash() {
	    return cash;
	}
	public static void setCash(double var) {
		cash = var;
		}
	
	public static String getSymbol0()
	{
		return label[0].getText().toString();
	}
	public static void setSymbol0(String var) {
		symbol[0] = var;
		}
	public static void setQuantity0(String var) {
		quantity[0] = var;
		}
	
	public static String getSymbol1()
	{
		return label[0].getText().toString();
	}
	public static void setSymbol1(String var) {
		symbol[1] = var;
		}
	public static void setQuantity1(String var) {
		quantity[1] = var;
		}
	
	
	public static void setRadioOn0True ()
	{
		radio0ON = true;
	}
	public static void setRadioOn0False ()
	{
		radio0ON = false;
	}
	public static void setRadioOn1True ()
	{
		radio1ON = true;
	}
	public static void setRadioOn1False ()
	{
		radio1ON = false;
	}

	 @Override
	 public void onClick(View V)
	 {

	 }
}
