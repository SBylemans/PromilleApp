/*
 * Author: Sander Bylemans
 * Last edited: 27 december 2013
 * Creates an object with amount of hours,amount of drinks and which drinks, the weight of the
 * client using the app and a constant that is different for man and woman. This also contains a 
 * method that calculates the amount of alcohol in your blood.
 */

package com.example.myfirstapp;

import java.util.ArrayList;
import android.widget.*;

public class Promille {
	double weight;
	ArrayList<EditText> glasses;
	ArrayList<Spinner> biersoorten;
	double hours;
	double r;
	/**
	 * Constructor of promille object.
	 * @param weight Weight of person using the app
	 * @param glasses ArrayList with EditTexts to get the different amounts of beers drunk
	 * @param biersoorten ArrayList with EditTexts to get the different beers drunk
	 * @param hours The hours of drinking defined by the client.
	 * @param male Define whether client is male or female. When male true else false.
	 */
	public Promille(double weight, ArrayList<EditText> glasses, ArrayList<Spinner> biersoorten, double hours, boolean male){
		this.weight = weight;
		this.glasses = glasses;
		this.biersoorten = biersoorten;
		this.hours = hours;
		if (male){
			this.r = 0.7;
		} else
			this.r = 0.5;
		
			
	}
	
	/**
	 * Actually calculate the amount of alcohol in the bloodstream.
	 * @return promille or 0
	 */
	public double calculate(){
		double realAmount = 0;
		for(int i = 0; i < this.glasses.size(); i++){
			EditText a = glasses.get(i);
			double amount = Double.parseDouble(a.getText().toString());
			Spinner b = biersoorten.get(i);
			String bier = b.getSelectedItem().toString();
			if(bier.contains("Bier(25cl)")){
				amount = amount*1;
			} else if(bier.contains("Bier(33cl)")){
				amount = amount*1.3;
			} else if(bier.contains("Bier(50cl)")){
				amount = amount*2;
			} else if(bier.contains("Speciaal Bier (33cl|6,5%)")){
				amount = amount*1.7;
			} else if(bier.contains("Speciaal Bier (33cl|8%)")){
				amount = amount*2;
			} else if(bier.contains("Speciaal Bier (33cl|10%)")){
				amount = amount*2.5;
			} else if(bier.contains("Wijn (glas)")){
				amount = amount*1;
			} else if(bier.contains("Wijn (fles)")){
				amount = amount*7;
			} else if(bier.contains("Aperitief (5cl|15%)")){
				amount = amount*1;
			} else if(bier.contains("Sterke drank (3,5cl|35%)")){
				amount = amount*1;
			} else if(bier.contains("Sterke drank (3,5cl|40%)")){
				amount = amount*1.19;
			} else if(bier.contains("Mixdrank (27,5cl|5,6%)")){
				amount = amount*1.25;
			}
			realAmount += amount;
		}
		double promille = ((realAmount*10)/(this.weight*this.r))-((this.hours-0.5)*(this.weight*0.002));
		if(promille > 0)
			return promille;
		else promille = 0;
		return promille;
	}
	
	/**
	 * Returns whether your amount of alcohol is within legla limits.
	 * @param p The amount of promille
	 * @return -1 when p < 0.2, 1 when p > 0.7 else 0
	 */
	public int inRedZone(double p){
		if (p>0.7) return 1;
		else if (p < 0.2) return -1;
		else return 0;
	}

}
