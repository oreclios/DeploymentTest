package crossword;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
	
	public int nrows;
	public int ncols;
	public int nwords;
	public int nholes;
	public String gridv[][];
	
	public int cv;
	
	List<List<String>> forbWords;
	
	public Grid(int nrow, int ncol) {
		this.nrows=nrow;
		this.ncols=ncol;
		this.gridv = new String[nrow][ncol];
		this.forbWords = new ArrayList<List<String>>();
		
		/*Establecemos todos los valores a 1:*/
		for(int i=0; i<nrow; i++) {
			for(int j=0; j<ncol; j++) {
				this.gridv[i][j]="1";
			}
		}
		
		addHoles(this);
		
		/*Insertamos los huecos donde queremos:*/
		/*this.gridv[0][4]="0";
		this.gridv[0][9]="0";
		this.gridv[1][2]="0";
		this.gridv[2][4]="0";
		this.gridv[2][7]="0";
		this.gridv[3][3]="0";
		this.gridv[4][5]="0";
		this.gridv[5][1]="0";
		this.gridv[5][6]="0";
		this.gridv[6][3]="0";
		this.gridv[6][8]="0";
		this.gridv[7][1]="0";
		this.gridv[7][5]="0";
		this.gridv[8][4]="0";
		this.gridv[9][7]="0";*/
		
		/*try {
			this.printGrid(new WordList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		this.nwords=countWords(this);
		
		for(int i=0; i<this.nwords; i++) {
			this.forbWords.add(new ArrayList<String>());
		}
		
	}
	
	private void addHoles(Grid grid) {
		
		int done;
		
		/*Determinamos numero de huecos*/
		int perct = ThreadLocalRandom.current().nextInt(15, 25);
		int nHoles = (int)((grid.nrows*grid.ncols)*0.01*perct);
		grid.nholes = nHoles;
		
		//System.out.printf("Numero de huecos: %d\n", nHoles);
		
		for(int i=0; i<nHoles; i++)
		{
			done=0;
			do {
				int x = ThreadLocalRandom.current().nextInt(0, grid.nrows);
				int y = ThreadLocalRandom.current().nextInt(0, grid.ncols);
				
				if(grid.gridv[x][y]!="0") {
					grid.gridv[x][y]="0";
					done=1;
				}
			}while(done==0);
		}
	}
	
	private int countWords(Grid grid) {
		
		int nwords=0;
		int count=0;
		
		/*Palabras por filas:*/
		for(int i=0; i<grid.nrows; i++) {
			count=0;
			for(int j=0; j<grid.ncols; j++) {
				//if(grid.gridv[i][j] == "0" && j>0 && j<grid.ncols-1) {
				if(grid.gridv[i][j] == "0" && j>0) {
					if(grid.gridv[i][j-1] != "0")
						count++;
				}else if (grid.gridv[i][j] != "0" && j==grid.ncols-1) {
					count++;
				}
			}
			
			nwords+=count;
		}
		
		int rwords=nwords;
		//System.out.printf("rows words: %d\n", rwords);
		
		
		/*Palabras por columnas:*/
		for(int i=0; i<grid.ncols; i++) {
			count=0;
			for(int j=0; j<grid.nrows; j++) {
				//if(grid.gridv[j][i] == "0" && j>0 && j<grid.nrows-1) {
				if(grid.gridv[j][i] == "0" && j>0) {
					if(grid.gridv[j-1][i] != "0")
						count++;
				}else if (grid.gridv[j][i] != "0" && j==grid.nrows-1) {
					count++;
				}
			}
			nwords+=count;
		}
		
		int cwords=nwords-rwords;
		//System.out.printf("cols words: %d\n", cwords);
		
		return nwords;
	}
	
	public void printGrid(WordList wordList) throws IOException{
		
		FileWriter fileWriter = new FileWriter("grid.out");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		printWriter.printf("Crossword of %d words:\n\n", this.nwords);
		
		for(int i=0; i<this.nrows; i++) {
			for(int j=0; j<this.ncols; j++) {
				if(this.gridv[i][j] == "0") {
					if(j==0)
						printWriter.print("\tX ");
					else
						printWriter.print("X ");
				}
				else {
					if(j==0)
						if(this.gridv[i][j] == "1")
							printWriter.print("\t_ ");
						else
							printWriter.printf("\t%s ",this.gridv[i][j]);
					else
						if(this.gridv[i][j] == "1")
							printWriter.print("_ ");
						else
							printWriter.printf("%s ",this.gridv[i][j]);
				}
				if(j==this.ncols-1)
					printWriter.print("\n");
			}
		}
		
		
		//Escribimos las descripciones de las palabras:
		printWriter.print("\nHORIZONTALES:\n");
		//Escribimos palabras horizontales:
		for(int i=0; i<wordList.nwords; i++) {
			if(wordList.wordList.get(i).pos == 0) {
				printWriter.printf("Pos(%d,%d)\tLongitud %d\t\t- %s\n",wordList.wordList.get(i).row
						,wordList.wordList.get(i).col,wordList.wordList.get(i).length, wordList.wordList.get(i).value);
			}
		}
		
		printWriter.print("\nVERTICALES:\n");
		//Escribimos palabras verticales:
		for(int i=0; i<this.nwords; i++) {
			if(wordList.wordList.get(i).pos == 1) {
				printWriter.printf("Pos(%d,%d)\tLongitud %d\t\t- %s\n",wordList.wordList.get(i).row
						,wordList.wordList.get(i).col,wordList.wordList.get(i).length, wordList.wordList.get(i).value);
			}
		}
		
		/*Dibujamos las soluciones:*/
		/*printWriter.print("\nSOLUCION:\n");
		
		for(int i=0; i<this.nrows; i++) {
			for(int j=0; j<this.ncols; j++) {
				if(this.gridv[i][j] == "0") {
					if(j==0)
						printWriter.print("\tX ");
					else
						printWriter.print("X ");
				}
				else {
					if(j==0)
						printWriter.printf("\t%s ", this.gridv[i][j]);
					else
						printWriter.printf("%s ", this.gridv[i][j]);
				}
				if(j==this.ncols-1)
					printWriter.print("\n");
			}
		}*/
		
		printWriter.close();
	}
	
	public void fillGrid(WordList wordList) throws IOException {
		
		int done = 0;
		int i = 0;
		int found = 0;
		int best=100;
		
		do
		{
			done=0;
			
			/*Generamos palabra a palabra:*/
			for(int t=0; t<this.nwords; t++) {
				
				/*Se rellenan las palabras en orden aleatorio:*/
				do {
					found=0;
					i = ThreadLocalRandom.current().nextInt(0, this.nwords);
					if(wordList.wordList.get(i).found==0) {
						found=1;
					}
				}while(found!=1);
			
				char[] constrs;
				constrs = new char[wordList.wordList.get(i).length];
				String constraints;
			
				/*Obtenemos las constraints:*/
				/*Leemos las posiciones del GRID que ocupa la palabra:*/
				/*Si es HORIZONTAL:*/
				if(wordList.wordList.get(i).pos == 0) {
					
					int row = wordList.wordList.get(i).row;
					int ini = wordList.wordList.get(i).col;
					int fin = ini+wordList.wordList.get(i).length;
					
					//System.out.printf("horizontal, columnas %d:%d\n", ini, fin-1);
					
					for(int k=ini; k<fin; k++) {
						if(this.gridv[row][k] != "0" && this.gridv[row][k] != "1") {
							constrs[k-ini]=this.gridv[row][k].charAt(0);
							//System.out.printf("constr(%d,%d): %s\n", row, k, this.gridv[row][k]);
						}else {
							constrs[k-ini]='_';
						}
					}
				
				}
				/*Si es VERTICAL:*/
				else {
				
					int col = wordList.wordList.get(i).col;
					int ini = wordList.wordList.get(i).row;
					int fin = ini+wordList.wordList.get(i).length;
					
					//System.out.printf("vertical, columnas %d:%d\n", ini, fin-1);
				
					for(int k=ini; k<fin; k++) {
						if(this.gridv[k][col] != "0" && this.gridv[k][col] != "1") {
							constrs[k-ini]=this.gridv[k][col].charAt(0);
							//System.out.printf("constr(%d,%d): %s\n", k, col, this.gridv[k][col]);
						}else {
							constrs[k-ini]='_';
						}
					}
				
				}
			
				/*Obtenemos las palabras que cumplen las constraints:*/
				WordBBDD wordddbb = new WordBBDD();
			
				constraints = String.valueOf(constrs);
				//System.out.printf("constrains: '%s'\n", constraints);
			
				WordList words = wordddbb.getWords(constraints, this.forbWords.get(i));
			
				if(words.nwords > 0) {
					
					wordList.wordList.get(i).found=1;
					
					/*Seleccionamos palabra aleatoria entre las obtenidas:*/
					int w = ThreadLocalRandom.current().nextInt(0, words.nwords);
					
					String nword = Normalizer.normalize(words.wordList.get(w).value, Normalizer.Form.NFD)
			    			.replaceAll("[^\\p{ASCII}]", "");
					
					wordList.wordList.get(i).setValue(nword);
					
					/*System.out.printf("palabra %d(%d): (%d, %d) %s \n",i
							, wordList.wordList.get(i).pos 
							, wordList.wordList.get(i).row
							, wordList.wordList.get(i).col
							, wordList.wordList.get(i).value);*/
				
					/*ACTUALIZAMOS EL GRID:*/
					/*Si es HORIZONTAL:*/
					if(wordList.wordList.get(i).pos == 0) {
						
						int row = wordList.wordList.get(i).row;
						int ini = wordList.wordList.get(i).col;
						int fin = ini+wordList.wordList.get(i).length;
						
						for(int k=ini; k<fin; k++) {
							this.gridv[row][k] = String.
									valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
						}
					
					}
					/*Si es VERTICAL:*/
					else {
					
						int col = wordList.wordList.get(i).col;
						int ini = wordList.wordList.get(i).row;
						int fin = ini+wordList.wordList.get(i).length;
					
						for(int k=ini; k<fin; k++) {
							this.gridv[k][col] = String.
									valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
						}
					
					}
					
					/*Pintamos el GRID actualizado:*/
					this.printGrid(wordList);
					
				}else {
					//System.out.println("No se ha encontrado ninguna palabra que cumpla las condiciones.");
					done++;
					//break;
				}
			}
			
			if(done<best) {
				best=done;
				/*System.out.printf("Palabras problematicas: %d \n", best);*/
			}
			
			/*Reseteamos el GRID:*/
			if(done != 0) {
				for(int j=0; j<this.nrows; j++) {
					for(int k=0; k<this.ncols; k++) {
						if(this.gridv[j][k] != "0")
							this.gridv[j][k] = "1";
					}
				}
			}
			
			/*Reseteamos palabras:*/
			for(int j=0; j<this.nwords; j++) {
				wordList.wordList.get(j).found=0;
			}
			
		}while(done != 0);
		
	}
	
	public void fillGrid_v2(WordList wordList) throws IOException{
		
		int error;
		int found;
		int i;
		int count=0;
		int count2;
		int neww;
		int obj=-1;
		
		/*Generamos palabra a palabra:*/
		do{
			
			/*Se rellenan las palabras en orden aleatorio:*/
			if(obj<0)
			{
				count2=0;
				do {
					found=0;
					i = ThreadLocalRandom.current().nextInt(0, this.nwords);
					count2++;
					if(wordList.wordList.get(i).found==0) {
						/*if(this.validWord(wordList,i) == 0 || count2 > this.nwords)
							found=1;*/
					}
				}while(found!=1);
			}else {
				i = obj;
			}
			
			/*System.out.printf("Siguiente palabra: %d (%d, %d) [%d]\n",
					 i, wordList.wordList.get(i).row, wordList.wordList.get(i).col, 
					 wordList.wordList.get(i).pos);*/
			
			error = this.nextWord(wordList, i);
			
			if(error==0) {
					wordList.last=i;
					obj=-1;
					count++;
					//System.out.printf("count antes: %d\n", count);
					this.testWords(wordList);
					count=count;
					//System.out.printf("count despues: %d\n", count);
			}
			else {
				wordList.wordList.get(wordList.last).found=0;
				this.updateGrid(wordList);
				obj=wordList.last;
				count--;
			}
			
			this.printGrid(wordList);
			
			//System.out.printf("palabras encontradas: %d\n", count);
	
		}while(count<this.nwords);
		
		return;
		
	}
	
	/*Metodo Recursivo:*/
	public int fillGrid_v3(WordList wordList, int n) throws IOException{
		
		int i;
		int count=0;
		int done=1;
		List<Integer> wordsIdx = new ArrayList<Integer>();
		
		/*Obtenemos la siguiente palabra:*/
		i = getNextWord(wordList, n);
		
		/*System.out.printf("Siguiente palabra: %d (%d, %d) [%d]\n",
				 i, wordList.wordList.get(i).row, wordList.wordList.get(i).col, 
				 wordList.wordList.get(i).pos);*/
		
		/*Obtenemos las posibles palabras:*/
		WordList pos_words = new WordList();
		pos_words = this.getValidWords(wordList, i);
		
		if(pos_words.nwords == 0)
			return 1;
		
		for(int k=0; k<pos_words.nwords; k++)
		{
			count = 0;
			
			//System.out.printf("palabra prueba 1: %s\n", pos_words.wordList.get(k).getValue());
			
			if(this.nextWord_v2(wordList, pos_words, i, k) == 0) {
				wordList.last=i;
				wordsIdx = this.testWords(wordList);
							
				/*Contamos el numero de palabras encontradas:*/
				for(int j=0; j<this.nwords; j++) {
					if(wordList.wordList.get(j).found == 1)
						count++;
				}
				
				if(count < this.nwords) {
					this.updateGrid(wordList);
					this.printGrid(wordList);
					//System.out.printf("Palabra establecida: %s\n", wordList.wordList.get(i).value);
					done=fillGrid_v3(wordList, i);
				}else {
					this.updateGrid(wordList);
					this.printGrid(wordList);
					return 0;
				}
			}
			else
			{
				wordList.wordList.get(i).found=0;
				this.forbWords.get(i).add(wordList.wordList.get(i).value);
				wordList.wordList.get(i).setValue("");
				//this.testWords(wordList);
				this.updateGrid(wordList);
				this.printGrid(wordList);
				//return 1;
			}
			
			if(done == 0)
				break;
			else {
				wordList.wordList.get(i).found=0;
				this.forbWords.get(i).add(wordList.wordList.get(i).value);
				wordList.wordList.get(i).setValue("");
				for(int j=0; j<wordsIdx.size(); j++) {
					wordList.wordList.get(wordsIdx.get(j)).found=0;
					this.forbWords.get(wordsIdx.get(j)).add(wordList.wordList.get(wordsIdx.get(j)).value);
					wordList.wordList.get(wordsIdx.get(j)).setValue("");
					wordsIdx.clear();
				}
			}
			
		}
		
		if(done != 0)
		{
			wordList.wordList.get(i).found=0;
			
			if(n>=0) {
				wordList.wordList.get(n).found=0;
				this.forbWords.get(n).add(wordList.wordList.get(n).value);
				wordList.wordList.get(n).setValue("");
				for(int j=0; j<wordsIdx.size(); j++) {
					wordList.wordList.get(wordsIdx.get(j)).found=0;
					this.forbWords.get(wordsIdx.get(j)).add(wordList.wordList.get(wordsIdx.get(j)).value);
					wordList.wordList.get(wordsIdx.get(j)).setValue("");
					wordsIdx.clear();
				}
				//System.out.printf("Reconsiderar palabra: %s\n", wordList.wordList.get(n).value);
				//this.testWords(wordList);
			}else {
				wordsIdx.clear();
				//System.out.println("Empezando de nuevo");
			}
			
			
			
			this.updateGrid(wordList);
			this.printGrid(wordList);
			
			return 1;
		}
		else
			return 0;
		
	}
	
	public int getNextWord(WordList wordList, int last) {
		
		/*int n;
		int count2=0;
		int found;
		
		do {
			found=0;
			n = ThreadLocalRandom.current().nextInt(0, this.nwords);
			count2++;
			if(wordList.wordList.get(n).found==0) {
				if(this.validWord(wordList,n,last) != -1 || count2 > this.nwords)
					found=1;
			}
		}while(found!=1);
		
		return n;*/
		
		return this.validWord(wordList,last);
	}
	
	public WordList getValidWords(WordList wordList, int i) throws IOException {
		
		char[] constrs;
		constrs = new char[wordList.wordList.get(i).length];
		String constraints;
		
		/*Obtenemos las constraints:*/
		/*Leemos las posiciones del GRID que ocupa la palabra:*/
		/*Si es HORIZONTAL:*/
		if(wordList.wordList.get(i).pos == 0) {
			
			int row = wordList.wordList.get(i).row;
			int ini = wordList.wordList.get(i).col;
			int fin = ini+wordList.wordList.get(i).length;
			
			//System.out.printf("horizontal, columnas %d:%d\n", ini, fin-1);
			
			for(int k=ini; k<fin; k++) {
				if(this.gridv[row][k] != "0" && this.gridv[row][k] != "1") {
					constrs[k-ini]=this.gridv[row][k].charAt(0);
					//System.out.printf("constr(%d,%d): %s\n", row, k, this.gridv[row][k]);
				}else {
					constrs[k-ini]='_';
				}
			}
		
		}
		/*Si es VERTICAL:*/
		else {
		
			int col = wordList.wordList.get(i).col;
			int ini = wordList.wordList.get(i).row;
			int fin = ini+wordList.wordList.get(i).length;
			
			//System.out.printf("vertical, columnas %d:%d\n", ini, fin-1);
		
			for(int k=ini; k<fin; k++) {
				if(this.gridv[k][col] != "0" && this.gridv[k][col] != "1") {
					constrs[k-ini]=this.gridv[k][col].charAt(0);
					//System.out.printf("constr(%d,%d): %s\n", k, col, this.gridv[k][col]);
				}else {
					constrs[k-ini]='_';
				}
			}
		
		}
		
		/*Obtenemos las palabras que cumplen las constraints:*/
		WordBBDD wordddbb = new WordBBDD();
	
		constraints = String.valueOf(constrs);
		//System.out.printf("constrains para pos_words: '%s'\n", constraints);
	
		//System.out.printf("palabras no permitidas para %d:\n", i);
		WordList words = wordddbb.getWords(constraints, this.forbWords.get(i));
		//System.out.printf("palabras encontradas: %d\n", words.nwords);
		
		return words;
		
	}
	
	public int nextWord_v2(WordList wordList, WordList posWords, int i, int p) throws IOException {
		
		int error=0;
		int found=0;
			
		wordList.wordList.get(i).found=1;
		
		String nword = Normalizer.normalize(posWords.wordList.get(p).value, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
				
		wordList.wordList.get(i).setValue(nword);
				
		//System.out.printf("palabra prueba: %s \n", wordList.wordList.get(i).value);
		
		/*System.out.printf("Palabra prueba: %s\n", nword);
		System.out.printf("Palabra: %s\n", wordList.wordList.get(i).getValue());
		System.out.printf("Posicion: (%d, %d)\n", wordList.wordList.get(i).row, wordList.wordList.get(i).col);
		System.out.printf("Longitud: %d\n", wordList.wordList.get(i).length);*/
				
		/*ACTUALIZAMOS EL GRID:*/
		/*Si es HORIZONTAL:*/
		if(wordList.wordList.get(i).pos == 0) {
					
			int row = wordList.wordList.get(i).row;
			int ini = wordList.wordList.get(i).col;
			int fin = ini+wordList.wordList.get(i).length;
					
			for(int k=ini; k<fin; k++) {
				this.gridv[row][k] = String.
						valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
			}
				
		}
		/*Si es VERTICAL:*/
		else {
		
			int col = wordList.wordList.get(i).col;
			int ini = wordList.wordList.get(i).row;
			int fin = ini+wordList.wordList.get(i).length;
			
			for(int k=ini; k<fin; k++) {
				this.gridv[k][col] = String.
						valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
			}
		}
				
		found = this.testGrid(wordList);
			
		if(found!=0) {
			wordList.wordList.get(i).found=0;
			error=1;
		}
		
		return error;
		
	}
	
	
	
	public int nextWord(WordList wordList, int i) throws IOException {
		
		int error=0;
		
		char[] constrs;
		constrs = new char[wordList.wordList.get(i).length];
		String constraints;
		
		/*Obtenemos las constraints:*/
		/*Leemos las posiciones del GRID que ocupa la palabra:*/
		/*Si es HORIZONTAL:*/
		if(wordList.wordList.get(i).pos == 0) {
			
			int row = wordList.wordList.get(i).row;
			int ini = wordList.wordList.get(i).col;
			int fin = ini+wordList.wordList.get(i).length;
			
			//System.out.printf("horizontal, columnas %d:%d\n", ini, fin-1);
			
			for(int k=ini; k<fin; k++) {
				if(this.gridv[row][k] != "0" && this.gridv[row][k] != "1") {
					constrs[k-ini]=this.gridv[row][k].charAt(0);
					//System.out.printf("constr(%d,%d): %s\n", row, k, this.gridv[row][k]);
				}else {
					constrs[k-ini]='_';
				}
			}
		
		}
		/*Si es VERTICAL:*/
		else {
		
			int col = wordList.wordList.get(i).col;
			int ini = wordList.wordList.get(i).row;
			int fin = ini+wordList.wordList.get(i).length;
			
			//System.out.printf("vertical, columnas %d:%d\n", ini, fin-1);
		
			for(int k=ini; k<fin; k++) {
				if(this.gridv[k][col] != "0" && this.gridv[k][col] != "1") {
					constrs[k-ini]=this.gridv[k][col].charAt(0);
					//System.out.printf("constr(%d,%d): %s\n", k, col, this.gridv[k][col]);
				}else {
					constrs[k-ini]='_';
				}
			}
		
		}
		
		/*Obtenemos las palabras que cumplen las constraints:*/
		WordBBDD wordddbb = new WordBBDD();
	
		constraints = String.valueOf(constrs);
		//System.out.printf("constrains: '%s'\n", constraints);
	
		WordList words = wordddbb.getWords(constraints, this.forbWords.get(i));
		//System.out.printf("palabras encontradas: %d\n", words.nwords);
		
		if(words.nwords > 0) {
			
			wordList.wordList.get(i).found=1;
			
			int found=1;
			int count=0;
			
			for(int w=0; w<words.nwords; w++) {
				
				String nword = Normalizer.normalize(words.wordList.get(w).value, Normalizer.Form.NFD)
						.replaceAll("[^\\p{ASCII}]", "");
				
				wordList.wordList.get(i).setValue(nword);
				
				//System.out.printf("palabra prueba: %s \n", wordList.wordList.get(i).value);
				
				/*ACTUALIZAMOS EL GRID:*/
				/*Si es HORIZONTAL:*/
				if(wordList.wordList.get(i).pos == 0) {
					
					int row = wordList.wordList.get(i).row;
					int ini = wordList.wordList.get(i).col;
					int fin = ini+wordList.wordList.get(i).length;
					
					for(int k=ini; k<fin; k++) {
						this.gridv[row][k] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
				
				}
				/*Si es VERTICAL:*/
				else {
				
					int col = wordList.wordList.get(i).col;
					int ini = wordList.wordList.get(i).row;
					int fin = ini+wordList.wordList.get(i).length;
				
					for(int k=ini; k<fin; k++) {
						this.gridv[k][col] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
				}
				
				found = this.testGrid(wordList);
				
				if(found == 0)
					break;
			}
			
			/*do {
					
				int w = ThreadLocalRandom.current().nextInt(0, words.nwords);
				
				String nword = Normalizer.normalize(words.wordList.get(w).value, Normalizer.Form.NFD)
						.replaceAll("[^\\p{ASCII}]", "");
				
				wordList.wordList.get(i).setValue(nword);
				
				System.out.printf("palabra prueba: %s \n", wordList.wordList.get(i).value);
			
				//ACTUALIZAMOS EL GRID:
				//Si es HORIZONTAL:
				if(wordList.wordList.get(i).pos == 0) {
					
					int row = wordList.wordList.get(i).row;
					int ini = wordList.wordList.get(i).col;
					int fin = ini+wordList.wordList.get(i).length;
					
					for(int k=ini; k<fin; k++) {
						this.gridv[row][k] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
				
				}
				//Si es VERTICAL:
				else {
				
					int col = wordList.wordList.get(i).col;
					int ini = wordList.wordList.get(i).row;
					int fin = ini+wordList.wordList.get(i).length;
				
					for(int k=ini; k<fin; k++) {
						this.gridv[k][col] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
				}
				
				found = this.testGrid(wordList);
				count++;
				
				//System.out.printf("count = %d\n",count);
				
			}while(found!=0 && count < words.nwords);*/
			
			if(found!=0) {
				wordList.wordList.get(i).found=0;
				error=1;
			}
			
		}else {
			//System.out.println("No se ha encontrado ninguna palabra que cumpla las condiciones.");
			wordList.wordList.get(i).found=0;
			error=1;
			//break;
		}
		
		return error;
		
	}
	
	public void updateGrid(WordList wordList) {
		
		/*Reseteamos el GRID:*/
		/*Establecemos todos los valores a 1:*/
		for(int i=0; i<this.nrows; i++) {
			for(int j=0; j<this.ncols; j++) {
				if(this.gridv[i][j] != "0")
					this.gridv[i][j]="1";
			}
		}
		
		/*Añadimos las palabras encontradas:*/
		for(int i=0; i<this.nwords; i++) {
			if(wordList.wordList.get(i).found==1) {
				/*Si es HORIZONTAL:*/
				if(wordList.wordList.get(i).pos == 0) {
		
					/*System.out.printf("Palabra: %s\n", wordList.wordList.get(i).getValue());
					System.out.printf("Posicion: (%d, %d)\n", wordList.wordList.get(i).row, wordList.wordList.get(i).col);
					System.out.printf("Longitud: %d\n", wordList.wordList.get(i).length);*/
					
					int row = wordList.wordList.get(i).row;
					int ini = wordList.wordList.get(i).col;
					int fin = ini+wordList.wordList.get(i).length;
			
					for(int k=ini; k<fin; k++) {
						this.gridv[row][k] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
		
				}
				/*Si es VERTICAL:*/
				else {
		
					int col = wordList.wordList.get(i).col;
					int ini = wordList.wordList.get(i).row;
					int fin = ini+wordList.wordList.get(i).length;
		
					for(int k=ini; k<fin; k++) {
						this.gridv[k][col] = String.
								valueOf(wordList.wordList.get(i).getValue().charAt(k-ini));
					}
				}
			}
		}
		
	}
	
	public int testGrid(WordList wordList) {
		
		/*Vamos palabra a palabra, de las que tengan alguna posicion rellena*/
		/*En caso de que alguna no devuelva resultados de la BBDD, se devuelve error*/
		
		WordList words = new WordList();
		
		words = this.getWords(wordList);
		
		for(int i=0; i<words.nwords; i++) {
			String constr = words.wordList.get(i).value;
			//System.out.printf("word: %s\n",constr);
			
			/*Obtenemos las palabras que cumplen las constraints:*/
			WordBBDD wordddbb = new WordBBDD();
			WordList gwords = wordddbb.getWords(constr, this.forbWords.get(i));
			
			if(gwords.nwords==0)
				return 1;
		}
		
		return 0;
	}
	
	public WordList getWords(WordList wordList) {
		
		WordList words = new WordList();
		
		for(int i=0; i<wordList.nwords; i++) {
			if(wordList.wordList.get(i).found == 0) {
				
				char[] constrs;
				constrs = new char[wordList.wordList.get(i).length];
				
				/*Obtenemos las constraints:*/
				/*Leemos las posiciones del GRID que ocupa la palabra:*/
				/*Si es HORIZONTAL:*/
				if(wordList.wordList.get(i).pos == 0) {
					
					int row = wordList.wordList.get(i).row;
					int ini = wordList.wordList.get(i).col;
					int fin = ini+wordList.wordList.get(i).length;
					
					for(int k=ini; k<fin; k++) {
						if(this.gridv[row][k] != "0" && this.gridv[row][k] != "1") {
							constrs[k-ini]=this.gridv[row][k].charAt(0);
						}else {
							constrs[k-ini]='_';
						}
					}
				
				}
				/*Si es VERTICAL:*/
				else {
				
					int col = wordList.wordList.get(i).col;
					int ini = wordList.wordList.get(i).row;
					int fin = ini+wordList.wordList.get(i).length;
				
					for(int k=ini; k<fin; k++) {
						if(this.gridv[k][col] != "0" && this.gridv[k][col] != "1") {
							constrs[k-ini]=this.gridv[k][col].charAt(0);
						}else {
							constrs[k-ini]='_';
						}
					}
				
				}
				
				words.wordList.add(new Word(0,0,constrs.length, 0, String.valueOf(constrs)));
				
			}
		}
		
		words.nwords=words.wordList.size();
		return words;
		
	}
	
	public int validWord(WordList wordList, int last) {
	
		int found = 0;
		int cross = 0;
		
		/*Comprobamos si es la primera palabra que se va a formar:*/
		if(last==-1)
			return 0;
		
		/*for(int i=0; i<wordList.nwords; i++) {
			if(wordList.wordList.get(i).found==1)
				found=1;
		}
		
		if(found == 0)
			return 0;*/
		
		/*Comprobamos que la palabra cruza a la palabra anteriormente formada:*/
		
		/*Formamos un grid con solo la anterior palabra:*/
		Grid gridaux = new Grid(this.nrows, this.ncols);
		for(int i=0; i<gridaux.nrows; i++) {
			for(int j=0; j<gridaux.ncols; j++) {
				gridaux.gridv[i][j]="1";
			}
		}
		/*Añadimos la ultima palabra:*/
		int xxi = wordList.wordList.get(last).row;
		int yyi = wordList.wordList.get(last).col;
		int lengthaux = wordList.wordList.get(last).length;
		int colaux = wordList.wordList.get(last).col;
		int rowaux = wordList.wordList.get(last).row;
		/*Si la palabra es HORIZONTAL*/
		if(wordList.wordList.get(last).pos == 0) {
			
			for(int i=yyi; i<yyi+lengthaux; i++) {
				gridaux.gridv[rowaux][i] = 
						String.valueOf(wordList.wordList.get(last).getValue().charAt(i-yyi));
			}
			
		}
		/*Si la palabra es VERTICAL*/
		else if(wordList.wordList.get(last).pos == 1) {
			
			for(int i=xxi; i<xxi+lengthaux; i++) {
				gridaux.gridv[i][colaux] = 
						String.valueOf(wordList.wordList.get(last).getValue().charAt(i-xxi));
			}
			
		}
		
		/*Identificamos las palabras sin establecer que cruzan a la ultima formada:*/
		List<Integer> cwords = new ArrayList<Integer>();
		List<Integer> ncwords = new ArrayList<Integer>();
		
		for(int i=0; i<this.nwords; i++) {
			if(wordList.wordList.get(i).found==0) {
				cross=0;
				/*Si la palabra es horizontal:*/
				if(wordList.wordList.get(i).pos==0) {
					int row = wordList.wordList.get(i).row;
					int yi = wordList.wordList.get(i).col;
					int length = wordList.wordList.get(i).length;
					
					for(int j=yi; j<yi+length; j++) {
						if(gridaux.gridv[row][j]!="1") {
							cross=1;
							cwords.add(i);
							break;
						}
					}
					
					if(cross==0)
						ncwords.add(i);
					
				}
				
				/*Si la palabra es vertical:*/
				else if(wordList.wordList.get(i).pos==1) {
					int col = wordList.wordList.get(i).col;
					int xi = wordList.wordList.get(i).row;
					int length = wordList.wordList.get(i).length;
					
					for(int j=xi; j<xi+length; j++) {
						if(gridaux.gridv[j][col]!="1") {
							cross=1;
							cwords.add(i);
							break;
						}
					}
					
					if(cross==0)
						ncwords.add(i);
					
				}
			}
		}
		
		/*Obtenemos una palabra aleatoria de las que cruzan:*/
		if(cwords.size() > 0) {
			//int index = ThreadLocalRandom.current().nextInt(0, cwords.size());
			return cwords.get(0).intValue();
		}
		else {
			int index = ThreadLocalRandom.current().nextInt(0, ncwords.size());
			return ncwords.get(index).intValue();
		}
		
		/*
		//Comprobamos si la nueva palabra cruza a la anterior:
		int xi = wordList.wordList.get(n).row;
		int yi = wordList.wordList.get(n).col;
		int length = wordList.wordList.get(n).length;
		int col = wordList.wordList.get(n).col;
		int row = wordList.wordList.get(n).row;
		
		//Si la palabra es HORIZONTAL
		if(wordList.wordList.get(n).pos == 0) {
			for(int i=yi; i<yi+length; i++) {
				if(gridaux.gridv[row][i]!="1")
					return 0;
			}
			
		}
		//Si la palabra es VERTICAL
		else if(wordList.wordList.get(n).pos == 1) {
			for(int i=xi; i<xi+length; i++) {
				if(gridaux.gridv[i][col]!="1")
					return 0;
			}
			
		}
		
		
		return 1;
		
		*/
	}
	
	public List<Integer> testWords(WordList wordList) {
		
		int size;
		int count=0;
		List<Integer> wordIdx = new ArrayList<Integer>();
		
		char[] constrs;
		
		/*Comprobamos que cada palabra que está marcada como
		 * no encontrada no se ha completado indirectamente:*/
		
		for(int i=0; i<wordList.nwords; i++) {
			if(wordList.wordList.get(i).found==0) {
				
				size=0;
				
				constrs = new char[wordList.wordList.get(i).length];
				
				int xi = wordList.wordList.get(i).row;
				int yi = wordList.wordList.get(i).col;
				int length = wordList.wordList.get(i).length;
				int col = wordList.wordList.get(i).col;
				int row = wordList.wordList.get(i).row;
				
				/*Obtenemos las posiciones que ocupa la palabra:*/
				/*Si la palabra es HORIZONTAL*/
				if(wordList.wordList.get(i).pos == 0) {
					
					for(int j=yi; j<yi+length; j++) {
						if(this.gridv[row][j]!="0" && this.gridv[row][j]!="1") {
							size++;
							constrs[j-yi]=this.gridv[row][j].charAt(0);
						}
					}
					
				}
				/*Si la palabra es VERTICAL*/
				else if(wordList.wordList.get(i).pos == 1) {
					
					for(int j=xi; j<xi+length; j++) {
						if(this.gridv[j][col]!="0" && this.gridv[j][col]!="1") {
							size++;
							constrs[j-xi]=this.gridv[j][col].charAt(0);
						}
					}
					
				}
				
				//System.out.printf("Palabra formada: %s[%d]\n", String.valueOf(constrs), size);
				//System.out.printf("Tamaño de palabra: %d\n", wordList.wordList.get(i).length);
				
				if(size == wordList.wordList.get(i).length) {
					wordList.wordList.get(i).value = String.valueOf(constrs);
					wordList.wordList.get(i).found = 1;
					count++;
					wordIdx.add(i);
				}
				
			}
		}
		
		return wordIdx;
		
	}
	
	
}
