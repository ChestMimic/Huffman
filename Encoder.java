import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;

public class Encoder{

	public StringBuffer contentFull;//destination of unEncoded string
	public Hashtable<Character, Integer> frequencyList; //Using keys to equal character 
/*
	public Encoder(String fileName, String output){
		
		
		//System.out.println("\n");
		
		//Checking something
		try{
			FileInputStream str = new FileInputStream(coded);
			int cchr;
			contentFull = new StringBuffer();
			
			while( (cchr = str.read()) != -1){//read until EOF, should be universal, but unconfirmed
			
				char test = (char) cchr;
				contentFull.append(test);
				//counting while inputting
				
				
				System.out.print(test);//comment this out later
			}
			str.close();//close stream on complete first read
		}
		catch(FileNotFoundException ex){
		
			System.out.println("File not found: " + fileName);
		}
		catch(IOException ex){
			System.out.println("File not read: " + fileName);
			System.out.println("Check permissions");
		}
		
		/*
		String base = out1.substring(0, 8);
		int charCode = Integer.parseInt(base, 2);
		char fin = (char) charCode;
		
		System.out.println(base);
		System.out.println(charCode);
		System.out.println(fin);
		
		
	}
	*/
	
	public Encoder(String input, String output, int flag){
		if(flag == 0){
			Encode(input, output);
		}
		else if(flag == 1){
			Decode(input, output);
		}
		else{
			System.exit(1);
		}
		
	
	}
	
	public void Encode(String target, String dest){
		File file;
		try{
			file = new File(target);
		}
		catch(NullPointerException ex){//In case of user error, presents more useful message than crash log
			System.out.println("ERR: \"" + target + "\" is not a valid file name.");
			file = null;//makes my compiler happy, shouldn't matter what this equals
			System.exit(-1);
		}
		
		contentFull = new StringBuffer();
		frequencyList = new Hashtable<Character, Integer> ();
		
		try{
			FileInputStream str = new FileInputStream(file);
			int cchr;
			
			while( (cchr = str.read()) != -1){//read until EOF, should be universal, but unconfirmed
			
				char test = (char) cchr;
				contentFull.append(test);
				//counting while inputting
				if(frequencyList.containsKey(test)){
					Integer temp =  frequencyList.get(test);
					temp++;
					frequencyList.remove(test);
					frequencyList.put(test, temp);
				}
				else{
					frequencyList.put(test, 1);
				}
				
				//System.out.print(test);//comment this out later
			}
			str.close();//close stream on complete first read
		}
		catch(FileNotFoundException ex){
		
			System.out.println("File not found: " + target);
		}
		catch(IOException ex){
			System.out.println("File not read: " + target);
			System.out.println("Check permissions");
		}
		
		//Turn nodes into a Huffman Tree, and get top node
		HuffNode h = genTree(frequencyList);
		//System.out.println(h.value);
		
		//Grab binary values from tree positioning
		//System.out.println(frequencyList);
		Hashtable enc = huffmanEncode(h, new String());
		//System.out.println(enc);
		//Convert characters in file to numerical values
		String out1 = new String();
		try{
		
			int chr;
			FileInputStream str = new FileInputStream(file);
			while((chr = str.read()) != -1){
				char replace = (char) chr;
				out1 += enc.get(replace);
			}
			str.close();
			//System.out.println(out1);
		}
		catch(IOException ex){
		
		}
		//change 1s and 0s to bytes
		String out2 = new String();
		
		int buff = 0;
		while(buff < out1.length()){
			int buff2 = buff+7;//represent end of 8 bit segment
			int charCode = 0;
			if(buff2 > out1.length()){
				int i = buff2-out1.length();
				String base = out1.substring(buff, buff+i);
				 charCode = Integer.parseInt(base, 2);//recognize number as binary
				for(int j = i; j != 0; j--){
					charCode = charCode << 1;
				}
			}
			else{
				String base = out1.substring(buff, buff2);
				 charCode = Integer.parseInt(base, 2);//recognize number as binary
			}
				char fin = (char) charCode;
				out2 += fin;
				//System.out.print(fin);
				buff = buff2;
			
		}
		
		//Save final string and data to new file
		/* data will be saved in format
			Chunk of encoded characters
			First X characters map the key guide for future decoding, and character count
		*/
		String outKeys = new String();
		//System.out.println("\n" + enc.toString());
		outKeys += enc.toString();//key
		outKeys += h.value;//character count
		outKeys += "ENDHERE";//Tag to decoder to know where keys end
		
		File coded = new File(dest);//Make new file Object, different than new file in system
		
		try{
			//if(!(coded.exists()){
				coded.createNewFile();
			
			
			FileOutputStream fStream = new FileOutputStream(coded);
			outKeys+=out2;
			
			fStream.write(outKeys.getBytes());
			fStream.close();
		}
		catch(IOException ex){
		
		}
	}
	
	
	public void Decode(String target, String dest){
		File file;
		try{
			file = new File(target);
		}
		catch(NullPointerException ex){//In case of user error, presents more useful message than crash log
			System.out.println("ERR: \"" + target + "\" is not a valid file name.");
			file = null;//makes my compiler happy, shouldn't matter what this equals
			System.exit(-1);
		}
		
		contentFull = new StringBuffer();
		frequencyList = new Hashtable<Character, Integer> ();
		
		try{
			FileInputStream str = new FileInputStream(file);
			int cchr;
			
			while( (cchr = str.read()) != -1){//read until EOF, should be universal, but unconfirmed
			
				char test = (char) cchr;
				contentFull.append(test);
				//counting while inputting
				
				
				//System.out.print(test);//comment this out later
			}
			str.close();//close stream on complete first read
		}
		catch(FileNotFoundException ex){
		
			System.out.println("File not found: " + target);
		}
		catch(IOException ex){
			System.out.println("File not read: " + target);
			System.out.println("Check permissions");
		}
		
		int endOfKeys = contentFull.lastIndexOf("ENDHERE");
		//System.out.println(endOfKeys);
		String keyGuide = contentFull.substring(0, endOfKeys);
		//System.out.println(keyGuide);
		String messyText = contentFull.substring(endOfKeys);
		//System.out.println(messyText);
		Hashtable<Character, Integer> codex = determineKeysFromString(keyGuide);
		//System.out.println(codex);
		int charCount;
		int a = keyGuide.lastIndexOf("}");
		
		charCount = Integer.parseInt(keyGuide.substring(a+1));
		
		String output = new String();
		int i = 0;
		String runoff = new String();
		while(charCount > 0){
			char x = messyText.charAt(i);
			int y = (int) x;
			
			String hold = Integer.toBinaryString(x);
			System.out.print(hold);
			String beta = new String();
			int find;
			for(int j = 0; j < 7; j++){
				char z = hold.charAt(j);
				beta += z;
				if(codex.contains((find = Integer.parseInt(beta, 2)))){
					for(Character c : codex.keySet()){
						if(codex.get(c) == find){
							System.out.println(c);
							//break();
						}
					}
				}
			}
			System.out.print(codex.contains(y));
			charCount = 0;
		
		}
		
		
	}
	
	
	public HuffNode genTree(Hashtable<Character, Integer> guide){
	//Create leaf nodes for all valid characters
		ArrayList<HuffNode> arr = new ArrayList<HuffNode>();
		for(Character key : guide.keySet()){
			char c = key.charValue();
			int i = guide.get(key);
		
			HuffNode temp = new HuffNode(i, c);
			arr.add(temp);
		}
		
		while(arr.size() > 1){
			//Get two smallest nodes in list
			HuffNode a = findSmallest(arr);
			arr.remove(a);
			HuffNode b = findSmallest(arr);
			arr.remove(b);
			//System.out.println("Combining: " + a.value + " + " + b.value);
			//combine
			HuffNode c = combineNodes(a, b);
			//System.out.println("New value: " + c.value);
		
			//rinse, repeat
			arr.add(c);
		}
		
		//give back the root node
		
		return arr.get(0);
	
	}
	
	//return a new parent node
	//be sure to grab reference
	public HuffNode combineNodes(HuffNode uno, HuffNode dos){
		
		HuffNode ret = new HuffNode((uno.value +dos.value));
		//order is mostly arbitrary
		//System.out.println("Uno: " + ret.value);
		ret.addLeft(uno);
		ret.addRight(dos);
		return ret;
	}
	
	//unsorted list
	public HuffNode findSmallest(ArrayList<HuffNode> arr){
		HuffNode min = arr.get(0);
		for(int i = 0; i < arr.size(); i++){
			if(min.value > arr.get(i).value){
				//System.out.println(arr.get(i).value);
				min = arr.get(i);
			}
		}
		
		return min;
	}
	
	//Gets the huffman encoding from a node. Assuming the tree is compete
	
	public Hashtable<Character, Integer> huffmanEncode(HuffNode h, String count){
		Hashtable<Character, Integer> ret = new Hashtable<Character, Integer>();
		//left adds a zero, right adds a one
		if(h.thisChar != '\u0000'){//assuming all characters are leaf nodes, which should be correct
			//convert array list into integer
			
			ret.put(h.thisChar, Integer.valueOf(count));
			
		}
		else{
		Hashtable<Character, Integer> a;
		Hashtable<Character, Integer> b;
			if(h.left != null){
				a = huffmanEncode(h.left, count.concat("0"));
				ret.putAll(a);
			}
		
			if(h.right != null){
				 b = huffmanEncode(h.right, count.concat("1"));
				ret.putAll(b);
			}
			
			
			
		}
		return ret;
	}

	
	public Hashtable<Character, Integer> determineKeysFromString(String s){
		Hashtable<Character, Integer> ret = new Hashtable<Character, Integer>();
		//Section format is character, then equal sign, binary number, a comma, then space.
		//Example:
		//{Y=10110000, Z=110}
		int flag = 0;//
		for(int i = 1; i < s.length() && flag != 1; ){//skips character zero, which is known to be '{' in all cases
			char c = s.charAt(i);
			//System.out.print(c);
			i+=2;
			String num = new String();
			//Get number
			int bFlag = 0;
			while(bFlag == 0){
				char x = s.charAt(i);
				if(x == '='){
				
				}
				else if(x ==','){
					bFlag = 1;
				}
				else if(x =='}'){
					bFlag = 1;
					flag = 1;
				}
				else{
					num += x;
				}
				i++;
			}
			i++;
			int newNum = Integer.parseInt(num, 2);
			ret.put(c, newNum);
			//System.out.println(num);
			
		}
		return ret;
			
	}
}