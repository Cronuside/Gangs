package xyz.cronu.gangs.utils;

public class Number {

	public static long add(long original, long input){
		return original + input;
	}

	public static long take(long original, long input){
		if(original - input <= 0) input = original;
		return original - input;
	}

}
