public class HuffNode{

	public char thisChar;//null is valid, make sure to check for null
	public int value; //frequency of char OR sum of all children nodes
	public HuffNode left;
	public HuffNode right;

	public HuffNode(int val, char chr){
		this.value = val;
		this.thisChar = chr;
		this.left = null;
		this.right = null;
	}
	
	public HuffNode(int val){
		this.value = val;
		this.thisChar = '\u0000';//replace null
		this.left = null;
		this.right = null;
	}
	
	public void addLeft(HuffNode h){
		this.left = h;
	}
	
	public void addRight(HuffNode h){
		this.right = h;
	}
	
	

}