package net.codejava;

import java.sql.*;
import java.util.*;

public class ristoranteManager {
	// connessione al database
	private static final String url = "jdbc:mysql://localhost:3306/ristorante";
	// impostazione nome utente
	private static final String username = "root";
	// password
	private static final String password = "#";

	private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS menu(id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(100) NOT NULL, prezzo DOUBLE NOT NULL)";
	private static final String INSERT_QUERY = "INSERT INTO menu(nome,prezzo) VALUES(?,?)";
	private static final String UPDATE_QUERY = "UPDATE menu SET nome = ?, prezzo = ? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE FROM menu WHERE id = ?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM menu";

	public static void main(String args[]) {
		// chiamata del metodo per la creazione della tabella menu se non esiste
		createTableIfNotExists();

		Scanner input = new Scanner(System.in);
		int scelta = -1; // cosi questo numero non fara mai parte del menu

		// partenza del menu principale dell'apllicazione
		while (scelta != 0) {
			// definiamo il menu principale dell'applicazione
			System.out.println("Seleziona un operazione: ");
			System.out.println("-----------------------");
			System.out.println("\n");
			System.out.println("1. Aggiungi piatto al menu!");
			System.out.println("2. Visualizza il menu!");
			System.out.println("3. Modifica un piatto!");
			System.out.println("4. Elimina un piatto!");
			System.out.println("0. Exit");

			// intercettiamo inserimento dati da parte dell'utente
			scelta = input.nextInt();
			// intercettiamo la nuova riga a capo dopo il nextInt
			input.nextLine();

			// creazione metodo switch per utilizzare il menu del nostro programma
			switch (scelta) {
			case 1:
				System.out.print("Inserisci il nome del piatto: ");
				String nome = input.nextLine();

				System.out.print("Inserisci il prezzo del piatto: ");
				double prezzo = input.nextDouble();
				input.nextLine();

				// chiamata del motodo per l'inserimento di un piatto
				insertPiatto(nome, prezzo);
				System.out.println("Piatto aggiunto al menu con successo!");
				break;

			case 2:
				System.out.println("Menu: ");
				System.out.println("------");
				printMenu();
				break;

			case 3:
				System.out.print("Inserisci l'id del piatto da modificare: ");
				int idModifica = input.nextInt();
				input.nextLine();

				System.out.print("Inserisci il nuovo nome del piatto: ");
				String nuovoNome = input.nextLine();

				System.out.print("Inserisci il nuovo prezzo del piatto: ");
				double nuovoPrezzo = input.nextDouble();
				

				// chiamata metodo per aggiornamento del record
				updatePiatto(idModifica, nuovoNome, nuovoPrezzo);
				System.out.println("Piatto modificato con successo!");
				break;
			case 4:
				System.out.print("Inserisci l'id del piatto da eliminare: ");
				int idElimina = input.nextInt();
				input.nextLine(); //consumiamo la riga prodotta da nextInt
				
				//chiamata per l'eliminazione dei rercord
				deletePiatto(idElimina);
				System.out.println("Piatto rimosso con successo!");
				break;
			case 0:
				System.out.println("Arrivederci!!");
				break; //poiche abbiamo la condizione del while non ci serve il return ma usiamo direttamente il break
				default:
					System.out.println("Scelta inserita non valida!!");
					break;
			}
		}
		
		//chiusura dell'ogetto scanner
		input.close();
	}

	// Metodi

	// metodo per la creazione della tabella menu se non esiste gia nel database
	private static void createTableIfNotExists() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(CREATE_TABLE_QUERY);
			//chiusura delle connessioni relative al metodo in corso
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Errore di connessione al database!");
			e.printStackTrace();
		}
	}

	// metodo er l'inserimento di un nuovo record all'interno della tabella menu
	private static void insertPiatto(String nome, double prezzo) {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
			stmt.setString(1, nome);
			stmt.setDouble(2, prezzo);
			//esecuzione query
			stmt.executeUpdate();
			//chiusura delle connessioni relative al metodo in corso
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Errore di connessione al database!");
			e.printStackTrace();
		}
	}

	// metodo per la visualizzazione di tutti i record della tabella
	private static void printMenu() {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SELECT_ALL_QUERY);

			// scorriamo all'interno del result set per visualizzare tutti i record presenti
			while (rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				Double prezzo = rs.getDouble("prezzo");
				// presi i dati facciamo la stampa
				System.out.println("ID: " + id + "\nNome: " + nome + "\nPrezzo: " + prezzo);
			}
			//chiusura connessioni relative al metodo in corso
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Errore di connessione al database!");
			e.printStackTrace();
		}
	}

	// metodo per l'aggiornamento di un record presente su tabella menu mediante la
	// scelta dell'ID
	private static void updatePiatto(int id, String nome, double prezzo) {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, nome);
			stmt.setDouble(2, prezzo);
			stmt.setInt(3, id);
			//esecuzione query
			stmt.executeUpdate();
			//chiusura connessioni relative al metodo in corso
			stmt.close();
			conn.close();	

		} catch (SQLException e) {
			System.out.println("Errore di connessione al database!");
			e.printStackTrace();
		}
	}
	
	//metodo per la funzione per l'eliminazione dei record
	private static void deletePiatto(int id) {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, id);
			//esecuzione query
			stmt.executeUpdate();
			//chiusura connessioni relative al metodo in corso
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Errore di connessione al database!");
			e.printStackTrace();
		}
	}
}
