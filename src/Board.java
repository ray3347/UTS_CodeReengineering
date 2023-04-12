
public class Board {
	private int width;
    private int height;
    private int pixel;
    
    public Board(int width, int height, int pixel) {
    	this.width=width;
    	this.height=height;
    	this.pixel=pixel;
    }

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPixel() {
		return pixel;
	}

	public void setPixel(int pixel) {
		this.pixel = pixel;
	}
    
}
