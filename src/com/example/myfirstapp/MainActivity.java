/*
 * Author: Sander Bylemans
 * Last edited: 27 december 2013
 * This class uses activity_main.xml, promille_view.xml and Promille.java to create an android app that calculates your
 * amount of alcohol in your blood.
 */
package com.example.myfirstapp;

import java.util.ArrayList;

import com.example.myfirstapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
/**
 * Respresents the object of the app.
 * @author Sander Bylemans
 *
 */
public class MainActivity extends Activity {
	
	int ID = 0; 
	ArrayList<EditText> hoeveelheden = new ArrayList<EditText>();
	ArrayList<Spinner> biersoorten = new ArrayList<Spinner>();
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refresh();
	}
	/**
	 * Sets text of TextView in promille_view.xml and sets ContentView to promille_view.xml.
	 * @param string The string that needs to be set in the new TextView
	 * @param inRedZone -1, 0 or 1 indicating what value promille has
	 */
	public void setNewTextViewWithText(String string, int inRedZone){
		setContentView(R.layout.promille_view);
		final Button recal = (Button) findViewById(R.id.btnRecal);
		final TextView tv = (TextView) findViewById(R.id.promille);
		tv.setText(string);
		if(inRedZone > 0){
			tv.setTextColor(Color.RED);
		}
		if(inRedZone == 0){
			tv.setTextColor(Color.YELLOW);
		}
		if(inRedZone < 0){
			tv.setTextColor(Color.GREEN);
		}
		recal.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				biersoorten.removeAll(biersoorten);
				hoeveelheden.removeAll(hoeveelheden);
				refresh();
				
			}
		});
	}
	/**
	 * Sets the ContentView to the activity_main.xml and defines the different elements in activity_main.xml.
	 * Defines also what to do when different buttons in activity_main.xml are pressed.
	 */
	public void refresh(){
		setContentView(R.layout.activity_main);
		
		biersoorten.removeAll(biersoorten);
		hoeveelheden.removeAll(hoeveelheden);
		
		final Button buttonCal = (Button) findViewById(R.id.buttonCalculate);
		final EditText weight = (EditText) findViewById(R.id.wText);
		final EditText glasses = (EditText) findViewById(R.id.gText);
		final EditText hours = (EditText) findViewById(R.id.hText);
		final RadioButton male = (RadioButton) findViewById(R.id.radioButtonMale);
		final RadioButton female = (RadioButton) findViewById(R.id.radioButtonFemale);
		final Button add = (Button) findViewById(R.id.btnAdd);
		final Button remove = (Button) findViewById(R.id.btnRemove);
		
		final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		
		weight.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		glasses.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		hours.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		buttonCal.setOnClickListener(
				new View.OnClickListener() 
				{		//What to do when button clicked
			
					@Override
					public void onClick(View v) {
						double w = 0;
						double h = 0;
						try{
							w = Double.parseDouble(weight.getText().toString());
							h = Double.parseDouble(hours.getText().toString());
							hoeveelheden.add((EditText)findViewById(R.id.gText));
							biersoorten.add((Spinner)findViewById(R.id.beers));
							boolean m = true;
							int inRedZone;
							if(male.isChecked()) m =true;
							else if(female.isChecked()) m=false;
							Promille promille = new Promille(w,hoeveelheden,biersoorten, h, m);
							double p = promille.calculate();
							p = Math.round(p*100.0)/100.0;
							inRedZone = promille.inRedZone(p);
							setNewTextViewWithText("You're promille is: " + p, inRedZone);
						} catch (NumberFormatException e) {

							dlgAlert.setMessage("You forget something to fill in :s");
							dlgAlert.setTitle("My First App");
							dlgAlert.setPositiveButton("OK", null);
							dlgAlert.setCancelable(true);
							dlgAlert.create().show();
							dlgAlert.setPositiveButton("Ok",
								    new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) {
								           refresh();
								        }
								    });
						}
						
					}
				});
		add.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addEditText();
			}
		});
		
		remove.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remove();
			}
		});
		
		
	}
	/**
	 * Adds EditText to activity_main.xml to define how many beers of what you have drunk when add button
	 * is pressed.
	 */
	public void addEditText(){
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout);
		LinearLayout newHorLay = new LinearLayout(this);
		newHorLay.setOrientation(LinearLayout.HORIZONTAL);
		newHorLay.setId(ID);
		ID++;
		
		EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		et.setHint("Amount");
		
		Spinner newBeer = new Spinner(this);
		
		Spinner beers = (Spinner) findViewById(R.id.beers);
		newBeer.setAdapter(beers.getAdapter());
		
		LayoutParams paramsEt = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.addView(newHorLay);
		newHorLay.addView(et,paramsEt);
		newHorLay.addView(newBeer);
		biersoorten.add(newBeer);
		hoeveelheden.add(et);
	}
	/**
	 * Removes EditText from activity_main.xml when Remove button pressed.
	 */
	public void remove(){
		if (ID == 0){
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

			dlgAlert.setMessage("You must have drunk something?! ö");
			dlgAlert.setTitle("My First App");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			dlgAlert.setPositiveButton("Ok",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				           refresh();
				        }
				    });
		}
		else{
			LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout);
			ID--;
			layout.removeView(findViewById(ID));
			hoeveelheden.remove(hoeveelheden.size()-1);
			biersoorten.remove(biersoorten.size()-1);
		}
		
	}
	
	/**
	 * Finish project when back button is pressed.
	 */
	@Override
	public boolean onKeyDown(int keycode,KeyEvent event){
		if (keycode == KeyEvent.KEYCODE_BACK) finish();
		return super.onKeyDown(keycode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
