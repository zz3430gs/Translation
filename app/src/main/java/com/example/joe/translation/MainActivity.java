package com.example.joe.translation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mbutton;
    EditText mEditText1;
    EditText mEditText2;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbutton = (Button)findViewById(R.id.search_button);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText1 == mEditText2){
                    //TODO: Get the translation from a dictionary if there is a word that matches that word
                    String translation = "";
                    mTextView.setText(translation);
                }
                else{
                    //Prints out a message if there is no translation to the word
                    Toast.makeText(MainActivity.this, "There is no translation", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
