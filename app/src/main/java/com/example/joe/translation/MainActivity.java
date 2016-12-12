package com.example.joe.translation;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Translate Activity";
    Button mbutton;
    EditText mEditText;
    TextView mTextView;
    TextView mTargetLang;
    ProgressBar mProgress;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the key and get the key data
        key = keys.getKeyFromRawResource(this, R.raw.keys);

        //Create references to the individual widgets from the layout resources
        mProgress = (ProgressBar)findViewById(R.id.progress);
        mEditText = (EditText)findViewById(R.id.text_to_translate);
        mTargetLang = (TextView) findViewById(R.id.translation_lang);
        mTextView = (TextView)findViewById(R.id.textTranslation);

        //If there is nothing in the key file, return error message
        if (key != null){
            getJapaneseTranslation();
        }else {
            Log.e(TAG, "Key can't be read from raw resource");
        }

        mbutton = (Button)findViewById(R.id.search_button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJapaneseTranslation();
            }
        });

    }
    public void getJapaneseTranslation(){

        //For now we get the translation from japanese base on the url
        //TODO: For later maybe get the user to be able to chooce what they want to translate to
        String baseTempURL = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyBKGHm9xQLBfJJOtg5_fHzFf2eu2oFjZjw&source=en&target=ja&q=";
        String edtitext = mEditText.getText().toString();
        String url = String.format("%1s%2s", baseTempURL, edtitext);

        requestTargetLang tempTask = new requestTargetLang();
        tempTask.execute(url);

        mProgress.setVisibility(ProgressBar.VISIBLE);
    }

    public static class keys{
        //returns a key, or null if file is not found or can't be read
        protected static String getKeyFromRawResource(Context context, int rawResource){

            InputStream keyStream = context.getResources().openRawResource(rawResource);
            BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));
            try {
                String key = keyStreamReader.readLine();
                return key;
            }catch (IOException ioe){
                return null;
            }
        }
    }
    private class requestTargetLang extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream));

                StringBuilder builder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null){
                    Log.d(TAG, "line = " + line);
                    builder.append(line);
                }
                String responseString = builder.toString();

                Log.d(TAG, responseString);

                JSONObject json = new JSONObject(responseString);

                return json;

            }catch (Exception e){

                Log.e(TAG, "Error fetching language data", e);
                return null;
            }
        }
        protected void onPostExecute(JSONObject json) {
            //fetching the data from the api
            if (json != null) {

                mProgress.setVisibility(ProgressBar.INVISIBLE);

                //If there is an error fetching data then get a error message
                try {
                    if (json.getJSONObject("data").has("error")) {
                        Log.e(TAG, "Error in response from Google Translate" + json.getJSONObject("data")
                                .getJSONObject("error")
                                .getString("description"));
                    }

                    //If there is a translation then it will change the textview and show the translated word
                    String translateword = json.getJSONObject("data").getString("translations");
                    String textWord = mEditText.getText().toString();
                    mTextView.setText(textWord + " translated to Japanese is" + translateword);

                } catch (JSONException je) {
                    Log.e(TAG, "JSON parsing error", je);
                }

            }
        }

    }

}
