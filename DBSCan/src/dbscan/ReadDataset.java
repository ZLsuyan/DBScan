package dbscan;
/**
 * @author 曾丽  
 * @date 2015/12/15
 */
import java.io.*;
import dbscan.Point;

public class ReadDataset {
	// 定义文件路径
	String filepath = "C:/Users/Zengli/Desktop/数据挖掘-实验数据集/raw_dataset/dataset5.txt" ;
	
	
	/**
	 * 读数据集文件中共有多少行，即共有多少个数据点
	 * @return
	 * @throws IOException
	 */
	public int getTextLines() throws IOException {
		//定义一个字符流的输入流的节点流，用于读取文件（一个字符一个字符的读取）
		FileReader fr = new FileReader(filepath); 
		//在定义好的流基础上套接一个处理流，用于更加效率的读取文件（一行一行的读取）
		BufferedReader br = new BufferedReader(fr); 
		//统计行数，从0开始
		int lines = 0;
		//readLine()方法是按行读的，返回值是这行的内容
		while(br.readLine() != null) { 
			lines++;
		}
		return lines; 
	}
	
	/**
	 * 读取到数据集中的数据点，将数据点保存到数组中并返回
	 * @return
	 * @throws IOException
	 */
	public Point[] getDataset() throws IOException{
		//获取数据集的点的个数，即文本行数
		int MAXNUM = getTextLines();
		//定义一个Point类型的数组，用于保存数据点的坐标
		Point[] my_point = new Point[MAXNUM];
		FileReader fr = new FileReader(filepath); 
		BufferedReader br = new BufferedReader(fr); 
		String eachLine = null;
		for(int i=0;i<MAXNUM;i++){
			/*
			 * 读每一行的元素，并根据空格区分元素
			 * 将读取到的每一行的第一个元素作为x坐标，第二个元素作为y坐标
			 */
			eachLine = br.readLine();
			String datas[] = eachLine.split(" ");
			//创建一个新的Point对象
	    	Point point = new Point();                                 
	    	point.setX(Double.valueOf(datas[0])) ;
	    	point.setY(Double.valueOf(datas[1])) ;
	    			
	    	//将对象放到数组中
	    	my_point[i] = point;									   
	    }
		return my_point;
	}
	
	/**
	 * 将运行出的簇读入.txt文件
	 * @param file
	 * @param appendance
	 */
	public static void append(File file, String appendance) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(file, true);
            fw.write(appendance);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	 
	
	/**
	 * 主函数
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		ReadDataset rds = new ReadDataset();
//		int MAXNUM = rds.getTextLines();
//		Point[] my_point = new Point[MAXNUM];
//		my_point = rds.getDataset();
//		for(int i=0;i<MAXNUM;i++){
//			System.out.println(my_point[i].getX()+","+my_point[i].getY());
//		}
		
		int lines = 0;
		ReadDataset rds = new ReadDataset();
		lines = rds.getTextLines();
		System.out.println(lines);
	}
}
