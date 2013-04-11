
import Algoritmo.ExternalMerge;
import Arquivo.ManipulacaoArquivos;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yuricampos
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExternalMerge me1 = new ExternalMerge();
        me1.deleteAllMerge();
        me1.deleteMerged();
     
        
        ManipulacaoArquivos ma = new ManipulacaoArquivos();
        try {
            
            ma.lerArquivos();
            ExternalMerge me = new ExternalMerge();
            me.ExternalMergeSort();
        } catch (Exception ex) {
            
        } 
    }
}
