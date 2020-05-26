/* On my honor, I have neither given nor received unauthorized aid on this assignment */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class SIM {
	static List<String> InsList = new ArrayList<String>();
	static LinkedHashMap<String, Integer> InsMap = new LinkedHashMap<String, Integer>();
	static LinkedHashMap<String, Integer> Dictionary = new LinkedHashMap<String, Integer>();
	static int RLE = 0;
	static String AllCompIns = "";
	static int index_Dict = 0;
	
	static LinkedHashMap<Integer, String> DictionaryDecomp = new LinkedHashMap<Integer, String>();
	static HashMap<String, Integer> formatLength = new HashMap<String, Integer>();
	static String InsCom = "";
	static String InsDecom = "";
	static String AllInsDecom = "";
	
	
	
	public static void main(String[] args) { 
		// "1" or "2" , args[0]
		String inputCoOrDe = args[0];
		if (inputCoOrDe.equals("1")) {
			Compress();
		} else if (inputCoOrDe.equals("2")) {
			Decompress();
		} else {
			System.out.print("Please input 1 for compression or 2 for decompression.");
		}
	}

	public static void Compress() {
		String filename = "original.txt";
		String InsLine;
		String PreInstLine ="";
		
		File file = new File(filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while((InsLine = br.readLine()) != null ) {
				InsList.add(InsLine);
				if(InsMap.containsKey(InsLine)) {
					InsMap.put(InsLine, InsMap.get(InsLine) + 1);		
				}
				else {
					InsMap.put(InsLine, 1);
				}
			}
						
		}catch(IOException e ){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//////Dictionary//////
		ArrayList<Map.Entry<String, Integer>> SortArrayList = new ArrayList<Map.Entry<String, Integer>>(InsMap.entrySet());
		Collections.sort(SortArrayList,new Comparator<Map.Entry<String, Integer>>(){
			
				public int compare(Map.Entry<String,Integer> o1, Map.Entry<String ,Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
		
		});
				
		//Advanced for loop ///sublist to pick range
	    for(Map.Entry<String,Integer> entity : SortArrayList.subList(0,8)){
	        Dictionary.put(entity.getKey(), index_Dict);
	        index_Dict++;
	    }
	    
	    
	    
	    /*for(int i=0; i < DictionarySort.size();i++ ) {
	    	Dictionary.put(DictionarySort.entry.getKey(i), i);
	    }*/
	    
	    
	    
		//System.out.print(InsList);
		
	    for(int i=0; i < InsList.size(); i++) {
	    	String InsLineComp = "";
	    	InsLine = InsList.get(i);
	    	if(InsLine.equals(PreInstLine)) {
	    		RLE++;	    		
	    	}
	    	else {
	    		///RLE///
	    		if(RLE > 1) {
	    			//print previous one
	    			CompRLE();
	    			RLE = 0;
	    			AllCompIns = AllCompIns;
	    		}
	    		///Direct Matching///
	    		if(Dictionary.containsKey(InsLine)) {
	    			int dic_index = Dictionary.get(InsLine);
	    			// System.out.println(Dictionary);
	    			//InsLineComp = Integer.toBinaryString(dic_index);
	    			InsLineComp = String.format("%3s", Integer.toBinaryString(dic_index)).replace(' ', '0');
	    			// System.out.println(InsLineComp);
	    			AllCompIns = AllCompIns + "101" + InsLineComp;
	    		}
	    		///OneBitMismatch///
	    		else if(!OneBitMis(InsLine).equals("")) {
	    			InsLineComp= OneBitMis(InsLine); 
	    			AllCompIns = AllCompIns + "010" + InsLineComp;
	    		}
	    		///TwoBitsMismatch///
	    		else if(!TwoBitMis(InsLine).equals("")) {
	    			InsLineComp= TwoBitMis(InsLine); 
	    			AllCompIns = AllCompIns + "011" + InsLineComp;
	    		}
	    		///BitMaskBase///
	    		else if(!BitMask(InsLine).equals("")) {
	    			InsLineComp= BitMask(InsLine); 
	    			AllCompIns = AllCompIns + "001" + InsLineComp;
	    		}
	    		///TwoBitsAnywhere///
	    		else if(!TwoBitsAnywhere(InsLine).equals("")) {
	    			InsLineComp= TwoBitsAnywhere(InsLine); 
	    			AllCompIns = AllCompIns + "100" + InsLineComp;
	    		}
	    		///Original///
	    		else {
	    			AllCompIns = AllCompIns + "110" + InsLine;
	    		}
	    		
	    		RLE = 1;
	    		PreInstLine = InsLine;
	    	}

	    }
		
    	String Comp_1 = String.format("%-" + ((AllCompIns.length()/32)+1)*32 + "s", AllCompIns).replace(' ', '1');
		//fill in "1" in the end
    	String cout = Comp_1.replaceAll("(.{32})", "$1\n").trim();
    	
		try{
			PrintStream fileStream = new PrintStream("cout.txt");
			System.setOut(fileStream);
		} catch(IOException e ){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(cout);
		System.out.println("xxxx");
        Iterator it = Dictionary.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey());// + " " + Integer.toBinaryString(pair.getValue()));
        }
		
	}
	
	public static void CompRLE() {
		String InsRLE = "";
		switch(RLE) {
			case 2: InsRLE = "00";
				break;
			case 3: InsRLE = "01";
				break;
			case 4: InsRLE = "10";
				break;
			case 5: InsRLE = "11";
				break;			
}
		AllCompIns = AllCompIns + "000" + InsRLE;
	}
	
	public static String OneBitMis(String InsLine) {
		for(int index=0; index < InsLine.length()-1; index++){
			StringBuilder InsLineCheck = new StringBuilder(InsLine);
			//stringBuilder can use function like append or insert
		
			// InsLineCheck.setCharAt(h, InsLineCheck.charAt(h)=='0' ? '1':'0');
			if(InsLineCheck.charAt(index)== '0')
				InsLineCheck.setCharAt(index, '1');
			else
				InsLineCheck.setCharAt(index, '0');
			String Ins = InsLineCheck.toString();
			if(Dictionary.containsKey(Ins)) {
				String MisLocation = String.format("%5s", Integer.toBinaryString(index)).replace(' ', '0');	
				//String MisLocation = Integer.toBinaryString(index);
				int dic_index = Dictionary.get(Ins);
				String dic_index_Binary = intTo3bitString(dic_index);
				return MisLocation + dic_index_Binary;
		}
		}
		return "";
	}
	
	
	public static String TwoBitMis(String InsLine) {
		for(int index=0; index < InsLine.length()-1; index++){
			StringBuilder InsLineCheck = new StringBuilder(InsLine);
			if(InsLineCheck.charAt(index) == '0' && InsLineCheck.charAt(index+1) == '0'){
				InsLineCheck.setCharAt(index, '1');
				InsLineCheck.setCharAt(index+1, '1');
			} else if(InsLineCheck.charAt(index) == '0' && InsLineCheck.charAt(index+1) == '1'){
				InsLineCheck.setCharAt(index, '1');
				InsLineCheck.setCharAt(index+1, '0');
			} else if(InsLineCheck.charAt(index) == '1' && InsLineCheck.charAt(index+1) == '0'){
				InsLineCheck.setCharAt(index, '0');
				InsLineCheck.setCharAt(index+1, '1');
			} else {
				InsLineCheck.setCharAt(index, '0');
				InsLineCheck.setCharAt(index+1, '0');
			}
			String Ins = InsLineCheck.toString();
			if(Dictionary.containsKey(Ins)) {
				String MisLocation = String.format("%5s", Integer.toBinaryString(index)).replace(' ', '0');	
				//String MisLocation = Integer.toBinaryString(index);
				int dic_index = Dictionary.get(Ins);
				String dic_index_Binary = intTo3bitString(dic_index);
				return MisLocation + dic_index_Binary;
		}
		}
		return "";
	}
	
	/* for 
	 * 
	 * */
	public static String BitMask(String InsLine) {
		for(int i=8; i<16; i++){
			String maskSmall = String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0');
			//System.out.println("maskSmall: " + maskSmall);
			for(int q=0; q<29; q++){
				StringBuilder instLineCheck = new StringBuilder();
				// String padLeft = padLeftZeros(Integer.toBinaryString(i), q);
				// String mask = rightPadZeros(padLeft, 32);				
				// StringBuilder instLineCheck = new StringBuilder();
				// for(int j=0; j<mask.length(); j++){
				// 	instLineCheck.append(charOf(bitOf(mask.charAt(j)) ^ bitOf(instLine.charAt(j))));
				// }
				String padLeftZeros = String.format("%" + (q+4) + "s", maskSmall).replace(' ', '0');
				String mask = String.format("%-" + 32 + "s", padLeftZeros ).replace(' ', '0');
				//%- left justify, % right justify
				
				
				//System.out.println("padLeftZeros " + padLeftZeros);
				//System.out.println("Mask " + mask + "instLine " + instLine);
				
				// String Ins="";
				for(int j=0; j<32; j++){
					instLineCheck.append(charOf(bitOf(mask.charAt(j)) ^ bitOf(InsLine.charAt(j))));
				}
				String Ins = instLineCheck.toString();
				// System.out.println("//////////" + Ins);
				if(Dictionary.containsKey(Ins)){
					int dic_index = Dictionary.get(Ins);
					String dic_index_S = intTo3bitString(dic_index);
					String mismatchLocation = String.format("%5s", Integer.toBinaryString(q)).replace(' ', '0');

					// System.out.println("//////////" + mismatchLocation + maskSmall + dict_index_S);
					return  mismatchLocation + maskSmall + dic_index_S;
				}
			}
		}
		return "";
	}
	
	
	
	public static String TwoBitsAnywhere(String InsLine){
		for(int i=0; i < InsLine.length(); i++) {
			StringBuilder InsLineCheck = new StringBuilder(InsLine);
			InsLineCheck.setCharAt(i, InsLineCheck.charAt(i)=='0' ? '1':'0');
			for(int j=i+1; j < InsLine.length(); j++) {
				InsLineCheck.setCharAt(j, InsLineCheck.charAt(j)=='0' ? '1':'0');			
				String Ins = InsLineCheck.toString();				
				
				if(Dictionary.containsKey(Ins)) {
					String leftMisLocation = String.format("%5s", Integer.toBinaryString(i)).replace(' ', '0');
					String rightMisLocation = String.format("%5s", Integer.toBinaryString(j)).replace(' ', '0');
					int dic_index = Dictionary.get(Ins);
					String dic_index_Binary = intTo3bitString(dic_index);				
					
					return leftMisLocation + rightMisLocation + dic_index_Binary;
			}
				InsLineCheck.setCharAt(j, InsLineCheck.charAt(j)=='0' ? '1':'0');	
			}
			InsLineCheck.setCharAt(i, InsLineCheck.charAt(i)=='0' ? '1':'0');
			
		}
		
		
		return "";
	}
	/*
	public static String FlipBit(String s) {
		if(s =="0") return "1";
		else return "0";
	}*/
	
	public static String intTo3bitString(int dic_index){
		// String dict_index_S = Integer.toBinaryString(dict_index);
		// dict_index_S = dict_index_S.substring(dict_index_S.length()-strLength);
		String dic_index_S = String.format("%3s", Integer.toBinaryString(dic_index)).replace(' ', '0');
		return dic_index_S;
	}
	
	private static boolean bitOf(char in) {
	    return (in == '1');
	}
	private static char charOf(boolean in) {
	    return (in) ? '1' : '0';
	}

	
	public static void Decompress() {
		String filename = "compressed.txt";
		String DictAll = "";
		int index = 0;		
		int InsLength = 0;

		
		/////  Read Compressed.txt File  /////
		// File file = new File(filename);
		try{
			AllCompIns = new String(Files.readAllBytes(Paths.get(filename)));
		} catch(IOException e ){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		DictAll = AllCompIns.substring(AllCompIns.lastIndexOf("x") + 1).trim();
		////r?n
		String Lines[] = DictAll.split("\\r?\\n");
		// System.out.println(DictAll);
		
		for(int i=0; i<Lines.length; i++){
			DictionaryDecomp.put(i, Lines[i]);
		}
		
		formatLength.put("000", 2);
		formatLength.put("001", 12);
		formatLength.put("010", 8);
		formatLength.put("011", 8);
		formatLength.put("100", 13);
		formatLength.put("101", 3);
		formatLength.put("110", 32);
		
		//System.out.println(AllCompIns);
		
		
		AllCompIns = AllCompIns.replaceAll("\\r\\n|\\\\r|\\\\n", "");
		//System.out.println(AllCompIns);
		
		while(AllCompIns.charAt(index) != 'x') {
			//// First 3 bits format and its bits length ////
			StringBuilder Format = new StringBuilder();	
			for(int j=0; j<3; j++){
				Format.append(AllCompIns.charAt(index++));
			}
			String FormatS = Format.toString();
			if(formatLength.containsKey(FormatS)){
				InsLength = formatLength.get(FormatS);
			}else {///////??????
				break;
			} 
			
			//// bits after format ////
			StringBuilder InsD = new StringBuilder();
			for(int j=0; j<InsLength; j++){
				InsD.append(AllCompIns.charAt(index++));
			}
			InsCom = InsD.toString();
			// System.out.println("InsCom :" + InsCom);
			
			if(FormatS.equals("000")) RLE_Decom(InsCom);
			//InsDecom = RLE_Decom(InsCom);
			else if(FormatS.equals("001")) BitMask_Decom(InsCom);
			else if(FormatS.equals("010")) OneBitMis_Decom(InsCom);
			else if(FormatS.equals("011")) TwoBitMis_Decom(InsCom);
		    else if(FormatS.equals("100")) TwoBitsAnywhere_Decom(InsCom);
			else if(FormatS.equals("101")) DirectMatch_Decom(InsCom);
			else if(FormatS.equals("110")){
				//// Original ////				
				InsDecom = InsCom;
				AllInsDecom = AllInsDecom + InsCom;
			}
				
						
			}
		
		String dout = AllInsDecom.replaceAll("(.{32})", "$1\n").trim();
		// System.out.println("AllInsDecom :" + AllInsDecom);
		// System.out.println("dout :" + dout);
		
		try{
			PrintStream fileStream = new PrintStream("dout.txt");
			System.setOut(fileStream);
		} catch(IOException e ){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(dout);
			
			
			
		}
		
				
	
	public static void RLE_Decom(String InsCom) {
		if(InsCom.equals("00"))AllInsDecom = AllInsDecom + InsDecom;
		else if(InsCom.equals("01"))AllInsDecom = AllInsDecom + InsDecom + InsDecom;
		else if(InsCom.equals("10"))AllInsDecom = AllInsDecom + InsDecom + InsDecom + InsDecom;
		else if(InsCom.equals("11"))AllInsDecom = AllInsDecom + InsDecom + InsDecom + InsDecom + InsDecom;
		
		
		
		
	}
	
	public static void BitMask_Decom(String InsCom) {
		int MisLocat = Integer.parseInt(InsCom.substring(0,5), 2);
		String bitmask = InsCom.substring(5,9);
		int dic_index = Integer.parseInt(InsCom.substring(9, InsCom.length()), 2);
		String Inst = "";
		int index_mask = 0;
		
		if(DictionaryDecomp.containsKey(dic_index)) {
			Inst = DictionaryDecomp.get(dic_index);
		}
		StringBuilder InstSB =new StringBuilder(Inst);
		for(int i=MisLocat; i < MisLocat+4;i++ ) {
			if(InstSB.charAt(i) == bitmask.charAt(index_mask)) {						
				InstSB.setCharAt(i, '0');}
			else {InstSB.setCharAt(i, '1');}
			
			index_mask++;
		}
		InsDecom = InstSB.toString();		
		AllInsDecom = AllInsDecom + InsDecom;
	}
	
	
	public static void OneBitMis_Decom(String InsCom) {
		int MisLocat = Integer.parseInt(InsCom.substring(0,5), 2);
		int dic_index = Integer.parseInt(InsCom.substring(5, InsCom.length()), 2);
		String Inst = "";
	
		if(DictionaryDecomp.containsKey(dic_index)) {
			Inst = DictionaryDecomp.get(dic_index);
		}
		StringBuilder InstSB =new StringBuilder(Inst);
		
		InstSB.setCharAt(MisLocat, InstSB.charAt(MisLocat)=='0' ? '1':'0');
		InsDecom = InstSB.toString();		
		AllInsDecom = AllInsDecom + InsDecom;
		
	}
	
	
	public static void TwoBitMis_Decom(String InsCom) {
		int MisLocat = Integer.parseInt(InsCom.substring(0,5), 2);
		int dic_index = Integer.parseInt(InsCom.substring(5, InsCom.length()), 2);
		String Inst = "";
	
		if(DictionaryDecomp.containsKey(dic_index)) {
			Inst = DictionaryDecomp.get(dic_index);
		}
		StringBuilder InstSB =new StringBuilder(Inst);
		
		InstSB.setCharAt(MisLocat, InstSB.charAt(MisLocat)=='0' ? '1':'0');
		InstSB.setCharAt(MisLocat+1, InstSB.charAt(MisLocat+1)=='0' ? '1':'0');
		InsDecom = InstSB.toString();		
		AllInsDecom = AllInsDecom + InsDecom;
		
//		System.out.println("InsDecom :" + InsDecom);
//		System.out.println("AllInsDecom :" + AllInsDecom);
	}
	
	public static void TwoBitsAnywhere_Decom(String InsCom) {
		int MisLocat_left = Integer.parseInt(InsCom.substring(0,5), 2);
		int MisLocat_right = Integer.parseInt(InsCom.substring(5,10), 2);
		int dic_index = Integer.parseInt(InsCom.substring(10, InsCom.length()), 2);
		String Inst = "";
	
		if(DictionaryDecomp.containsKey(dic_index)) {
			Inst = DictionaryDecomp.get(dic_index);
		}
		StringBuilder InstSB =new StringBuilder(Inst);
		
		InstSB.setCharAt(MisLocat_left, InstSB.charAt(MisLocat_left)=='0' ? '1':'0');
		InstSB.setCharAt(MisLocat_right, InstSB.charAt(MisLocat_right)=='0' ? '1':'0');
		InsDecom = InstSB.toString();		
		AllInsDecom = AllInsDecom + InsDecom;
		
	}
	
	
	public static void DirectMatch_Decom(String InsCom) {
		int dic_index = Integer.parseInt(InsCom.substring(0, InsCom.length()), 2);
		String Inst = "";
	
		if(DictionaryDecomp.containsKey(dic_index)) {
			Inst = DictionaryDecomp.get(dic_index);
		}
		StringBuilder InstSB =new StringBuilder(Inst);
	
		InsDecom = InstSB.toString();		
		AllInsDecom = AllInsDecom + InsDecom;
		
			
	}
	
	
}
