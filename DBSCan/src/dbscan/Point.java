package dbscan;

public  class Point{               //����һ��Point�࣬��ʾ���ݵ�����꼰�������
	private double x ;
	private double y ;
	private boolean isVisited ;    //������Ƿ񱻷��ʹ�
		
	
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
