package db.tools;

public class Sprint extends Thread {
	private String[] stuff;  //array to avoid passing parameters by value
	
	public Sprint(String []stuff) {this.stuff=stuff;}
	
	public String[] getCooked(){return stuff;}
	
	@Override public void run() {}
}
