package crossword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

public class WordBBDD {
	
	public void loadBBDD() {
			
		/*Introducimos las palabras de los ficheros:*/
		this.readFile("resources/NombresMasculinos.txt", "sustantivo");
		System.out.println("Nombres masculinos ledos.");
		this.readFile("resources/NombresFemeninos.txt", "sustantivo");
		System.out.println("Nombres femeninos ledos.");
		this.readFile("resources/Adjetivos.txt", "adjetivo");
		System.out.println("Adjetivos ledos.");
		this.readFile("resources/Adverbios.txt", "adverbio");
		System.out.println("Adverbios ledos.");
		this.readFile("resources/Articulos.txt", "articulo");
		System.out.println("Articulos ledos.");
		this.readFile("resources/ConjugacionesIrregulares.txt", "conjuncion");
		System.out.println("ConjugacionesIrregulares ledos.");
		this.readFile("resources/Conjunciones.txt", "conjuncion");
		System.out.println("Conjunciones ledos.");
		this.readFile("resources/Expresiones.txt", "expresion");
		System.out.println("Expresiones ledos.");
		this.readFile("resources/Frases.txt", "frase");
		System.out.println("Frases ledos.");
		this.readFile("resources/Interjecciones.txt", "interjeccion");
		System.out.println("Interjecciones ledos.");
		this.readFile("resources/Locuciones.txt", "locucion");
		System.out.println("Locuciones ledos.");
		this.readFile("resources/NombresAmbiguos.txt", "sustantivo");
		System.out.println("NombresAmbiguos ledos.");
		this.readFile("resources/NombresCompuestos.txt", "sustantivo");
		System.out.println("NombresCompuestos ledos.");
		this.readFile("resources/NombresComunes.txt", "sustantivo");
		System.out.println("NombresComunes ledos.");
		this.readFile("resources/NombresMasculinosFemeninos.txt", "sustantivo");
		System.out.println("NombresMasculinosFemeninos ledos.");
		this.readFile("resources/Onomatopeyas.txt", "onomatopeya");
		System.out.println("Onomatopeyas ledos.");
		this.readFile("resources/participiosIrregulares.txt", "participio");
		System.out.println("participiosIrregulares ledos.");
		this.readFile("resources/Preposiciones.txt", "preposicion");
		System.out.println("Preposiciones ledos.");
		this.readFile("resources/Pronombres.txt", "pronombre");
		System.out.println("Pronombres ledos.");
		this.readFile("resources/VerbosAnticuadosDesusados.txt", "verbo");
		System.out.println("VerbosAnticuadosDesusados ledos.");
		this.readFile("resources/VerbosIntransitivos.txt", "verbo");
		System.out.println("VerbosIntransitivos ledos.");
		this.readFile("resources/VerbosIntransitivosPronominales.txt", "verbo");
		System.out.println("VerbosIntransitivosPronominales ledos.");
		this.readFile("resources/VerbosPronominales.txt", "verbo");
		System.out.println("VerbosPronominales ledos.");
		this.readFile("resources/VerbosTransitivos.txt", "verbo");
		System.out.println("VerbosTransitivos ledos.");
		this.readFile("resources/VerbosTransitivosIntransitivos.txt", "verbo");
		System.out.println("VerbosTransitivosIntransitivos ledos.");
		this.readFile("resources/VerbosTransitivosIntransitivosPronominales.txt", "verbo");
		System.out.println("VerbosTransitivosIntransitivosPronominales ledos.");
		this.readFile("resources/VerbosTransitivosPronominales.txt", "verbo");
		System.out.println("VerbosTransitivosPronominales ledos.");
		this.readFile("resources/Vocales.txt", "vocal");
		System.out.println("Vocales ledos.");
		this.readFile("resources/Consonantes.txt", "consonante");
		System.out.println("Consonantes ledos.");
		this.readFile("resources/NombresPropios.txt", "nombre propio");
		System.out.println("Nombres Propios ledos.");
		this.readFile("resources/PaisesCiudades.txt", "geografia");
		System.out.println("Paises y Ciudades ledos.");
		this.readFile("resources/Geograficos.txt", "geografia");
		System.out.println("Rios y Lagos leidos.");
		
	}
	
	public void readFile(String filename, String type) {
		
		/*Conectamos con la BBDD de MySQL:*/
		String url = "jdbc:mysql://localhost:3306/mydb";
		String username = "root";
		String password = "3St0F4d0.!";

		System.out.println("Connecting database...");
		
		Connection connection;
		
		/*Cargamos palabras en la base de datos:*/
		try {
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
			
			try (BufferedReader br = new BufferedReader(
			           new InputStreamReader(new FileInputStream(filename)
			        		   , "UTF-8"));) {
				String line;
				int count = 0;
			    while ((line = br.readLine()) != null && count <100) {
			    	
			    	/*Obtenemos la palabra:*/
			    	String[] parts = line.split("\\/|\n|\t|\\s+");
			    	String word = parts[0];
			    	
			    	String nword = Normalizer.normalize(word, Normalizer.Form.NFD)
			    			.replaceAll("[^\\p{ASCII}]", "");
			    	
			    	//System.out.println(nword);
			    	
			    	/*Insertamos la palabra en BBDD:*/
			    	PreparedStatement pstmt = connection.prepareStatement(
			    			   "INSERT INTO DICCIONARIO (valor, longitud, tipo) values (lower(?), ?, ?)");
			    	pstmt.setString(1, word);
			    	pstmt.setInt(2, word.length());
			    	pstmt.setString(3, type);
			    	pstmt.executeUpdate();
			    	
			    	//count++;
			    	
			    }
			} catch (Exception e){
			    System.err.println(e.getMessage()); // handle exception
			}
			
			connection.close();
		    
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		
	}
	
	/*Retornamos una lista de palabras que cumplen las contraints:*/
	public WordList getWords(String constraints, List<String> forbids) {
		
		WordList wordList = new WordList();
		int count=0;
		
		/*Conectamos con la BBDD de MySQL:*/
		String url = "jdbc:mysql://localhost:3306/mydb?useSSL=false";
		String username = "root";
		String password = "3St0F4d0.!";

		//System.out.println("Connecting database...");
		
		Connection connection;
		
		/*Cargamos palabras en la base de datos:*/
		try {
			connection = DriverManager.getConnection(url, username, password);
			//System.out.println("Database connected!");
			
			String query = "SELECT DISTINCT VALOR, LONGITUD FROM DICCIONARIO WHERE VALOR LIKE ? ";
			
			if(forbids.size() > 0) {
				for(int i=0; i<forbids.size(); i++) {
					query += "AND VALOR NOT LIKE ? ";
				}
			}
			
			query += "ORDER BY RAND() ";
			
			/*Preparamos la query:*/
			PreparedStatement pstmt = connection.prepareStatement(query);
			
			pstmt.setString(1, constraints);
			
			if(forbids.size() > 0) {
				for(int i=0; i<forbids.size(); i++) {
					pstmt.setString(i+2, forbids.get(i));
					//System.out.println(forbids.get(i));
				}
			}
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
		    {
				if(rs.getString("VALOR").length() == constraints.length()) {
					//wordList.wordList.add(new Word(0,0,rs.getInt("LONGITUD"),0,rs.getString("VALOR")));
					wordList.wordList.add(new Word(0,0,rs.getString("VALOR").length(),0,rs.getString("VALOR")));
					count++;
				}
		    }
			
			pstmt.close();
			
			connection.close();
		    
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
		
		wordList.nwords=count;
		return wordList;
		
	}
	

}
