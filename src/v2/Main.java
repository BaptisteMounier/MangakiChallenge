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

public class Main {

	private static String pathGlobal = "datas/"+File.separator;
	private static String pathOeuvres = "titles.csv";
	private static String pathVotes = "watched.csv";
	private static String pathSouhaits = "train.csv";
	private static String pathEntrees = "test.csv";
	private static String pathResultat = "resultat.csv";

	private static int[][] votes;
	private static int[][] souhaits;
	private static int nombreUtilisateur;
	private static int nombreOeuvre;

	public static void main(String[] args) {

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

		RecupDonnees();
		Traitement();
		//Validation();

		double a = 1;
		double b = 2;
		double c = a/b;
		System.out.println(c);
		
		double d = 0;
		double e = 0;
		double f = d/e;
		System.out.println(f);

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
		//RecupDonnees_listeCible();
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

					double totalVxW = 0;
					double totalW = 0;
					boolean voteUtilisable = false;
					
					for(int utilisateur = 0;utilisateur < nombreUtilisateur;utilisateur++){

						if(votes[utilisateur][id_oeuvre] != -1){

							double uv = 0;
							double uvNorme = 0;
							double uvNormeIdUtilisateur = 0;
							double uvNormeUtilisateur = 0;
							boolean utilisable = false;

							for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
								if((votes[id_utilisateur][oeuvre] != -1) && (votes[utilisateur][oeuvre] != -1)){
//									System.out.println(votes[id_utilisateur][oeuvre]);
//									System.out.println(votes[utilisateur][oeuvre]);
									uv += votes[id_utilisateur][oeuvre]*votes[utilisateur][oeuvre];
									uvNormeIdUtilisateur += Math.pow(votes[id_utilisateur][oeuvre], 2);
									uvNormeUtilisateur += Math.pow(votes[utilisateur][oeuvre], 2);
									voteUtilisable = true;
									utilisable = true;
								}
							}

							if(utilisable){
								uvNormeIdUtilisateur = Math.sqrt(uvNormeIdUtilisateur);
								uvNormeUtilisateur = Math.sqrt(uvNormeUtilisateur);
								uvNorme = uvNormeIdUtilisateur * uvNormeUtilisateur;
								double w = uv / uvNorme;
								double vw = votes[utilisateur][id_oeuvre]*w;
								totalVxW += vw;
								totalW += w;
							}

						}
					}
					if(!voteUtilisable){
						for(int utilisateur = 0; utilisateur < nombreUtilisateur;utilisateur++){

							if(souhaits[utilisateur][id_oeuvre] > 0){

								double uv = 0;
								double uvNorme = 0;
								double uvNormeIdUtilisateur = 0;
								double uvNormeUtilisateur = 0;
								boolean utilisable = false;

								for(int oeuvre = 0;oeuvre < nombreOeuvre;oeuvre++){
									if(souhaits[id_utilisateur][oeuvre] > 0 && souhaits[utilisateur][oeuvre] > 0){
										uv += souhaits[id_utilisateur][oeuvre]*souhaits[utilisateur][oeuvre];
										uvNormeIdUtilisateur += Math.pow(souhaits[id_utilisateur][oeuvre], 2);
										uvNormeUtilisateur += Math.pow(souhaits[utilisateur][oeuvre], 2);
										utilisable = true;
									}
								}

								if(utilisable){
									uvNormeIdUtilisateur = Math.sqrt(uvNormeIdUtilisateur);
									uvNormeUtilisateur = Math.sqrt(uvNormeUtilisateur);
									uvNorme = uvNormeIdUtilisateur * uvNormeUtilisateur;
									double w = uv / uvNorme;
									double vw = souhaits[utilisateur][id_oeuvre]*w;
									totalVxW += vw;
									totalW += w;
								}

							}

						}
					}

					double estimation = totalVxW / totalW;

					double percent = (double) nbligne/100016 *100;
					System.out.print(percent+"\t");
					System.out.println(id_utilisateur+"\t"+id_oeuvre+"\t"+estimation);

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
		//
		//		try {
		//
		//			// Assume default encoding.
		//			FileWriter fileWriter;
		//			fileWriter = new FileWriter(pathGlobal + pathResultat);
		//			// Always wrap FileWriter in BufferedWriter.
		//			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		//
		//			bufferedWriter.write("user_id,work_id,prob_willsee");
		//			bufferedWriter.newLine();
		//			for(Utilisateur utilisateur : listeCibles){
		//				HashMap<Integer, Integer> souhaits = utilisateur.getSouhaits();
		//				for(int oeuvre : souhaits.keySet()){
		//					bufferedWriter.write(utilisateur.getId()+","+oeuvre+","+souhaits.get(oeuvre));
		//					bufferedWriter.newLine();
		//				}
		//			}
		//
		//			bufferedWriter.close();
		//
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

	}
	//
	//	private static void Validation(){
	//		// Trouver et mettre un moyen de calcul de performance auc
	//		// http://mark.goadrich.com/programs/AUC/
	//		// http://www.edwardraff.com/jsat_docs/JSAT-0.0.7-javadoc/jsat/classifiers/evaluation/AUC.html#AUC--
	//	}
	//
	//	private static void RecupDonnees_listeOeuvres(){
	//		// Récupération de la liste des oeuvres
	//	}

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
	//
	//	private static void RecupDonnees_listeCible(){
	//	}
	//
	//	private static void ecritureResultats(){
	//
	//		try {
	//
	//			// Assume default encoding.
	//			FileWriter fileWriter;
	//			fileWriter = new FileWriter(pathGlobal + pathResultat);
	//			// Always wrap FileWriter in BufferedWriter.
	//			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	//
	//			bufferedWriter.write("user_id,work_id,prob_willsee");
	//			bufferedWriter.newLine();
	//			for(Utilisateur utilisateur : listeCibles){
	//				HashMap<Integer, Integer> souhaits = utilisateur.getSouhaits();
	//				for(int oeuvre : souhaits.keySet()){
	//					bufferedWriter.write(utilisateur.getId()+","+oeuvre+","+souhaits.get(oeuvre));
	//					bufferedWriter.newLine();
	//				}
	//			}
	//
	//			bufferedWriter.close();
	//
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//
	//	}

}