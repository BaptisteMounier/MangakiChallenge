
public class Oeuvre {
	
	private int id;
	private String titre;
	private String categorie;
	
	public Oeuvre(int id, String titre, String catagorie){
		this.id = id;
		this.titre = titre;
		this.categorie = catagorie;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getTitre(){
		return this.titre;
	}
	
	public String getCatagorie(){
		return this.categorie;
	}
	
	public String toString(){
		return this.id+" - "+this.titre+ " - "+this.categorie;
	}

}
