package crossword;

public class Word {
	
	public int row;
	public int col;
	public int length;
	public int pos;
	public String value;
	public int found;
	
	public Word(int x, int y, int length, int p, String value) {
		this.row=x;
		this.col=y;
		this.pos=p;
		this.length = length;
		this.value = value;
		this.found=0;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
