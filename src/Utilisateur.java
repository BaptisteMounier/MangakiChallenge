import java.util.HashMap;

public class Utilisateur {
	
	private int id;
	private HashMap<Integer, String> votes;
	
	public Utilisateur(int id){
		this.id = id;
		this.votes = new HashMap<Integer, String>();
	}
	
	public void ajoutVote(int idOeuvre, String vote){
		if(votes.containsKey(idOeuvre)){
			System.out.println("ERROR : L'utilisateur "+this.id+" a déjà voté pour l'oeuvre "+idOeuvre+" !");
		}else{
			votes.put(idOeuvre, vote);
		}
	}
	
	public int getId(){
		return this.id;
	}
	
	public HashMap<Integer, String> getVotes(){
		return this.votes;
	}
	
	public boolean equals(int id){
		return this.id == id;
	}
	
}
