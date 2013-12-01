public class HuffmanTree{

	public HashTable frequencyList; //Using keys to equal character 

	public HuffmanTree(String fileName, String outputFile){
		//NOTE: File systems have ONLY been tested on Windows computers
		//may not work on different systems
		//create list with frequency of each character
		
		FileReader file = new FileReader(fileName);
		
	}
	
	//return a new parent node
	//be sure to grab reference
	public HuffNode combineNodes(HuffNode uno, HuffNode dos){
		Huffnode ret = new HuffNode(uno.value +dos.value , null);
		return ret;
	}

}