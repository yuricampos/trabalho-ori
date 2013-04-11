/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmo;

import Arquivo.ManipulacaoArquivos;
import Arquivo.SalvaBytes;
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
    ManipulacaoArquivos ma = new ManipulacaoArquivos();
    HashMap<String, Integer> indiceVocabularioExternal;
    

    int finalArquivo = 0;

    public void comparar(HashMap<Integer, HashMap<String, Integer>> lista1, HashMap<Integer, HashMap<String, Integer>> lista2) throws FileNotFoundException, IOException {
        for (Integer h : lista1.keySet()) {
            if (lista2.containsKey(h)) {
                HashMap<String, Integer> aux1 = new HashMap();
                HashMap<String, Integer> aux2 = new HashMap();
                aux1 = lista1.get(h);
                aux2 = lista2.get(h);
                for (String l : aux1.keySet()) {
                    if (aux2.containsKey(l)) {
                        int o = aux1.get(l) + aux2.get(l);
                        HashMap<String, Integer> auxiliar = new HashMap();
                        auxiliar.put(l, o);
                        this.indiceInvertidoExternal.put(h, auxiliar);
                    } else {
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


        salvarHash();
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
        File folder = new File(ManipulacaoArquivos.saidaParaMerge);
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
        File folder = new File(ManipulacaoArquivos.saidaMerged);
        File[] listaDeArquivos = folder.listFiles();
        ManipulacaoArquivos ma = new ManipulacaoArquivos();
        ma.deleteAllMerge();
        if (listaDeArquivos.length == 1) {
            finalizarMerge();
        } else {
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
                    listaDeArquivos[i + 1].delete();
                    //Chama outra funcao
                    this.comparar(hash1, hash2);
                    s1.close();
                }

            }
            lerArquivosMerge();
        }

    }
    
        public HashMap<Integer, HashMap<String, Integer>> lerArquivoFinalMerge() throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        File folder = new File(ManipulacaoArquivos.saidaMerged);
        File[] listaDeArquivos = folder.listFiles();
        HashMap<Integer, HashMap<String, Integer>> hash1 = null;
            for (int i = 0; i < listaDeArquivos.length; i++) {
                String arquivos = listaDeArquivos[i].getName();
                if (arquivos.endsWith(".merged")) {
                    //Le o arquivo inteiro
                    ObjectInputStream s1 = new ObjectInputStream(new FileInputStream(new File(listaDeArquivos[i].toString())));
                    //retorna um objeto desse aqui \/
                    hash1 = (HashMap<Integer, HashMap<String, Integer>>) s1.readObject();

                    s1.close();
                }

            }
            
        
        return hash1;
        
        

    }
    

        
    
    public void finalizarMerge() throws ClassNotFoundException, FileNotFoundException, IOException{
        indiceInvertidoExternal.clear();
        indiceInvertidoExternal = lerArquivoFinalMerge();
        salvarBytes();
        
    }
    
        public void salvarBytes() {
            SalvaBytes s = new SalvaBytes();
        for (String key : indiceVocabularioExternal.keySet()) {
            HashMap<String, Integer> listaInv = new HashMap<>();
            int indice = indiceVocabularioExternal.get(key);
            listaInv = indiceInvertidoExternal.get(indice);
            s.save(indice, key, listaInv);
         //   listaInv.clear();
        }
    }
    
    

    public void salvarHash() throws FileNotFoundException, IOException {
        finalArquivo++;
        File file2 = new File("indiceInvertido" + finalArquivo + ".merged");
        FileOutputStream f2 = new FileOutputStream(ManipulacaoArquivos.saidaMerged + file2);
        ObjectOutputStream s2 = new ObjectOutputStream(f2);
        s2.writeObject(this.indiceInvertidoExternal);
        s2.flush();
        s2.close();
        f2.close();
        this.indiceInvertidoExternal.clear();
        file2 = null;
        System.gc();

    }
    
        public void setindiceVocabularioExternal(HashMap<String, Integer> indice){
        this.indiceVocabularioExternal = indice;
    }

}