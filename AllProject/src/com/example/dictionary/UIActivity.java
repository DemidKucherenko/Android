package com.example.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class UIActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
    }

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			String word = ((EditText)findViewById(R.id.editText1)).getText().toString();
			word = word.trim().toLowerCase();
			if (!word.matches("^[a-z -]+$")) {
				Toast.makeText(this, "введите английское слово", Toast.LENGTH_SHORT).show();
				break;
			}
			Intent i = new Intent(this, SearchResultActivity.class);
			i.putExtra("word", word);
			startActivity(i);
		}
	}

}
