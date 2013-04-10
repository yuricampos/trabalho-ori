/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *
 * @author pablohenrique
 */
public class ExternalMerge {
    private HashMap<Integer, HashMap<String, Integer>> indiceInvertido = new HashMap<>();
    
    //private String saida = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/";
    private String saida = "/home/pablohenrique/Projetos/Java/ORI-saida/";
    
    private String indiceInvertidoResultante = this.saida + "indiceInvResult";
    private String indiceInvertidoFinal = this.saida + "indiceInvFinal";
    
    private File fResultante = new File(this.indiceInvertidoResultante);
    private File fFinal = new File(this.indiceInvertidoFinal);
    
    public void comparar(HashMap<Integer, HashMap<String, Integer>> lista1, HashMap<Integer, HashMap<String, Integer>> lista2){
        
        for(Integer h : lista1.keySet()){
           if(!lista2.containsKey(h))
               this.indiceInvertido.put(h, lista1.get(h));
           else{
               HashMap<String, Integer> aux = new HashMap<>();
               aux.putAll(lista1.get(h));
               aux.putAll(lista2.get(h));
               this.indiceInvertido.put(h, aux);
               System.gc();
           }
        }
        
        for(Integer h : lista2.keySet())
           if(!this.indiceInvertido.containsKey(h))
               this.indiceInvertido.put(h, lista2.get(h));
        
    }
    
    public void ExternalMergeSort(String pasta){
        try {
            this.lerArquivos(pasta);
        } catch (Exception e) {
            System.out.println("erro no externalmergesort. " + e.toString());
        }
    }
    
    public void lerArquivos(String pasta) throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        File folder = new File(pasta);
        File[] listaDeArquivos = folder.listFiles();
        
        for (int i = 0; i < listaDeArquivos.length-1; i++) {
            ObjectInputStream s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i].toString())));
            HashMap<Integer, HashMap<String, Integer>> hash1 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
            s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i+1].toString())));
            HashMap<Integer, HashMap<String, Integer>> hash2 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
            
            this.comparar(hash1, hash2);
            
            s1.close();
        }
    }
    
}