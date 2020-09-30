package com.newtec.company.utils;

import java.io.*;
import java.util.ArrayList;
 
public class TxtUtil {
 
    private static ArrayList<Float> dataGroup;
 
    public static void main(String[] args) {
        // write your code here
        //**************************读取
        File file = new File("C:\\Users\\Administrator\\Desktop\\TXT\\a.txt");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
 
            String readData;
            dataGroup = new ArrayList<>();
 
            while((readData = br.readLine())!=null){
                try{
                    dataGroup.add(Float.parseFloat(readData));//读出所有数据，存入ArrayList中
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
 
            br.close();
            fr.close();
 
            System.out.println(dataGroup.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        //*************************写入
        File file1 = new File("C:\\Users\\Administrator\\Desktop\\TXT\\bbb.txt");
        try {
            FileWriter fw = new FileWriter(file1);
            BufferedWriter bw = new BufferedWriter(fw);
 
            for(int i = 0; i<dataGroup.size();i++){
                bw.write(dataGroup.get(i).toString()+"\n");
                bw.flush();
            }
            bw.close();
            fw.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
