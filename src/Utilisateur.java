import java.util.HashMap;

public class Utilisateur {
	
	private int id;
	private HashMap<Integer, Integer> votes;
	private Double votesMoy;
	private HashMap<Integer, Integer> souhaits;
	private Double souhaitsMoy;
	
	public Utilisateur(int id){
		this.id = id;
		this.votes = new HashMap<Integer, Integer>();
		this.souhaits = new HashMap<Integer, Integer>();
	}
	
	public void ajoutVote(int idOeuvre, String vote){
		if(votes.containsKey(idOeuvre)){
			System.out.println("ERROR : L'utilisateur "+this.id+" a déjà voté pour l'oeuvre "+idOeuvre+" !");
		}else{
			int v_vote;
			switch(vote){
				case "dislike":
					v_vote = 0;
					break;
				case "neutral":
					v_vote = 1;
					break;
				case "like":
					v_vote = 2;
					break;
				case "love":
					v_vote = 3;
					break;
				default:
					v_vote = -1;
					break;
			}
			votes.put(idOeuvre, v_vote);
		}
	}
	
	public void calcVotesMoy(){
		int nb = 0;
		int total = 0;
		for(Integer key : votes.keySet()){
			int value = votes.get(key);
			if(value <= 0){
				nb++;
				total += value;
			}
		}
		votesMoy = (double) (total / nb);
	}
	
	public void ajoutSouhait(int idOeuvre, String souhait){
		if(votes.containsKey(idOeuvre)){
			System.out.println("ERROR : L'utilisateur "+this.id+" a déjà exprimé un souhait pour l'oeuvre "+idOeuvre+" !");
		}else{
			int v_souhait;
			switch(souhait){
				case "0":
					v_souhait = 0;
					break;
				case "1":
					v_souhait = 1;
					break;
				default:
					v_souhait = -1;
					break;
			}
			souhaits.put(idOeuvre, v_souhait);
		}
	}
	
	public void calcSouhaitsMoy(){
		int nb = 0;
		int total = 0;
		for(Integer key : souhaits.keySet()){
			int value = souhaits.get(key);
			if(value <= 0){
				nb++;
				total += value;
			}
		}
		souhaitsMoy = (double) (total / nb);
	}
	
	public int getId(){
		return this.id;
	}
	
	public HashMap<Integer, Integer> getVotes(){
		return this.votes;
	}
	
	public HashMap<Integer, Integer> getSouhaits(){
		return this.souhaits;
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
		for(Integer key : souhaits.keySet()){
			string +="\t\t"+key+" "+souhaits.get(key)+"\n";
		}
		return string;
	}
	
}
