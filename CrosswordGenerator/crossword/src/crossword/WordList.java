package crossword;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WordList {

	public List<Word> wordList = new ArrayList<Word>();
	public int nwords;
	public int last;
	
	public void getWords(Grid grid) {
		
		int col;
		int row;
		int lwords=0;
		
		/*Añadimos palabras horizontales:*/
		for(int i=0; i<grid.nrows; i++) {
			col = 0;
			
			/*for(int j=0; j<grid.ncols; j++) {
				if(grid.gridv[i][j]=="0" || j==grid.ncols-1) {
					if(j>0) {
						if(j>col && j<grid.ncols-1) {
							this.wordList.add(new Word(i, col, j-col, 0, ""));
							lwords++;
						}else if(j>col && j==grid.ncols-1) {
							if(grid.gridv[i][j]=="0") {
								this.wordList.add(new Word(i, col, j-col, 0, ""));
								lwords++;
							}else {
								this.wordList.add(new Word(i, col, j-col+1, 0, ""));
								lwords++;
							}
						}else if(j==col && j==grid.ncols-1 && grid.gridv[i][j]!="0") {
							this.wordList.add(new Word(i, col, 1, 0, ""));
							lwords++;
						}
					}
					col=j+1;
				}
			}*/
			
			for(int j=0; j<grid.ncols; j++) {
				if(grid.gridv[i][j]=="0" && j>0 && grid.gridv[i][j-1]!="0") {
					this.wordList.add(new Word(i, col, (j-1-col)+1, 0, ""));
					lwords++;
					col=j+1;
				}
				else if(grid.gridv[i][j]=="0" && j>0 && grid.gridv[i][j-1]=="0") {
					col=j+1;
				}
				else if(grid.gridv[i][j]=="0" && j==0) {
					col=j+1;
				}
				else if(grid.gridv[i][j]!="0" && j==grid.ncols-1) {
					this.wordList.add(new Word(i, col, (j-col)+1, 0, ""));
					lwords++;
					col=j+1;
				}
			}
		}
		
		//System.out.printf("list words until rows: %d \n", lwords);
		
		/*Añadimos palabras verticales:*/
		for(int i=0; i<grid.ncols; i++) {
			row = 0;
			
			/*for(int j=0; j<grid.nrows; j++) {
				if(grid.gridv[j][i]=="0" || j==grid.nrows-1) {
					if(j>0) {
						if(j>row && j<grid.nrows-1) {
							this.wordList.add(new Word(row, i, j-row, 1, ""));
							lwords++;
						}else if(j>row && j==grid.nrows-1) {
							if(grid.gridv[j][i]=="0") {
								this.wordList.add(new Word(row, i, j-row, 1, ""));
								lwords++;
							}else {
								this.wordList.add(new Word(row, i, j-row+1, 1, ""));
								lwords++;
							}
						}else if(j==row && j==grid.nrows-1 && grid.gridv[j][i]!="0") {
							this.wordList.add(new Word(row, i, 1, 1, ""));
							lwords++;
						}
					}
					row=j+1;
				}
			}*/
			
			for(int j=0; j<grid.nrows; j++) {
				if(grid.gridv[j][i]=="0" && j>0 && grid.gridv[j-1][i]!="0") {
					this.wordList.add(new Word(row, i, (j-1-row)+1, 1, ""));
					lwords++;
					row=j+1;
				}
				else if(grid.gridv[j][i]=="0" && j>0 && grid.gridv[j-1][i]=="0") {
					row=j+1;
				}
				else if(grid.gridv[j][i]=="0" && j==0) {
					row=j+1;
				}
				else if(grid.gridv[j][i]!="0" && j==grid.nrows-1) {
					this.wordList.add(new Word(row, i, (j-row)+1, 1, ""));
					lwords++;
					row=j+1;
				}
			}
			
		}
		
		//System.out.printf("list words until columns: %d \n", lwords);
		
		this.nwords = grid.nwords;
		
	}
	
	public void printList() throws IOException{
		FileWriter fileWriter = new FileWriter("wordList.out");
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		printWriter.print("HORIZONTALES:\n");
		/*Escribimos palabras horizontales:*/
		for(int i=0; i<this.nwords; i++) {
			if(this.wordList.get(i).pos == 0) {
				printWriter.printf("Pos(%d,%d), Longitud %d\n",this.wordList.get(i).row
						,this.wordList.get(i).col,this.wordList.get(i).length);
			}
		}
		
		printWriter.print("Verticales:\n");
		/*Escribimos palabras verticales:*/
		for(int i=0; i<this.nwords; i++) {
			if(this.wordList.get(i).pos == 1) {
				printWriter.printf("Pos(%d,%d), Longitud %d\n",this.wordList.get(i).row
						,this.wordList.get(i).col,this.wordList.get(i).length);
			}
		}
		
		printWriter.close();
		
	}
	
	
}
