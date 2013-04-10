package Arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author pablohenrique
 */
public class SalvaBytes {
    private final String DIRECTORY_ROOT = "/home/pablohenrique/Projetos/Java/ORI-saida/";
    //private final String DIRECTORY_ROOT = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/";
    private final String FILE_INDEX = DIRECTORY_ROOT + "indice.dat";
    private final String FILE_VOCABULARY = DIRECTORY_ROOT + "vocabulario.dat";
    private final String FILE_INDEXINVFILE = DIRECTORY_ROOT + "indexinvfile.dat";
    private final String FILE_INVFILE = DIRECTORY_ROOT + "invfile.dat";
    private FileOutputStream fosI;
    private FileOutputStream fosV;
    private FileOutputStream fosII;
    private FileOutputStream fosF;
    private ObjectOutputStream oos;
    private int maxSizevocabulary = 28;
    private int maxSizeInvList = 864;
    private int maxSizeIndex = 4;
    private int maxSizeFileName = 4;
    private int maxSizeOcorr = 2;
    
    public SalvaBytes(){
        try {
            File fileI = new File(FILE_INDEX);
            File fileV = new File(FILE_VOCABULARY);
            File fileII = new File(FILE_INDEXINVFILE);
            File fileF = new File(FILE_INVFILE);
            
            if(!fileI.exists())
                fileI.createNewFile();
            else{
                fileI.delete();
                fileI.createNewFile();
            }
            
            if(!fileV.exists())
                fileV.createNewFile();
            else{
                fileV.delete();
                fileV.createNewFile();
            }
            
            if(!fileII.exists())
                fileII.createNewFile();
            else{
                fileII.delete();
                fileII.createNewFile();
            }
            
            if(!fileF.exists())
                fileF.createNewFile();
            else{
                fileF.delete();
                fileF.createNewFile();
            }
            
            this.fosI = new FileOutputStream(FILE_INDEX, true);
            this.fosV = new FileOutputStream(FILE_VOCABULARY, true);
            this.fosII = new FileOutputStream(FILE_INDEXINVFILE, true);
            this.fosF = new FileOutputStream(FILE_INVFILE, true);
            this.oos = new ObjectOutputStream(this.fosF);
            
            System.gc();
            
        } catch (IOException e) {
            System.out.println("SalbaBytes   " + e.toString());
        }
    }
    
    public void saveString(String toSave) throws FileNotFoundException, IOException {
        int size = toSave.length();
        this.fosV.write(toSave.getBytes());
        String barrazero = "\0";
        size++;
        while (size < maxSizevocabulary){
            barrazero += "\0";
            size++;
        }
        this.fosV.write(barrazero.getBytes());
    }
    
    public void saveInt(int toSave, String place) throws FileNotFoundException, IOException{
        int size = String.valueOf(toSave).length();
        if(place.equals("index")){
            this.fosI.write(toSave);
            String barrazero = "";
            size++;
            while(size < maxSizeIndex - 1){
                barrazero += "\0";
                size++;
            }
            this.fosI.write(barrazero.getBytes());
        }
        else{
            this.fosII.write(toSave);
            String barrazero = "\0";
            size++;
            while(size < maxSizeIndex - 1){
                barrazero += "\0";
                size++;
            }
            this.fosII.write(barrazero.getBytes());
        }
    }
    
    public void saveHashMap(HashMap<String, Integer> invIndex) throws FileNotFoundException, IOException {
        int qtdEntradas = invIndex.size();
        int size = qtdEntradas * 6;
        this.oos.writeObject(invIndex);
        String barrazero = "\0";
        size++;
        while(size < maxSizeIndex - 1){
            barrazero += "\0";
            size++;
        }
        this.oos.writeBytes(barrazero);
        
    }
    
    public void save(int index, String word, HashMap<String, Integer> invIndex){
        try {
            this.saveInt(index, "index");
            this.saveString(word);
            this.saveInt(index, "coisa");
            this.saveHashMap(invIndex);
        } catch (Exception e) {
            System.out.println("Erro ao salvar informacoes: " + e.toString());
        }
    }
    
    public void close(){
        try {
            this.oos.close();
            this.fosI.close();
            this.fosV.close();
            this.fosII.close();
            this.fosF.close();
        } catch (Exception e) {
            System.out.println("Erro ao fechar SalvaBytes   "+ e.toString());
        }    
    }
    
}
