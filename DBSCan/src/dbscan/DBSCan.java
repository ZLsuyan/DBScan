package dbscan;
/** 
 * @author ����
 * @date 2015/12/18
 */
import dbscan.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import dbscan.ReadDataset;

public class DBSCan {
	

	/**
	 * DBSCan����
	 * @param MAXNUM     ���ݵ�����
	 * @param Density    ���ָ���뾶�ڵĵ�ĸ���
	 * @param Eps        ָ���뾶
	 * @param MinPts     �ܶ���ֵ
	 * @param isVisited  ��ǵ��Ƿ񱻷��ʹ�
	 * @return 
	 */
	
	// ����Eps��MinPts	 
	public static final double Eps = 2.4 ;
	public static final int MinPts = 20 ;
	
	
	/**
	 * ��������֮��ľ���
	 * @param point1
	 * @param point2
	 * @return distance
	 */
	public static double distance(Point point1,Point point2){
		return Math.sqrt((point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY()));
	}
	
	
	
	/**
	 * ����������֮�����С���룬���������ĵ㹹�ɵĴ�֮��ĺ��ĵ�֮�����С����
	 * �����ж����������ĵ����ڵĴ��Ƿ��ܹ��ϲ�
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
	 * ����һ��������һ�������
	 * ���ڷ���߽��
	 * @param point
	 * @param clusters
	 * @return
	 */
	public static int whichCluster(Point point,ArrayList<ArrayList<Point>> clusters){
		//����һ��������������±�
		int whichCluster = -1;
		//�洢һ���㵽ÿһ���ص���С����
		double[] MinDis = new double[clusters.size()];
		for(int i=0;i<clusters.size();i++){
			MinDis[i] = 1.0E12;
			for(int j=0;j<clusters.get(i).size();j++){
				if(distance(point,clusters.get(i).get(j))<MinDis[i]){
					MinDis[i] = distance(point,clusters.get(i).get(j));
				}
			}
		}
		//��ʼ����С����Ϊ�õ㵽��0�ص���С����
		double MinNum = MinDis[0];
		for(int i=0;i<clusters.size();i++){
			if(MinDis[i]<=MinNum){
				MinNum = MinDis[i];
				whichCluster = i;
			}
		}
		//�����±�
		return whichCluster;
	}
	
	
	/**
	 * �����е����ݵ���з��ࣺ���ĵ㣬�߽�㣬������
	 * @param my_point
	 * @param Eps
	 * @param MinPts
	 * @param Core
	 * @param Border
	 * @param Noise
	 */
	public static void classify(Point[] my_point,double Eps,int MinPts,ArrayList<Point> Core,ArrayList<Point> Border,ArrayList<Point> Noise){
		int density = 0;
		//��һ���ǳ����еĺ��ĵ㣬������������Ϊ�ѱ����ʹ�
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
		
		//�ڶ��������л�û���ʹ��ĵ��б�����еı߽�㣬�����߽������Ϊ�ѷ��ʹ�
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
		
		//�����������е������㣬��ʣ�µ����л�û���ʵĵ㼴Ϊ������
		for(int i=0;i<my_point.length;i++){
			if(!(my_point[i].getIsVisited())){				
				Noise.add(my_point[i]);
				my_point[i].setIsVisited(true);
			}
		}
	}
	
	
	
	
	/**
	 * ������
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//�����ݼ��л�ȡ���ݵ�ĸ���
		ReadDataset rds = new ReadDataset();
		int MAXNUM = rds.getTextLines();
		
		//����һ��Point���󣬱����ȡ�������ݼ��е����ݵ�
		Point[] my_point = new Point[MAXNUM]; 
		my_point = rds.getDataset();
	
		//��ʼ�����еĵ㶼���Ϊδ���ʹ�
		for(int i=0;i<my_point.length;i++){
			my_point[i].setIsVisited(false);		
		}
		
		//������ĵ㣬�߽�㣬�����㼯��
		ArrayList<Point> Core = new ArrayList<Point>();
		ArrayList<Point> Border = new ArrayList<Point>();
		ArrayList<Point> Noise = new ArrayList<Point>();
		ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>() ; 

		//��ÿ������з���
		classify(my_point,Eps,MinPts,Core,Border,Noise);
		
		
		//��ʼ��ÿ�����ĵ���Ϊһ����
		for(int i=0;i<Core.size();i++){
			ArrayList<Point> list = new ArrayList<Point>();
			clusters.add(list);
			clusters.get(i).add(Core.get(i));
		}
		
		//��������֮�����С����С��Eps����ϲ���������
		for(int i=0;i<clusters.size();i++){
			for(int j=i+1;j<clusters.size();j++){
				if(distance(clusters.get(i),clusters.get(j))<Eps){
					clusters.get(i).addAll(clusters.get(j));
					clusters.remove(j);
					j-=1;
				}
			}
		}
		
		
		//�Ա߽����д����������ɵ������ĺ��ĵ�Ĵ���ȥ
		for(int i=0;i<Border.size();i++){
			clusters.get(whichCluster(Border.get(i),clusters)).add(Border.get(i));
		}
		
		
		
		//������*********************************
		System.out.println("��������");
		System.out.println("����"+my_point.length+"����");
		System.out.println("����"+Core.size()+"�����ĵ�");
		System.out.println("����"+Border.size()+"���߽��");
		System.out.println("����"+Noise.size()+"��������");
		System.out.println("����"+clusters.size()+"����");
		for(int i=0;i<clusters.size();i++){
			System.out.println("��"+(i+1)+"��������"+clusters.get(i).size()+"�����ݵ㣬����Ϊ��");
			//����ÿһ���أ�ÿ8�����ݵ㻻������������֮���ÿո����
    		for(int j =1;j<clusters.get(i).size()+1;j++){
    			if(j%8==0){
    				System.out.println(clusters.get(i).get(j-1).toString());
    			}else{
    				System.out.print(clusters.get(i).get(j-1).toString()+"  ");
    			}
    		}
            System.out.println();
		}
		System.out.println("������Ϊ��");
		for(int i=1;i<(Noise.size()+1);i++){
			//ÿ8�����ݵ㻻������������֮���ÿո����
    		if(i%8==0){
    			System.out.println(Noise.get(i-1).toString());
    		}else{
    			System.out.print(Noise.get(i-1).toString()+"  ");
    		}
            System.out.println();
		}
		
		
		
		//1.�������ĵ㣬���ݻ��ֵĴز�ͬ����������ļ���*******************
        for(int i=1;i<(clusters.size()+1);i++){
        	//��ȡÿһ�����е����ݵ�ĸ���
        	int pointNum = clusters.get(i-1).size();
        	/*
        	 * ָ���ֱ𱣴�ÿһ���ص�X��Y������ļ�·�����ļ���
        	 * ��cu��+i+��x.txt����ʾ��i��x�����ļ�
        	 * ��cu��+i+��x.txt����ʾ��i��y�����ļ�
        	 */
        	File xfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test3/cu"+i+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test3/cu"+i+"y.txt");
        	for(int j=0;j<pointNum;j++){
        		//��ÿһ�����е��������ݵ��x��y�������ݷֱ�д��ָ���ļ���
        		ReadDataset.append(xfile, clusters.get(i-1).get(j).getX()+" ");
        		ReadDataset.append(yfile, clusters.get(i-1).get(j).getY()+" ");
            }
        }
        //2.��������Ҳ������ļ���*******************************
        for(int i=0;i<Noise.size();i++){
        	File xfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test3/cu"+(clusters.size()+1)+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test3/cu"+(clusters.size()+1)+"y.txt");
        	//���������е��������ݵ��x��y�������ݷֱ�д��ָ���ļ���
        	ReadDataset.append(xfile, Noise.get(i).getX()+" ");
    		ReadDataset.append(yfile, Noise.get(i).getY()+" ");
        }
	}
}
