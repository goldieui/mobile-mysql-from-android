package com.vlad.mysql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MySQLActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private List<String> countries = new ArrayList<String>();
    
//    private ListView listView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        listView = (ListView)findViewById(R.id.listView1);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                countries);
        setListAdapter(adapter);
        new RetrieveJsonTask().execute(new String[] {});
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String country = (String) getListView().getItemAtPosition(position);
        new RetrieveCountryCodeTask().execute(new String[] {country});
    }
    
    
    private class RetrieveCountryCodeTask extends AsyncTask<String, Void, String> {
        
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            // //the year data to send
            ArrayList<NameValuePair> nameValuePairs = new
            ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("country", params[0]));
            InputStream is = null;
            // http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://10.0.2.2/script.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            // parse json data
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    result = json_data.getString("ccode");
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return result;
        }
    }

private class RetrieveJsonTask extends AsyncTask<String, Void, List<String>> {


    protected void onPostExecute(List<String> result) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected List<String> doInBackground(String... arg0) {
        String result = "";
        // //the year data to send
        // ArrayList<NameValuePair> nameValuePairs = new
        // ArrayList<NameValuePair>();
        // nameValuePairs.add(new BasicNameValuePair("year","1980"));
        InputStream is = null;
        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2/script.php");
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Log.i("log_tag", "ccode: " + json_data.getString("ccode") + ", name: "
                        + json_data.getString("country"));
                countries.add(json_data.getString("country"));
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return countries;
    }

}

}


