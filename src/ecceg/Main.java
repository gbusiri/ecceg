/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Moch Ginanjar Busiri
 */
public class Main {

    public static byte[] loadFile(String location) throws IOException{
        Path path = Paths.get(location);
        return Files.readAllBytes(path);
    }
    
    public static void writeFile(byte[] data, String location) throws IOException{
        Path path = Paths.get(location);
        Files.write(path, data);
    }
    
    public static void printFile(byte[] data){
        try {
            System.out.println(new String(data, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void printFileHex(byte[] data){
        StringBuilder builder = new StringBuilder();
        for (byte b:data){
            builder.append(String.format("%02X ", b));
        }
        System.out.println(builder.toString());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            long startTime = System.nanoTime();
            byte[] file = loadFile("gambar.gif");
            long fileSize = file.length;
            System.out.println("File size: " + String.valueOf(fileSize) + " bytes");
            writeFile(file ,"gambar_out.gif");
            printFile(file);
            printFileHex(file);
            long stopTime = System.nanoTime();
            System.out.println("Execution time:" + String.valueOf((stopTime-startTime)/1000000) + "ms");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
