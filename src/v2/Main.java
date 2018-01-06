package v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class Main {

	private static String pathGlobal = "datas/"+File.separator;
//	private static String pathOeuvres = "titles.csv";
	private static String pathVotes = "watched.csv";
	private static String pathSouhaits = "train.csv";
	private static String pathEntrees = "test.csv";
	private static String pathResultat = "resultat.csv";
	private static String pathObjectif = "submission.csv";

	private static int[][] votes;
	private static int[][] souhaits;
	private static int nombreUtilisateur;
	private static int nombreOeuvre;

	private static HashMap<Integer, Double> moyVotes;
	private static HashMap<Integer, Double> moySouhaits;

	private static ArrayList<String> objectif;
	private static ArrayList<String> resultat;	

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		nombreUtilisateur = 1982;
		nombreOeuvre = 9896;
		votes = new int[nombreUtilisateur+1][nombreOeuvre+1];
		for(int i = 0;i < nombreUtilisateur;i++){
			for(int j = 0;j < nombreOeuvre;j++){
				votes[i][j] = -1;
			}
		}
		souhaits = new int[nombreUtilisateur+1][nombreOeuvre+1];
		for(int i = 0;i < nombreUtilisateur;i++){
			for(int j = 0;j < nombreOeuvre;j++){
				souhaits[i][j] = -1;
			}
		}
		resultat = new ArrayList<String>();
		objectif = new ArrayList<String>();

		RecupDonnees();
		CalculMoyennes();
		Traitement();
		Validation();
		
		System.out.println(System.currentTimeMillis()-start);
		

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
		RecupDonnees_votes();
		RecupDonnees_souhaits();
		RecupDonnees_objectif();
	}

	private static void Traitement(){

		try {

			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathEntrees);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			FileWriter fileWriter;
			fileWriter = new FileWriter(pathGlobal + pathResultat);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);			

			bufferedWriter.write("user_id,work_id,prob_willsee");
			bufferedWriter.newLine();

			String line;
			int nbligne = 0;
			while((line = bufferedReader.readLine()) != null) {
				nbligne++;
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					int id_utilisateur = Integer.parseInt(ligne[0]);
					int id_oeuvre = Integer.parseInt(ligne[1]);

					double estimation_haut = 0;
					double estimation_bas = 0;

					for(int utilisateur = 0;utilisateur < nombreUtilisateur;utilisateur++){

						double w = 0;
						boolean utilisable = false;

						if(votes[utilisateur][id_oeuvre] != -1){

							double w_haut = 0;
							double w_bas_gauche = 0;
							double w_bas_droit = 0;

							for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
								if((votes[id_utilisateur][oeuvre] > 0) && (votes[utilisateur][oeuvre] > 0)){

									w_haut += (votes[id_utilisateur][oeuvre] * votes[utilisateur][oeuvre]);
									w_bas_gauche += Math.pow(votes[id_utilisateur][oeuvre], 2);
									w_bas_droit += Math.pow(votes[utilisateur][oeuvre], 2);
									utilisable = true;

								}
							}

							if(utilisable){
								w = w_haut/(Math.sqrt(w_bas_gauche*w_bas_droit));
								estimation_haut += votes[utilisateur][id_oeuvre]*w;
								estimation_bas += w;
							}

						}if(souhaits[utilisateur][id_oeuvre] != -1){

							double w_haut = 0;
							double w_bas_gauche = 0;
							double w_bas_droit = 0;

							for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
								if((souhaits[id_utilisateur][oeuvre] > 0) && (souhaits[utilisateur][oeuvre] > 0)){

									w_haut += (souhaits[id_utilisateur][oeuvre] * souhaits[utilisateur][oeuvre]);
									w_bas_gauche += Math.pow(souhaits[id_utilisateur][oeuvre], 2);
									w_bas_droit += Math.pow(souhaits[utilisateur][oeuvre], 2);
									utilisable = true;

								}
							}

							if(utilisable){
								w = w_haut/(Math.sqrt(w_bas_gauche*w_bas_droit));
								estimation_haut += souhaits[utilisateur][id_oeuvre]*w;
								estimation_bas += w;
							}

						}

					}

					double estimation = estimation_haut / estimation_bas;

					double percent = (double) nbligne/100016 *100;
					System.out.print("Avancement: "+percent+"\t");
					System.out.println("Utilisateur: "+id_utilisateur+"\tOeuvre: "+id_oeuvre+"\tEstimation: "+estimation);

					resultat.add(id_utilisateur+","+id_oeuvre+","+estimation);
					bufferedWriter.write(id_utilisateur+","+id_oeuvre+","+estimation);
					bufferedWriter.newLine();

				}

			}

			bufferedReader.close();
			bufferedWriter.close();

		} catch (FileNotFoundException e) {
			System.out.println("ERROR : Fichier non trouvé !");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void CalculMoyennes(){
		moyVotes = new HashMap<Integer, Double>();
		for(int utilisateur = 0;utilisateur < nombreUtilisateur;utilisateur++){
			double somme = 0;
			double nombre = 0;
			boolean utilisable = false;
			for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
				int vote = votes[utilisateur][oeuvre];
				if(vote != -1){
					somme += vote;
					nombre++;
					utilisable = true;
				}
			}
			if(utilisable){
				double moyenne = somme/nombre;
				moyVotes.put(utilisateur, moyenne);
			}
		}

		moySouhaits = new HashMap<Integer, Double>();
		for(int utilisateur = 0;utilisateur < nombreUtilisateur;utilisateur++){
			double somme = 0;
			double nombre = 0;
			boolean utilisable = false;
			for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
				int souhait = souhaits[utilisateur][oeuvre];
				if(souhait != -1){
					somme += souhait;
					nombre++;
					utilisable = true;
				}
			}
			if(utilisable){
				double moyenne = somme/nombre;
				moySouhaits.put(utilisateur, moyenne);
			}
		}
	}

	private static void Validation(){
		// Trouver et mettre un moyen de calcul de performance auc
		// http://mark.goadrich.com/programs/AUC/
		// http://www.edwardraff.com/jsat_docs/JSAT-0.0.7-javadoc/jsat/classifiers/evaluation/AUC.html#AUC--
		
	
		if(objectif.size() == resultat.size()){
			double erreurTotal = 0;
			int nombreCas = 0;
			int nombreCasNonResolu = 0;
			for(int line = 0;line < objectif.size();line++){
				String[] entreObj = objectif.get(line).split(",");
				String[] entreRes = resultat.get(line).split(",");
				if(entreObj[0].equals(entreRes[0]) && entreObj[1].equals(entreRes[1])){
					double prediction = Double.parseDouble(entreRes[2]);
					double reel = Double.parseDouble(entreObj[2]);
					if(Double.isNaN(prediction)){
						nombreCasNonResolu++;
					}else{
						double erreur = Math.sqrt(Math.pow(prediction - reel, 2));
						erreurTotal += erreur;
						nombreCas++;
					}
				}else{
					System.out.println("L'objectif et le résultat ne comportent pas le même couple utilisateur/oeuvre.");
				}
			}
			double mae = erreurTotal / nombreCas;
			System.out.println("La valeur mae du système de recommandations est de "+mae+" et dispose de "+nombreCasNonResolu+" cas non résolu (NaN).");
			System.out.println(erreurTotal+" "+nombreCas);
		}else{
			System.out.println("L'objectif et le résultat ne comportent pas le même nombre d'élément.");
		}
	
	}

	private static void RecupDonnees_votes(){
		// Récupération des votes des utilisateurs
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathVotes);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					int id_utilisateur = Integer.parseInt(ligne[0]);
					int id_oeuvre = Integer.parseInt(ligne[1]);
					int vote;
					switch(ligne[2]){
					case "dislike":
						vote = 0;
						break;
					case "neutral":
						vote = 0;
						break;
					case "like":
						vote = 1;
						break;
					case "love":
						vote = 1;
						break;
					default:
						vote = -1;
						break;
					}
					//System.out.println(id_utilisateur+" "+id_oeuvre+" "+vote);
					votes[id_utilisateur][id_oeuvre] = vote;
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

	private static void RecupDonnees_souhaits(){
		// Récupération des votes des utilisateurs
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathSouhaits);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					String[] ligne = line.split(",");
					int id_utilisateur = Integer.parseInt(ligne[0]);
					int id_oeuvre = Integer.parseInt(ligne[1]);
					int souhait;
					switch(ligne[2]){
					case "0":
						souhait = 0;
						break;
					case "1":
						souhait = 1;
						break;
					default:
						souhait = -1;
						break;
					}
					//System.out.println(id_utilisateur+" "+id_oeuvre+" "+souhait);
					souhaits[id_utilisateur][id_oeuvre] = souhait;
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

	private static void RecupDonnees_objectif(){
		// Récupération des votes des utilisateurs
		try {
			FileReader fileReader;
			fileReader = new FileReader(pathGlobal + pathObjectif);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;

			while((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("user_id")){
					objectif.add(line);
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

}