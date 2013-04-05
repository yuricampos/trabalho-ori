
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

     
        
        ManipulacaoArquivos ma = new ManipulacaoArquivos();
        try {
            ma.lerArquivos();
        } catch (Exception ex) {
            
        } 
    }
}
