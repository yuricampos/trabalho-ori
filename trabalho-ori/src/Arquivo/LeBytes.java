/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Arquivo;

import java.io.File;

/**
 *
 * @author yuricampos
 */
public class LeBytes {
    
    private final String DIRECTORY_ROOT = "/users/yuricampos/Documents/ori/trabalho-ori/trabalho-ori/src/corpus/";
    File indexinvfile = new File(DIRECTORY_ROOT+"indexinvfile.dat");
    File indice = new File(DIRECTORY_ROOT+"indice.dat");
    File invfile = new File(DIRECTORY_ROOT+"invfile.dat");
    
    public void iniciaLeitura(int memoria){
    int sizeindexinvfile = (int) indexinvfile.length();
    int sizeindice = (int) indice.length();
    int sizeinvfile = (int) invfile.length();
    
        
    }
    
}
