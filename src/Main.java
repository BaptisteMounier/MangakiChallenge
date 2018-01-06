import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	private static String pathGlobal = "datas/"+File.separator;
	private static String pathOeuvres = "titles.csv";
	private static String pathVotes = "watched.csv";
	private static String pathSouhait = "train.csv";
	private static String pathCible = "test.csv";
	private static String pathResultat = "Resultat.csv";

	private static ArrayList<Oeuvre> listeOeuvres;
	private static ArrayList<Utilisateur> listeUtilisateurs;
	private static ArrayList<Utilisateur> listeCibles;

	public static void main(String[] args) {

		listeOeuvres = new ArrayList<Oeuvre>();
		listeUtilisateurs = new ArrayList<Utilisateur>();
		listeCibles = new ArrayList<Utilisateur>();

		RecupDonnees();
		Traitement();
		Validation();


		//		for(Oeuvre oeuvre : listeOeuvres){
		//			System.out.println(oeuvre);
		//		}
		//
		//		for(Utilisateur utilisateur : listeUtilisateurs){
		//			System.out.println(utilisateur);
		//		}

	}

	private static void RecupDonnees(){
		//RecupDonnees_listeOeuvres(); // Inutile de connaître dans le cadre du TP
		//RecupDonnees_listeVotes();
		RecupDonnees_listeSouhait();
		RecupDonnees_listeCible();
	}

	private static void Traitement(){

		for(Utilisateur cible : listeCibles){
			HashMap<Integer, Integer> souhaitsCible = cible.getSouhaits();
			for(int oeuvreCible : souhaitsCible.keySet()){
				
				// 
				ArrayList<Utilisateur> utilisateursCommun = ontSouhaiterPour(oeuvreCible);
				for(Utilisateur utilisateurCommun : utilisateursCommun){
					HashMap<Integer, Integer> souhaitsCommun = utilisateurCommun.getSouhaits();
					for(int oeuvre : souhaitsCommun.keySet()){
						if(oeuvre == oeuvreCible){
							
						}
					}
				}
				
			}
		}
		
		ecritureResultats();

	}

	private static void Validation(){
		// Trouver et mettre un moyen de calcul de performance auc
		// http://mark.goadrich.com/programs/AUC/
		// http://www.edwardraff.com/jsat_docs/JSAT-0.0.7-javadoc/jsat/classifiers/evaluation/AUC.html#AUC--
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

	private static void RecupDonnees_listeCible(){
		// Récupération de la liste des utilisateurs cibles
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathCible);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					Utilisateur utilisateur = new Utilisateur(Integer.parseInt(ligne[0]));
					utilisateur.ajoutSouhait(Integer.parseInt(ligne[1]), "-1");
					listeCibles.add(utilisateur);
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

	private static void ecritureResultats(){

		try {

			// Assume default encoding.
			FileWriter fileWriter;
			fileWriter = new FileWriter(pathGlobal + pathResultat);
			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write("user_id,work_id,prob_willsee");
			bufferedWriter.newLine();
			for(Utilisateur utilisateur : listeCibles){
				HashMap<Integer, Integer> souhaits = utilisateur.getSouhaits();
				for(int oeuvre : souhaits.keySet()){
					bufferedWriter.write(utilisateur.getId()+","+oeuvre+","+souhaits.get(oeuvre));
					bufferedWriter.newLine();
				}
			}

			bufferedWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	private static ArrayList<Utilisateur> ontSouhaiterPour(int idOeuvre){
		ArrayList<Utilisateur> utilisateursAyantSouhaites = new ArrayList<Utilisateur>();
		for(Utilisateur utilisateur : listeUtilisateurs){
			HashMap<Integer, Integer> souhaits = utilisateur.getSouhaits();
			if(souhaits.containsKey(idOeuvre)){
				utilisateursAyantSouhaites.add(utilisateur);
			}
		}
		return utilisateursAyantSouhaites;
	}

}