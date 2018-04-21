package database;

public class DatabaseHandlerException extends Exception {
	public DatabaseHandlerException(String origin, Exception parent){
		super("Exception at: "+origin+" due to: \n"+ parent.toString(), parent);
	}
	public String toString(){
		return super.toString();
	}
}
