import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	private static String pathGlobal = "datas/"+File.separator;
	private static String pathOeuvres = "titles.csv";
	private static String pathVotes = "watched.csv";
	private static String pathSouhait = "train.csv";
	
	private static ArrayList<Oeuvre> listeOeuvres;
	private static ArrayList<Utilisateur> listeUtilisateurs;

	public static void main(String[] args) {
		
		listeOeuvres = new ArrayList<Oeuvre>();
		listeUtilisateurs = new ArrayList<Utilisateur>();
		
		RecupDonnees();
		
		Utilisateur u = new Utilisateur(2);
		Utilisateur u2 = new Utilisateur(2);
		listeUtilisateurs.add(u);
		if(listeUtilisateurs.contains(u2)){
			System.out.println("Work !");
		}
		
		for(Oeuvre oeuvre : listeOeuvres){
			System.out.println(oeuvre);
		}
		
		for(Utilisateur utilisateur : listeUtilisateurs){
			System.out.println(utilisateur);
		}
		
	}
	
	private static void RecupDonnees(){
		RecupDonnees_listeOeuvres();
		RecupDonnees_listeVotes();
		RecupDonnees_listeSouhait();
	}

	private static void RecupDonnees_listeOeuvres(){
		// Récupération de la liste des oeuvres
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathOeuvres);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("work_id")){
					String[] ligne = line.split(",");
					Oeuvre oeuvre = new Oeuvre(Integer.parseInt(ligne[0]), ligne[1], ligne[2]);
					listeOeuvres.add(oeuvre);
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR : Fichier non trouvé !");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void RecupDonnees_listeVotes(){
		// Récupération de la liste des votes des utilisateurs
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathVotes);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					boolean present = false;
					for(Utilisateur utilisateur : listeUtilisateurs){
						if(utilisateur.getId() == Integer.parseInt(ligne[0])){
							utilisateur.ajoutVote(Integer.parseInt(ligne[1]), ligne[2]);
							present = true;
							break;
						}
					}
					if(!present){
						Utilisateur utilisateur = new Utilisateur(Integer.parseInt(ligne[0]));
						utilisateur.ajoutVote(Integer.parseInt(ligne[1]), ligne[2]);
						listeUtilisateurs.add(utilisateur);
					}
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR : Fichier non trouvé !");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void RecupDonnees_listeSouhait(){
		// Récupération de la liste des votes des utilisateurs
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathSouhait);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					boolean present = false;
					for(Utilisateur utilisateur : listeUtilisateurs){
						if(utilisateur.getId() == Integer.parseInt(ligne[0])){
							utilisateur.ajoutSouhait(Integer.parseInt(ligne[1]), ligne[2]);
							present = true;
							break;
						}
					}
					if(!present){
						Utilisateur utilisateur = new Utilisateur(Integer.parseInt(ligne[0]));
						utilisateur.ajoutVote(Integer.parseInt(ligne[1]), ligne[2]);
						listeUtilisateurs.add(utilisateur);
					}
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR : Fichier non trouvé !");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Utilisateur> ontVoterPour(int idOeuvre){
		ArrayList<Utilisateur> utilisateursAyantVotes = new ArrayList<Utilisateur>();
		for(Utilisateur utilisateur : listeUtilisateurs){
			HashMap<Integer, Integer> votes = utilisateur.getVotes();
			if(votes.containsKey(idOeuvre)){
				utilisateursAyantVotes.add(utilisateur);
			}
		}
		return utilisateursAyantVotes;
	}

}