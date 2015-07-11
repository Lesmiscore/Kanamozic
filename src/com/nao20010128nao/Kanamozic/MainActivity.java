package com.nao20010128nao.Kanamozic;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.TextView.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
	EditText normal,encoded,caesar;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		normal=(EditText)findViewById(R.id.normalText);
		encoded=(EditText)findViewById(R.id.encodedText);
		caesar=(EditText)findViewById(R.id.caesar);
		findViewById(R.id.encodeButton).setOnClickListener(new View.OnClickListener(){
				public void onClick(View w){
					encoded.setText(KanamozicCore.encode(normal.getText().toString(),getCaesar()),BufferType.EDITABLE);
				}
			});
		findViewById(R.id.decodeButton).setOnClickListener(new View.OnClickListener(){
				public void onClick(View w){
					normal.setText(KanamozicCore.decode(encoded.getText().toString(),getCaesar()),BufferType.EDITABLE);
				}
			});
    }
	public byte getCaesar(){
		return Byte.parseByte(caesar.getText().toString());
	}
}
