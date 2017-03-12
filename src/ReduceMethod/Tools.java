package ReduceMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
class Dataset{
	public Map<String, Map<Integer, Double>> positive;
	public Map<String, Map<Integer, Double>> negative;
}
class Centrev{
	public String mark;
	public Map<Integer, Double> vector;
}
public class Tools {
	/**
	 * ���������ı������ƶ�
	 * 
	 * @param testWordTFMap
	 *            �ı�1��<����,ֵ>����
	 * @param trainWordTFMap
	 *            �ı�2<����,ֵ>����
	 * @return Double ����֮������ƶ� �������н����Ҽ���
	 * @throws IOException
	 */
	public double computeSim(Map<Integer, Double> testWordTFMap,
			Map<Integer, Double> trainWordTFMap) {
		// TODO Auto-generated method stub
		double mul = 0, testAbs = 0, trainAbs = 0;
		Set<Map.Entry<Integer, Double>> testWordTFMapSet = testWordTFMap
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = testWordTFMapSet
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (trainWordTFMap.containsKey(me.getKey())) {
				mul += me.getValue() * trainWordTFMap.get(me.getKey());
			}
			testAbs += me.getValue() * me.getValue();
		}
		testAbs = Math.sqrt(testAbs);

		Set<Map.Entry<Integer, Double>> trainWordTFMapSet = trainWordTFMap
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = trainWordTFMapSet
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			trainAbs += me.getValue() * me.getValue();
		}
		trainAbs = Math.sqrt(trainAbs);
		return mul / (testAbs * trainAbs);
	}
	/**
	 * ����������ľ���
	 * 
	 * @param map1
	 *            ��1������map
	 * @param map2
	 *            ��2������map
	 * @return double �������ŷʽ����
	 */
	public double getDistance(Map<Integer, Double> map1,
			Map<Integer, Double> map2) {
		// TODO Auto-generated method stub
		double mul = 0, result = 0, temp = 0;
		Set<Map.Entry<Integer, Double>> map1Set = map1
				.entrySet();
		Set<Map.Entry<Integer, Double>> map2Set = map2
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = map1Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (map2.containsKey(me.getKey())) {
				temp= me.getValue() - map2.get(me.getKey());
				
			}else temp= me.getValue();
			mul +=temp*temp;
		}
		for (Iterator<Map.Entry<Integer, Double>> it = map2Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (!map1.containsKey(me.getKey())){
				temp= me.getValue();
			mul +=temp*temp;
			}
		}
		result=Math.sqrt(mul);
		return result;
	}
	/**
	 * �ϲ������ı�������
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 *            �ı�������2
	 * @return map1 �����ı��������ĺϼ�
	 */
	public Map<String, Map<Integer, Double>> merge(Map<String, Map<Integer, Double>> map1,Map<String, Map<Integer, Double>> map2){
		 Map<String, Map<Integer, Double>> result=new TreeMap<String, Map<Integer, Double>>();
		 Set<Map.Entry<String,Map<Integer, Double>>> map1Set = map1.entrySet();
			for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map1Set
					.iterator(); it.hasNext();) {
				Map.Entry<String,Map<Integer, Double>> me=it.next();
				result.put(me.getKey(), me.getValue());
			}
		 Set<Map.Entry<String,Map<Integer, Double>>> map2Set = map2.entrySet();
		for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map2Set
				.iterator(); it.hasNext();) {
			Map.Entry<String,Map<Integer, Double>> me=it.next();
			result.put(me.getKey(), me.getValue());
		}
			return result;
	}
	/**
	 * ����������
	 * 
	 * @param map1
	 *            �ı�������1
	 * @return Centrevector 
	 * 			 �����ı�����������
	 */
	public Centrev getCentre(Map<String, Map<Integer, Double>> map1){
		Centrev cen=new Centrev();
		Map<Integer, Double> temp1=new TreeMap<Integer, Double>();
		Map<Integer, Double> temp2=new TreeMap<Integer, Double>();
		double count=0;
		 Set<Map.Entry<String,Map<Integer, Double>>> map1Set = map1.entrySet();
			for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map1Set
					.iterator(); it.hasNext();) {
				Map.Entry<String,Map<Integer, Double>> me=it.next();
				temp1=addmap(temp1,me.getValue());		
				cen.mark=me.getKey();
				count++;
			}
			Set<Map.Entry<Integer, Double>> tempSet = temp1
					.entrySet();
			for (Iterator<Map.Entry<Integer, Double>> it = tempSet
					.iterator(); it.hasNext();) {
				Map.Entry<Integer, Double> me = it.next();
				temp2.put(me.getKey(), me.getValue()/count);
			}
			cen.vector=temp2;
		return cen;
	}
	/**
	 * ����������
	 * 
	 * @param ArrayList<point>()
	 *            �ı�������1
	 * @return Centrevector 
	 * 			 �����ı�����������
	 */
	public Map<Integer, Double> getCeter(ArrayList<point> list){
		Map<Integer, Double> temp1=new TreeMap<Integer, Double>();
		for(int i=0;i<list.size();i++){
			temp1=addmap(temp1,list.get(i).vector);
		}	
		return onemul((1.0/list.size()),temp1);
		
	}
	/**
	 * �����������ӷ�
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 * 			       �ı�����2          
	 * @return map
	 * 			 �����ı�����������
	 */
	public  Map<Integer, Double> addmap( Map<Integer, Double> map1,Map<Integer, Double> map2){
		Map<Integer, Double> result=new TreeMap<Integer, Double>();
		double temp=0;
		Set<Map.Entry<Integer, Double>> map1Set = map1
				.entrySet();
		Set<Map.Entry<Integer, Double>> map2Set = map2
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = map1Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (map2.containsKey(me.getKey())) {
				temp= me.getValue() + map2.get(me.getKey());
				result.put(me.getKey(),temp);
			}else result.put(me.getKey(),me.getValue());
			
		}
		for (Iterator<Map.Entry<Integer, Double>> it = map2Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (!map1.containsKey(me.getKey())){
				result.put(me.getKey(),me.getValue());
			}
		}
		return result;
		
	}
	/**
	 * ��������������
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 * 			       �ı�����2          
	 * @return map
	 * 			 �����ı�����������
	 */
	
	public  Map<Integer, Double> submap( Map<Integer, Double> map1,Map<Integer, Double> map2){
		Map<Integer, Double> result=new TreeMap<Integer, Double>();
		double temp=0;
		Set<Map.Entry<Integer, Double>> map1Set = map1
				.entrySet();
		Set<Map.Entry<Integer, Double>> map2Set = map2
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = map1Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (map2.containsKey(me.getKey())) {
				temp= me.getValue() - map2.get(me.getKey());
				result.put(me.getKey(),temp);
			}else result.put(me.getKey(),me.getValue());
			
		}
		for (Iterator<Map.Entry<Integer, Double>> it = map2Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (!map1.containsKey(me.getKey())){
				result.put(me.getKey(),-me.getValue());
			}
		}
		return result;
		
	}
	/**
	 * �������������
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 * 			       �ı�����2          
	 * @return double
	 * 				��˽��
	 * 			 
	 */
	public double pointmul( Map<Integer, Double> map1,Map<Integer, Double> map2){
		double result=0,temp=0;
		Set<Map.Entry<Integer, Double>> map1Set = map1
				.entrySet();
		Set<Map.Entry<Integer, Double>> map2Set = map2
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = map1Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (map2.containsKey(me.getKey())) {
				temp= me.getValue() * map2.get(me.getKey());
				result +=temp;
			}
			
		}
		return result;
		
	}
	/**
	 * ��1���������һ������
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param x
	 * 			       ����        
	 * @return double
	 * 				��˽��
	 * 			 
	 */
	public Map<Integer, Double> onemul( double x,Map<Integer, Double> map1){
		double temp=0;
		Map<Integer, Double> result=new TreeMap<Integer, Double>();
		Set<Map.Entry<Integer, Double>> map1Set = map1
				.entrySet();
		
		for (Iterator<Map.Entry<Integer, Double>> it = map1Set
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			temp=me.getValue()*x;
			result.put(me.getKey(), temp);			
		}
		return result;
		
	}
	
	/**
	 * ������д��ָ���ļ���
	 * 
	 * @param filename
	 *            �ı���
	 * @param map1
	 * 			       �ı�����1          
	 * @return void
	 * 			 
	 */
	public void writevocter(String filename,Map<String, Map<Integer, Double>> map1){
		File file=new File(filename);
		try {
			FileWriter Writer = new FileWriter(file);
			 Set<Map.Entry<String,Map<Integer, Double>>> map1Set = map1.entrySet();
				for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map1Set
						.iterator(); it.hasNext();) {
					Map.Entry<String,Map<Integer, Double>> me=it.next();
				//	System.out.println("++++++"+me.getKey());
				    Writer.write(me.getKey()+" ");
				    Map<Integer, Double> temp=me.getValue();
				    Set<Map.Entry<Integer, Double>> tempset=temp.entrySet();
				    for(Iterator<Map.Entry<Integer, Double>> it1=tempset.iterator();it1.hasNext();){
				    	Map.Entry<Integer, Double> me1=it1.next();
				    	Writer.write(me1.getKey()+":"+me1.getValue()+" ");
				    	
				    }
				    Writer.write("\n");
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * ������������ǩ���
	 * 
	 * @param map1
	 * 			       �ı�����1          
	 * @return ArrayList
	 * 			 
	 */
	public ArrayList split(Map<String, Map<Integer, Double>> map1){
		 ArrayList al=new ArrayList();
		 String temp1=null,temp2=null;
		 Map<String, Map<Integer, Double>> tmap=new TreeMap<String, Map<Integer, Double>>();
		 Set<Map.Entry<String,Map<Integer, Double>>> map1Set = map1.entrySet();
			for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map1Set
					.iterator(); it.hasNext();) {
				Map.Entry<String,Map<Integer, Double>> me=it.next();
				temp2=me.getKey().split("_")[0];
				//System.out.println("++++++"+temp2+"++++"+temp1);
				if(temp2.equals(temp1)){
					tmap.put(me.getKey(), me.getValue());
				}else{
					al.add(tmap);
					tmap=new TreeMap<String, Map<Integer, Double>>();
					tmap.put(me.getKey(), me.getValue());
					temp1=temp2;
				}
			}
			al.add(tmap);
			al.remove(0);
			//System.out.println("++++++"+al.size());
			return al;
	}
	/**
	 * ������1��2��ͶӰ
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 * 			       �ı�����2          
	 * @return double
	 * 				��˽��
	 * 			 
	 */
	public double shadow(Map<Integer, Double> testWordTFMap,
			Map<Integer, Double> trainWordTFMap) {
		// TODO Auto-generated method stub
		double mul = 0, testAbs = 0, trainAbs = 0;
		Set<Map.Entry<Integer, Double>> testWordTFMapSet = testWordTFMap
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = testWordTFMapSet
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			if (trainWordTFMap.containsKey(me.getKey())) {
				mul += me.getValue() * trainWordTFMap.get(me.getKey());
			}
			testAbs += me.getValue() * me.getValue();
		}
		testAbs = Math.sqrt(testAbs);

		Set<Map.Entry<Integer, Double>> trainWordTFMapSet = trainWordTFMap
				.entrySet();
		for (Iterator<Map.Entry<Integer, Double>> it = trainWordTFMapSet
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> me = it.next();
			trainAbs += me.getValue() * me.getValue();
		}
		trainAbs = Math.sqrt(trainAbs);
		return mul / trainAbs;
	}
	/**
	 * �󼯺ϵĲ
	 * 
	 * @param map1
	 *            �ı�������1
	 * @param map2
	 * 			       �ı�����2          
	 * @return map
	 * 				map1-map2������ɾ��map1�е�map2
	 * 			 
	 */
	public Map<String, Map<Integer, Double>> chaji(Map<String, Map<Integer, Double>> map1,Map<String, Map<Integer, Double>> map2){
		Set<Map.Entry<String,Map<Integer, Double>>> map2Set = map2.entrySet();
		for (Iterator<Map.Entry<String,Map<Integer, Double>>> it = map2Set
				.iterator(); it.hasNext();) {
			Map.Entry<String,Map<Integer, Double>> me=it.next();
			String temp1=me.getKey();
			map1.remove(temp1);
		}
		return map1;
	}
	
}
