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

    private HashMap<Integer, HashMap<String, Integer>> indiceInvertidoExternal = new HashMap<>();
    private String saida = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/arquivosmerge/";
    private String saida2 = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/merged/";
    //private String saida = "/home/pablohenrique/Projetos/Java/ORI-saida/";
    int finalArquivo = 0;

    public void comparar(HashMap<Integer, HashMap<String, Integer>> lista1, HashMap<Integer, HashMap<String, Integer>> lista2) throws FileNotFoundException, IOException {
        for (Integer h : lista1.keySet()){
            if(lista2.containsKey(h)){
                HashMap<String, Integer> aux1 = new HashMap();
                HashMap<String, Integer> aux2 = new HashMap();
                aux1 = lista1.get(h);
                aux2 = lista2.get(h);
                for(String l : aux1.keySet()){
                    if(aux2.containsKey(l)){
                        int o = aux1.get(l) + aux2.get(l);
                        HashMap<String, Integer> auxiliar = new HashMap();
                        auxiliar.put(l, o);
                        this.indiceInvertidoExternal.put(h,auxiliar);
                    } else{
                        this.indiceInvertidoExternal.put(h, aux2);
                    }
                }
                
            }
        }
        for (Integer h : lista1.keySet()) {
            if (!this.indiceInvertidoExternal.containsKey(h)) {
                this.indiceInvertidoExternal.put(h, lista2.get(h));
            }
        }

        for (Integer h : lista2.keySet()) {
            if (!this.indiceInvertidoExternal.containsKey(h)) {
                this.indiceInvertidoExternal.put(h, lista2.get(h));
            }
        }

        //  }

        salvarHash();
        /*
         for(Integer h : lista2.keySet())
         if(!this.indiceInvertido.containsKey(h))
         this.indiceInvertido.put(h, lista2.get(h));
         */
    }
    
    

    public void teste() {
        HashMap<String, Integer> aux = new HashMap();
        for (int i = 0; i < 999; i++) {
            String letter = "a" + i;
            aux.put(letter, i);
        }
        for (int i = 0; i < 999; i++) {
            indiceInvertidoExternal.put(i, aux);
        }
    }

    public void ExternalMergeSort() {
        try {
            this.lerArquivos();
        } catch (Exception e) {
            System.out.println("erro no externalmergesort. " + e.toString());
        }
    }

    //Recebe uma string com o caminho
    public void lerArquivos() throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        File folder = new File(saida);
        File[] listaDeArquivos = folder.listFiles();

        for (int i = 0; i < listaDeArquivos.length - 1; i = i + 2) {
            String arquivos = listaDeArquivos[i].getName();
            if (arquivos.endsWith(".inv")) {
                //Le o arquivo inteiro
                ObjectInputStream s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i].toString())));
                //retorna um objeto desse aqui \/
                HashMap<Integer, HashMap<String, Integer>> hash1 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
                s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i + 1].toString())));
                HashMap<Integer, HashMap<String, Integer>> hash2 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
                //Chama outra funcao
                this.comparar(hash1, hash2);

                s1.close();
            }

        }
        lerArquivosMerge();
    }
    
        public void lerArquivosMerge() throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        File folder = new File(saida2);
        File[] listaDeArquivos = folder.listFiles();
        deleteAllMerge();
        if(listaDeArquivos.length == 1){
            System.out.println("FINALIZOU!");
        } else{
                    for (int i = 0; i < listaDeArquivos.length - 1; i = i + 2) {
            String arquivos = listaDeArquivos[i].getName();
            if (arquivos.endsWith(".merged")) {
                //Le o arquivo inteiro
                ObjectInputStream s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i].toString())));
                //retorna um objeto desse aqui \/
                HashMap<Integer, HashMap<String, Integer>> hash1 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
                s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i + 1].toString())));
                HashMap<Integer, HashMap<String, Integer>> hash2 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();
                
                listaDeArquivos[i].delete();
                listaDeArquivos[i+1].delete();
                //Chama outra funcao
                this.comparar(hash1, hash2);
                s1.close();
            }

        }
        lerArquivosMerge();
        }

    }

    public void deleteAllMerge() {
        File folder = new File("/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/arquivosmerge/");
        File[] listaDeArquivos = folder.listFiles();
        for (int i = 0; i < listaDeArquivos.length; i++) {
            listaDeArquivos[i].delete();
        }
    }
        
        public void deleteMerged(){
                  File folder2 = new File("/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/merged/");
        File[] listaDeArquivos2 = folder2.listFiles();
        for (int i = 0; i < listaDeArquivos2.length; i++) {
            listaDeArquivos2[i].delete();
        }  
        }


    

    public void salvarHash() throws FileNotFoundException, IOException {
        finalArquivo++;
        File file2 = new File("indiceInvertido" + finalArquivo + ".merged");
        FileOutputStream f2 = new FileOutputStream(saida2 + file2);
        ObjectOutputStream s2 = new ObjectOutputStream(f2);
        s2.writeObject(this.indiceInvertidoExternal);
        s2.flush();
        s2.close();
        f2.close();
        this.indiceInvertidoExternal.clear();
        file2 = null;
        System.gc();

    }
}