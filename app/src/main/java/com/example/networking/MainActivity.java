package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    ArrayList<Mountain> data;
    ArrayAdapter<Mountain> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,R.layout.mytextview,data);


       ListView lv = findViewById(R.id.listview);
       lv.setAdapter(adapter);

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
                public void onItemClick(AdapterView<?> parent, View view, int a, long id) {
                Toast.makeText(getApplicationContext(),data.get(a).getName()+ " är " + data.get(a).getSize() + " meter högt och befinner sig i " + data.get(a).getLocation(),
                Toast.LENGTH_LONG).show(); //toast
        }
     });
       new JsonTask().execute ("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");


    }


    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
               JSONArray jsonArray = new JSONArray(json);
               for (int a = 0; a < jsonArray.length(); a++){
                   JSONObject jsonObject = jsonArray.getJSONObject(a);
                   String name = jsonObject.getString("name");
                   String location = jsonObject.getString("location");
                   int size = jsonObject.getInt("size");
                   Mountain mountain = new Mountain(name,location,size);
                   data.add(mountain);
               }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("TAG", json);
            adapter.notifyDataSetChanged();
        }
    }
}
