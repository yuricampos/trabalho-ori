
import Arquivo.ManipulacaoArquivos;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
