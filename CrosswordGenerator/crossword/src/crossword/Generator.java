package crossword;

import java.io.IOException;

public class Generator {
	
	public static void main(String[] args) {
		
		Grid grid = new Grid(10,10);
		
		int done = 1;
		
		/*Obtenemos la lista de palabras:*/
		WordList wordList = new WordList();
		wordList.getWords(grid);
		
		/*Dibujamos el grid generado:*/
		try {
			grid.printGrid(wordList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Cargamos la BBDD con el diccionario: (SOLO LA PRIMERA VEZ)*/
		/*WordBBDD wordddbb = new WordBBDD();
		wordddbb.loadBBDD();*/
		
		/*Rellenamos el GRID:*/
		try {
			
			done=grid.fillGrid_v3(wordList, -1);
			System.out.println("Crucigrama Generado!");
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
