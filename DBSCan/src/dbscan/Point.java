package dbscan;

public  class Point{               //定义一个Point类，表示数据点的坐标及相关属性
	private double x ;
	private double y ;
	private boolean isVisited ;    //定义点是否被访问过
		
	
	public Point() {
		super();
	}

	public Point(boolean isVisited) {
		super();
		this.isVisited = isVisited;
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y,boolean isVisited) {
		super();
		this.x = x;
		this.y = y;
		this.isVisited = isVisited;
	}
	
	public boolean getIsVisited() {
		return isVisited;
	}
	
	public void setIsVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		return "("+x+","+y+")";
	} 	

}
