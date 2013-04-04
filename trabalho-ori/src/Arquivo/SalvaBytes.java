package Arquivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    //private final String DIRECTORY_ROOT = "/users/yuricampos/desktop/Desktop/workspace/arquivos/";
    private final String FILE_INDEX = DIRECTORY_ROOT + "indice.dat";
    private final String FILE_VOCABULARY = DIRECTORY_ROOT + "vocabulario.dat";
    private final String FILE_INDEXINVFILE = DIRECTORY_ROOT + "indexinvfile.dat";
    private final String FILE_INVFILE = DIRECTORY_ROOT + "invfile.dat";
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    
    public SalvaBytes(){
        try {
            File fileI = new File(FILE_INDEX);
            File fileV = new File(FILE_VOCABULARY);
            File fileII = new File(FILE_INDEXINVFILE);
            File fileF = new File(FILE_INVFILE);
            
            if(!fileI.exists())
                fileI.createNewFile();
            if(!fileV.exists())
                fileV.createNewFile();
            if(!fileII.exists())
                fileII.createNewFile();
            if(!fileF.exists())
                fileF.createNewFile();
            
            System.gc();
            
        } catch (IOException e) {
            System.out.println("SalbaBytes   " + e.toString());
        }
    }
    
    public void saveString(String toSave, String place) throws FileNotFoundException, IOException {
        this.fos = new FileOutputStream(FILE_VOCABULARY);
        this.fos.write(toSave.getBytes());
        this.fos.flush();
        this.fos.close();
    }
    
    public void saveInt(int toSave, String place) throws FileNotFoundException, IOException{
        if(place.equals("index"))
            this.fos = new FileOutputStream(FILE_INDEX);
        else
            this.fos = new FileOutputStream(FILE_INDEXINVFILE);
        
        this.fos.write(toSave);
        this.fos.flush();
        this.fos.close();
    }
    
    public void saveHashMap(HashMap<Integer, HashMap<String, Integer>> invIndex) throws FileNotFoundException, IOException {
        this.fos = new FileOutputStream(FILE_INVFILE);
        this.oos = new ObjectOutputStream(this.fos);
        this.oos.writeObject(invIndex);
        this.oos.close();
        this.fos.close();
    }
    
}
