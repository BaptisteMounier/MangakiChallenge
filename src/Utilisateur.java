import java.util.HashMap;

public class Utilisateur {
	
	private int id;
	private HashMap<Integer, Integer> votes;
	private HashMap<Integer, Boolean> souhaite;
	
	public Utilisateur(int id){
		this.id = id;
		this.votes = new HashMap<Integer, Integer>();
		this.souhaite = new HashMap<Integer, Boolean>();
	}
	
	public void ajoutVote(int idOeuvre, String vote){
		if(votes.containsKey(idOeuvre)){
			System.out.println("ERROR : L'utilisateur "+this.id+" a déjà voté pour l'oeuvre "+idOeuvre+" !");
		}else{
			int v_vote = 0;
			switch(vote){
				case "dislike":
					v_vote = -1;
					break;
				case "neutral":
					v_vote = 0;
					break;
				case "like":
					v_vote = 1;
					break;
				case "love":
					v_vote = 2;
					break;
			}
			votes.put(idOeuvre, v_vote);
		}
	}
	
	public void ajoutSouhait(int idOeuvre, String souhait){
		if(votes.containsKey(idOeuvre)){
			System.out.println("ERROR : L'utilisateur "+this.id+" a déjà exprimé un souhait pour l'oeuvre "+idOeuvre+" !");
		}else{
			boolean v_souhait = false;
			switch(souhait){
				case "0":
					v_souhait = false;
					break;
				case "1":
					v_souhait = true;
					break;
			}
			souhaite.put(idOeuvre, v_souhait);
		}
	}
	
	public int getId(){
		return this.id;
	}
	
	public HashMap<Integer, Integer> getVotes(){
		return this.votes;
	}
	
	public boolean equals(int id){
		return this.id == id;
	}
	
	public String toString(){
		String string = this.id+"\n";
		string += "\tVotes\n";
		for(Integer key : votes.keySet()){
			string +="\t\t"+key+" "+votes.get(key)+"\n";
		}
		string += "\tSouhaits\n";
		for(Integer key : souhaite.keySet()){
			string +="\t\t"+key+" "+souhaite.get(key)+"\n";
		}
		return string;
	}
	
}
