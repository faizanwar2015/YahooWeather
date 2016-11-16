package com.example.resalat.yahooweather;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.R.id.edit;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> contactList;

    private static final String  tempkey= "com.example.resalat.key";
    String [] weather = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ListView listView = (ListView)findViewById(R.id.tempList);

       }

    void getWeather(View view)
    {
        EditText editText = (EditText)findViewById(R.id.cityText);
        EditText editText1 = (EditText)findViewById(R.id.stateText);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        new MyAsyncTaskgetNews().execute(url);
    }

    // get news from server
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
       public String [] myString = new String[50];


        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }
        protected void onProgressUpdate(String... progress) {








               /* weather[0]="chill:"+chill;
                weather[1]="direction:"+direction;
                weather[2]="speed:"+speed;
                weather[3]="humidity:"+humidity;
                weather[4]="visibility:"+visibility;
                weather[5]="temp:"+temp;*/


                //Toast.makeText(getApplicationContext(),chill+direction+speed+humidity+visibility+temp,Toast.LENGTH_LONG).show();
               try
               {
                   JSONObject json = new JSONObject(progress[0]);
                   JSONObject query = json.getJSONObject("query");
                   JSONObject results = query.getJSONObject("results");
                   JSONObject channel = results.getJSONObject("channel");

                   JSONArray forecast = channel.getJSONArray("forecast");


                   HashMap<String, String> contact = new HashMap<>();
                // List<String> allNames = new ArrayList<String>();
                for (int i = 0; i < forecast.length(); i++) {
                    JSONObject Forecast = forecast.getJSONObject(i);
                    String date = Forecast.getString("date");
                    String day = Forecast.getString("day");
                    String high = Forecast.getString("high");
                    String low = Forecast.getString("low");
                    String text = Forecast.getString("text");


                    contact.put("date", date);
                    contact.put("day", day);
                    contact.put("high", high);
                    contact.put("low", low);
                    contact.put("text", text);
                    contactList.add(contact);


                  //  myString[i] = String.valueOf(new String[]{date + " " + day + " " + high + " " + low + " " + text});


                }



               }catch(Exception ex){
                }
            Intent intent = new Intent(MainActivity.this,Display.class);

            intent.putExtra("tempkey",contactList);

            startActivity(intent);



            }

        }

        protected void onPostExecute(String  result2){



        }






    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }

}
