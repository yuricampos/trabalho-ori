package Arquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yuricampos
 */
public class ManipulacaoArquivos {
    
    float memoriaTotal;
    int finalArquivo = 1;
    int MegaBytes = 10241024;
    int indicePalavra = -1;
    // Palavra -> Indice (tem que gerar 2 arquivos)
    HashMap<String, Integer> indiceVocabulario = new HashMap<>();
    // indice -> lista invertida (doc,ocorrencia)
    HashMap<Integer, HashMap<String, Integer>> indiceInvertido = new HashMap<>();
    String pasta = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/";
    int memoria = 20;

    public void lerArquivos() throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        String arquivos;
        File folder = new File(pasta);
        File[] listaDeArquivos = folder.listFiles();
        for (int i = 0; i < listaDeArquivos.length; i++) {
            //pega memoria que esta sendo usada na execucao
            float heapSize = Runtime.getRuntime().totalMemory();
            //transforma memoria em MB
            float memoriaUsada = heapSize / MegaBytes;
            float memoriaDisponivel = memoria - memoriaUsada;
           // System.out.println("MEMORIA TOTAL: "+memoria);
           // System.out.println("MEMORIA USADA: "+memoriaUsada);
           // System.out.println("MEMORIA DISPONIVEL: "+memoriaDisponivel);
           
            if (memoriaDisponivel <= 1) {
                salvarBytes();
                System.gc();
                if (listaDeArquivos[i].isFile()) {
                    arquivos = listaDeArquivos[i].getName();
                    if (arquivos.endsWith(".html") || arquivos.endsWith(".htm")) {
                        salvarPalavra(arquivos);
                    }
                }
            } else {
                if (listaDeArquivos[i].isFile()) {
                    arquivos = listaDeArquivos[i].getName();
                    if (arquivos.endsWith(".html") || arquivos.endsWith(".htm")) {
                        salvarPalavra(arquivos);

                    }
                }
            }
        }
    }

    public void salvarHash() throws FileNotFoundException, IOException {
        finalArquivo++;
        
        //salvar hashmap de indice -> vocabulario
        File file = new File("indiceVocabulario" + finalArquivo);
        FileOutputStream f = new FileOutputStream(pasta + file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(indiceVocabulario);
        s.flush();
        indiceVocabulario.clear();
        //salvar hashmap de indice -> lista invertida (doc,ocorrencia)
        File file2 = new File("indiceInvertido" + finalArquivo);
        FileOutputStream f2 = new FileOutputStream(pasta + file2);
        ObjectOutputStream s2 = new ObjectOutputStream(f2);
        s2.writeObject(indiceInvertido);
        s2.flush();
        indiceInvertido.clear();

    }

    public void salvarPalavra(String arquivo) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pasta + arquivo), "ISO8859-1"))) {
                while (br.ready()) {
                    int serialMem;
                    String linha = br.readLine().replaceAll("\\<.*?\\>", "").replaceAll("&ccedil;", "c").replaceAll("&Ccedil;", "C").replaceAll("&acirc;", "a").replaceAll("&ecirc;", "e").replaceAll("&ocirc;", "o").replaceAll("&aacute;", "a").replaceAll("&eacute;", "e").replaceAll("&iacute;", "i").replaceAll("&oacute;", "o").replaceAll("&uacute;", "u").replaceAll("&atilde;", "a").replaceAll("&otilde;", "o").replaceAll("&Acirc;", "A").replaceAll("&Ecirc;", "E").replaceAll("&Ocirc;", "O").replaceAll("&Aacute;", "A").replaceAll("&Eacute;", "E").replaceAll("&Iacute;", "I").replaceAll("&Oacute;", "O").replaceAll("&Uacute;", "U").replaceAll("&Atilde;", "A").replaceAll("&Otilde;", "O").replaceAll("&nbsp;", " ").toUpperCase();
                    Scanner sc = new Scanner(linha);
                    sc.useDelimiter("[^ABCDEFGHIJKLMNOPQRSTUVXWYZ1234567890ÇÁÉÍÓÚÃÕÂÊÎÔÛº]");
                    while (sc.hasNext()) {
                        String proximaPalavra = sc.next();
                        if (proximaPalavra.length() > 0) {
                            insertHash(proximaPalavra, arquivo);
                        }

                    }
                }
            }



        } catch (IOException ioe) {
        }

    }

    public void insertHash(String palavra, String arquivo) {
        HashMap<String, Integer> lista = new HashMap<>();
        if (indiceVocabulario.containsKey(palavra)) {
            Integer indice = indiceVocabulario.get(palavra);
            if (indiceInvertido.containsKey(indice)) {
                lista = indiceInvertido.get(indice);
                if (lista.containsKey(arquivo)) {
                    int ocorrencia = lista.get(arquivo);
                    ocorrencia++;
                    lista.put(arquivo, ocorrencia);
                    indiceInvertido.put(indice, lista);
                    lista.clear();
                } else {
                    lista.put(arquivo, 1);
                    indiceInvertido.put(indice, lista);
                    lista.clear();
                }
            }
        }else {
                indicePalavra++;
                indiceVocabulario.put(palavra, indicePalavra);
                lista.put(arquivo, 1);
                indiceInvertido.put(indicePalavra, lista);

            }
        }
    
    public void salvarBytes(){
        SalvaBytes s = new SalvaBytes();
        for(String key: indiceVocabulario.keySet()){
            HashMap<String, Integer> listaInv = new HashMap<>();
            int indice = indiceVocabulario.get(key);
            listaInv = indiceInvertido.get(indice);
            s.save(indice, key, listaInv);
            System.gc();       
        }
    }
    

}
    
