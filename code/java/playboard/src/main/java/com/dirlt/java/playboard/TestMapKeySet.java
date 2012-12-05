package com.dirlt.java.playboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestMapKeySet {
	public static void main(String[] args) {
		Map<String,String> dict = new HashMap<String,String>();		
		
		System.out.println("----session1-----");
		dict.put("hello", "world");
		Set<String> ks = dict.keySet();
		for(String k:ks) {
			System.out.println(k);
		}
		
		System.out.println("----session2-----");
		dict.put("hello2", "world2");
		ks = dict.keySet();
		for(String k:ks) {
			System.out.println(k);
		}
	}	
	
}
