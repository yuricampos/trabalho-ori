package Arquivo;

import Algoritmo.ExternalMerge;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

    int finalArquivo = 1;
    int indicePalavra = -1;
    int qtd = 1;
    int MegaBytes = 10241024;
    //private SalvaBytes s = new SalvaBytes();
    // Palavra -> Indice (tem que gerar 2 arquivos)
    HashMap<String, Integer> indiceVocabulario = new HashMap<>();
    
    
    // indice -> lista invertida (doc,ocorrencia)
    HashMap<Integer, HashMap<String, Integer>> indiceInvertido = new HashMap<>();
    HashMap<String, String> stopword = new HashMap<>();
    String pasta = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/";
    //String pasta = "/home/pablohenrique/Projetos/Java/trabalho-ori/trabalho-ori/src/corpus/";
    String tohash = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/stopwords/stopwords.txt";
    //String tohash = "/home/pablohenrique/Projetos/Java/trabalho-ori/trabalho-ori/src/stopwords/stopwords.txt";
    String saida = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/arquivosmerge/";
    public final static String saida2 = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/arquivosvocabulario/";
    //String saida = "/home/pablohenrique/Projetos/Java/ORI-saida/";
    long memoria = 20;
    long memoriaUsada = 0;

    public void lerArquivos() throws ClassNotFoundException, FileNotFoundException, FileNotFoundException, IOException {
        deleteFiles();
        String arquivos;
        File folder = new File(pasta);
        this.stopword = toHash(tohash);
        File[] listaDeArquivos = folder.listFiles();
        for (int i = 0; i < listaDeArquivos.length; i++) {
            memoriaUsada = memoriaUsada + 1;
            //float sizeArquivo = listaDeArquivos[i].getUsableSpace() / 1024 / 1024;
            if (listaDeArquivos[i].isFile()) {
                arquivos = listaDeArquivos[i].getName();
                if (arquivos.endsWith(".html") || arquivos.endsWith(".htm")) {

                    // System.out.println("Size Arquivo " + sizeArquivo);
                    //float heapSize = Runtime.getRuntime().totalMemory();
                    //memoriaUsada = memoriaUsada + sizeArquivo;
                    //memoriaUsada = (long) (heapSize / MegaBytes);
                    System.out.println("MEMORIA USADA: " + memoriaUsada);
                    long memoriaDisponivel = memoria - memoriaUsada;
                    System.out.println("MEMORIA TOTAL: " + memoria);
                    if (memoriaDisponivel <= 1) {
                        salvarHash();
                        //memoriaUsada = sizeArquivo;
                        memoriaUsada = 0;
                        System.out.println("LENDO ARQUIVO NRO: " + qtd + " NOME: " + arquivos);
                        qtd++;
                        salvarPalavra(arquivos);
                    } else {
                        System.out.println("LENDO ARQUIVO NRO: " + qtd + " NOME: " + arquivos);
                        qtd++;
                        salvarPalavra(arquivos);

                    }
                }
            }
        }
        salvarHash();
        salvarVocabulario();
        ExternalMerge me = new ExternalMerge();
        me.setindiceVocabularioExternal(indiceVocabulario);
        me.ExternalMergeSort();
        
        //s.close();
    }
    
        public void deleteFiles(){
        File folder = new File(saida2);
        File[] listaDeArquivos = folder.listFiles();
                for (int i = 0; i < listaDeArquivos.length; i++) {
            listaDeArquivos[i].delete();
        }
    }

    public void salvarHash() throws FileNotFoundException, IOException {
        finalArquivo++;
        File file2 = new File("indiceInvertido" + finalArquivo + ".inv");
        FileOutputStream f2 = new FileOutputStream(saida + file2);
        ObjectOutputStream s2 = new ObjectOutputStream(f2);
        s2.writeObject(indiceInvertido);
        s2.flush();
        s2.close();
        f2.close();
        indiceInvertido.clear();
        file2 = null;
        System.gc();

    }

    public void salvarVocabulario() throws FileNotFoundException, IOException {
        File file = new File("indiceVocabulario"+".inv");
        FileOutputStream f = new FileOutputStream(saida2 + file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(indiceVocabulario);
        s.flush();
        s.close();
        f.close();
    }

    public void salvarPalavra(String arquivo) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pasta + arquivo), "ISO8859-1"))) {
                while (br.ready()) {
                    //int serialMem;
                    String linha = br.readLine().replaceAll("\\<.*?\\>", "").replaceAll("&ccedil;", "c").replaceAll("&Ccedil;", "C").replaceAll("&acirc;", "a").replaceAll("&ecirc;", "e").replaceAll("&ocirc;", "o").replaceAll("&aacute;", "a").replaceAll("&eacute;", "e").replaceAll("&iacute;", "i").replaceAll("&oacute;", "o").replaceAll("&uacute;", "u").replaceAll("&atilde;", "a").replaceAll("&otilde;", "o").replaceAll("&Acirc;", "A").replaceAll("&Ecirc;", "E").replaceAll("&Ocirc;", "O").replaceAll("&Aacute;", "A").replaceAll("&Eacute;", "E").replaceAll("&Iacute;", "I").replaceAll("&Oacute;", "O").replaceAll("&Uacute;", "U").replaceAll("&Atilde;", "A").replaceAll("&Otilde;", "O").replaceAll("&nbsp;", " ").toUpperCase();
                    Scanner sc = new Scanner(linha);
                    sc.useDelimiter("[^ABCDEFGHIJKLMNOPQRSTUVXWYZ1234567890ÇÁÉÍÓÚÃÕÂÊÎÔÛº]");
                    while (sc.hasNext()) {
                        String proximaPalavra = sc.next();
                        if (proximaPalavra.length() > 0 && !this.stopword.containsKey(proximaPalavra)) {
                            insertHash(proximaPalavra, arquivo);
                        }
                    }
                    sc.close();
                }
            }



        } catch (IOException ioe) {
        }

    }

    public void insertHash(String palavra, String arquivo) {
        HashMap<String, Integer> lista = new HashMap<>();
        if (indiceVocabulario.containsKey(palavra)) {
            Integer indice = consultaIndiceVocabulario(palavra);
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
        } else {
            indicePalavra++;
            
            indiceVocabulario.put(palavra, indicePalavra);
            lista.put(arquivo, 1);
            indiceInvertido.put(indicePalavra, lista);
        }
    }
    
    public int consultaIndiceVocabulario(String palavra){
        return  indiceVocabulario.get(palavra);
    }

    public HashMap<String, String> toHash(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IOException("Arquivo nao existe.");
            }

            BufferedReader br = new BufferedReader(new FileReader(filePath));

            HashMap<String, String> stopword = new HashMap<>();
            String currentLine;
            //int index = 0;
            while ((currentLine = br.readLine()) != null) {
                currentLine = currentLine.replaceAll("[ÂÀÁÄÃ]", "A").replaceAll("[âãàáä]", "a").replaceAll("[ÊÈÉË]", "E").replaceAll("[êèéë]", "e").replaceAll("ÎÍÍÌÏ", "I").replaceAll("îííìï", "i").replaceAll("[ÔÕÒÓÖ]", "O").replaceAll("[ôõòóö]", "o").replaceAll("[ÛÙÚÜ]", "U").replaceAll("[ûúùü]", "u").replaceAll("Ç", "C").replaceAll("ç", "c").replaceAll("[ýÿ]", "y").replaceAll("Ý", "Y").replaceAll("ñ", "n").replaceAll("Ñ", "N").replaceAll("['<>\\|/]", "").toUpperCase();
                stopword.put(currentLine, currentLine);
            }

            if (stopword.isEmpty()) {
                throw new Exception("Stopword vazia.");
            }

            System.gc();
            br.close();
            return stopword;

        } catch (Exception e) {
            return null;
        }
    }
    
    public HashMap<String, Integer> getindiceVocabulario(){
        return this.indiceVocabulario;
    }
/*
    public void salvarBytes() {
        for (String key : indiceVocabulario.keySet()) {
            HashMap<String, Integer> listaInv = new HashMap<>();
            int indice = indiceVocabulario.get(key);
            listaInv = indiceInvertido.get(indice);
            s.save(indice, key, listaInv);
            listaInv.clear();
        }

        indiceVocabulario.clear();
        indiceInvertido.clear();
    }
*/
}
