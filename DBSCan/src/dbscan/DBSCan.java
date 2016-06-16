package dbscan;
/** 
 * @author 曾丽
 * @date 2015/12/18
 */
import dbscan.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import dbscan.ReadDataset;

public class DBSCan {
	

	/**
	 * DBSCan聚类
	 * @param MAXNUM     数据点总数
	 * @param Density    点的指定半径内的点的个数
	 * @param Eps        指定半径
	 * @param MinPts     密度阈值
	 * @param isVisited  标记点是否被访问过
	 * @return 
	 */
	
	// 定义Eps和MinPts	 
	public static final double Eps = 2.4 ;
	public static final int MinPts = 20 ;
	
	
	/**
	 * 计算两点之间的距离
	 * @param point1
	 * @param point2
	 * @return distance
	 */
	public static double distance(Point point1,Point point2){
		return Math.sqrt((point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY()));
	}
	
	
	
	/**
	 * 计算两个簇之间的最小距离，即两个核心点构成的簇之间的核心点之间的最小距离
	 * 用来判断这两个核心点所在的簇是否能够合并
	 * @param cluster1
	 * @param cluster2
	 * @return distance
	 */
	public static double distance(ArrayList<Point> cluster1,ArrayList<Point> cluster2){
		double MinDis = 1E12;
		for(int i=0;i<cluster1.size();i++){
			for(int j=0;j<cluster2.size();j++){
				if(distance(cluster1.get(i),cluster2.get(j))<MinDis){
					MinDis = distance(cluster1.get(i),cluster2.get(j));
				}
			}
		}
		return MinDis;
	}
	
	
	
	/**
	 * 计算一个点离哪一个簇最近
	 * 用于分配边界点
	 * @param point
	 * @param clusters
	 * @return
	 */
	public static int whichCluster(Point point,ArrayList<ArrayList<Point>> clusters){
		//到哪一个簇最近，返回下标
		int whichCluster = -1;
		//存储一个点到每一个簇的最小距离
		double[] MinDis = new double[clusters.size()];
		for(int i=0;i<clusters.size();i++){
			MinDis[i] = 1.0E12;
			for(int j=0;j<clusters.get(i).size();j++){
				if(distance(point,clusters.get(i).get(j))<MinDis[i]){
					MinDis[i] = distance(point,clusters.get(i).get(j));
				}
			}
		}
		//初始化最小距离为该点到第0簇的最小距离
		double MinNum = MinDis[0];
		for(int i=0;i<clusters.size();i++){
			if(MinDis[i]<=MinNum){
				MinNum = MinDis[i];
				whichCluster = i;
			}
		}
		//返回下标
		return whichCluster;
	}
	
	
	/**
	 * 对所有的数据点进行分类：核心点，边界点，噪音点
	 * @param my_point
	 * @param Eps
	 * @param MinPts
	 * @param Core
	 * @param Border
	 * @param Noise
	 */
	public static void classify(Point[] my_point,double Eps,int MinPts,ArrayList<Point> Core,ArrayList<Point> Border,ArrayList<Point> Noise){
		int density = 0;
		//第一遍标记出所有的核心点，并将它们设置为已被访问过
		for(int i=0;i<my_point.length;i++){
			for(int j=0;j<my_point.length;j++){
				if(distance(my_point[i],my_point[j])<Eps){
					density++;
				}
			}
			if(density>=MinPts){
				Core.add(my_point[i]);
				my_point[i].setIsVisited(true);
			}
			density = 0;
		}
		
		//第二遍在所有还没访问过的点中标记所有的边界点，并将边界点设置为已访问过
		for(int i=0;i<my_point.length;i++){
			if(!(my_point[i].getIsVisited())){
				for(int j=0;j<Core.size();j++){
					if(distance(my_point[i],Core.get(j))<=Eps){
						Border.add(my_point[i]);
						my_point[i].setIsVisited(true);
						break;
					}
				}
			}
		}
		
		//第三遍标记所有的噪音点，即剩下的所有还没访问的点即为噪音点
		for(int i=0;i<my_point.length;i++){
			if(!(my_point[i].getIsVisited())){				
				Noise.add(my_point[i]);
				my_point[i].setIsVisited(true);
			}
		}
	}
	
	
	
	
	/**
	 * 主函数
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//从数据集中获取数据点的个数
		ReadDataset rds = new ReadDataset();
		int MAXNUM = rds.getTextLines();
		
		//创建一个Point对象，保存读取到的数据集中的数据点
		Point[] my_point = new Point[MAXNUM]; 
		my_point = rds.getDataset();
	
		//初始将所有的点都标记为未访问过
		for(int i=0;i<my_point.length;i++){
			my_point[i].setIsVisited(false);		
		}
		
		//定义核心点，边界点，噪音点集合
		ArrayList<Point> Core = new ArrayList<Point>();
		ArrayList<Point> Border = new ArrayList<Point>();
		ArrayList<Point> Noise = new ArrayList<Point>();
		ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>() ; 

		//对每个点进行分类
		classify(my_point,Eps,MinPts,Core,Border,Noise);
		
		
		//初始将每个核心点作为一个簇
		for(int i=0;i<Core.size();i++){
			ArrayList<Point> list = new ArrayList<Point>();
			clusters.add(list);
			clusters.get(i).add(Core.get(i));
		}
		
		//若两个簇之间的最小距离小于Eps，则合并这两个簇
		for(int i=0;i<clusters.size();i++){
			for(int j=i+1;j<clusters.size();j++){
				if(distance(clusters.get(i),clusters.get(j))<Eps){
					clusters.get(i).addAll(clusters.get(j));
					clusters.remove(j);
					j-=1;
				}
			}
		}
		
		
		//对边界点进行处理，将它分派到所属的核心点的簇中去
		for(int i=0;i<Border.size();i++){
			clusters.get(whichCluster(Border.get(i),clusters)).add(Border.get(i));
		}
		
		
		
		//输出结果*********************************
		System.out.println("输出结果：");
		System.out.println("共有"+my_point.length+"个点");
		System.out.println("共有"+Core.size()+"个核心点");
		System.out.println("共有"+Border.size()+"个边界点");
		System.out.println("共有"+Noise.size()+"个噪音点");
		System.out.println("共有"+clusters.size()+"个簇");
		for(int i=0;i<clusters.size();i++){
			System.out.println("第"+(i+1)+"个簇中有"+clusters.get(i).size()+"个数据点，它们为：");
			//对于每一个簇，每8个数据点换行输出，点与点之间用空格隔开
    		for(int j =1;j<clusters.get(i).size()+1;j++){
    			if(j%8==0){
    				System.out.println(clusters.get(i).get(j-1).toString());
    			}else{
    				System.out.print(clusters.get(i).get(j-1).toString()+"  ");
    			}
    		}
            System.out.println();
		}
		System.out.println("噪音点为：");
		for(int i=1;i<(Noise.size()+1);i++){
			//每8个数据点换行输出，点与点之间用空格隔开
    		if(i%8==0){
    			System.out.println(Noise.get(i-1).toString());
    		}else{
    			System.out.print(Noise.get(i-1).toString()+"  ");
    		}
            System.out.println();
		}
		
		
		
		//1.将聚类后的点，根据划分的簇不同进行输出到文件中*******************
        for(int i=1;i<(clusters.size()+1);i++){
        	//获取每一个簇中的数据点的个数
        	int pointNum = clusters.get(i-1).size();
        	/*
        	 * 指定分别保存每一个簇的X、Y坐标的文件路径和文件名
        	 * ”cu”+i+”x.txt”表示簇i的x坐标文件
        	 * ”cu”+i+”x.txt”表示簇i的y坐标文件
        	 */
        	File xfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test3/cu"+i+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test3/cu"+i+"y.txt");
        	for(int j=0;j<pointNum;j++){
        		//将每一个簇中的所有数据点的x和y坐标数据分别写入指定文件中
        		ReadDataset.append(xfile, clusters.get(i-1).get(j).getX()+" ");
        		ReadDataset.append(yfile, clusters.get(i-1).get(j).getY()+" ");
            }
        }
        //2.将噪音点也输出到文件中*******************************
        for(int i=0;i<Noise.size();i++){
        	File xfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test3/cu"+(clusters.size()+1)+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test3/cu"+(clusters.size()+1)+"y.txt");
        	//噪音集合中的所有数据点的x和y坐标数据分别写入指定文件中
        	ReadDataset.append(xfile, Noise.get(i).getX()+" ");
    		ReadDataset.append(yfile, Noise.get(i).getY()+" ");
        }
	}
}
