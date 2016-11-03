/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecceg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
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
    
    public static void writePoint(Point p, String location) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(location);
        out.println(p.toString());
    }
    
    public static void writeBigInt(BigInteger data, String location) throws IOException{
        PrintWriter out = new PrintWriter(location);
        out.println(data.toString());
    }
    
    public static void writeArrayOfPoint(ArrayList<Point> points, String location) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(location);
        for (Point point:points){
            out.println(point.toString()); 
        }
    }
    
    public static ArrayList<Point> stringToArrayOfPoint(String input){
        String inputs[] = input.split("\\r?\\n");
        ArrayList<Point> points = new ArrayList();
        for (String sp : inputs) {
            String tmp[] = sp.split(" ");
            Point p = new Point(new BigInteger(tmp[0]), new BigInteger(tmp[1]));
            points.add(p);
        }
        return points;
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
    
    public static void encode() throws IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Alamat file yang akan dienkripsi:");
        byte[] plainfile = loadFile(sc.nextLine());
        long fileSize = plainfile.length;
        System.out.println("Ukuran plainteks: " + String.valueOf(fileSize) + " bytes");
        printFile(plainfile);
        printFileHex(plainfile);

        System.out.println("Sedang mengenkripsi...");
        ArrayList<Point> cipherText = new ArrayList();
        long startTime = System.nanoTime();
        Ecceg.encode(new String(plainfile,"UTF-8"), cipherText);
        long stopTime = System.nanoTime();
        System.out.println("Waktu enkripsi:" + String.valueOf((stopTime-startTime)/1000000) + "ms");

        System.out.println("Alamat file output: ");
        String output = sc.nextLine();
        writeArrayOfPoint(cipherText, output);

        byte[] reloadEncrypted = loadFile(output);
        fileSize = reloadEncrypted.length;
        System.out.println("Ukuran cipherteks: " + String.valueOf(fileSize) + " bytes");
        printFile(reloadEncrypted);
        printFileHex(reloadEncrypted);  
    }
    
    public static void decode() throws IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Alamat file yang akan didekripsi:");
        byte[] cipherfile = loadFile(sc.nextLine());
        long fileSize = cipherfile.length;
        System.out.println("Ukuran cipherteks: " + String.valueOf(fileSize) + " bytes");
        System.out.println("Isi file: ");
        printFile(cipherfile);
        printFileHex(cipherfile);

        System.out.println("Sedang mendekripsi...");
        ArrayList<Point> cipherText = stringToArrayOfPoint(new String(cipherfile, "UTF-8"));
        long startTime = System.nanoTime();
        String plainFile = "";
        Ecceg.decode(plainFile, cipherText);
        long stopTime = System.nanoTime();
        System.out.println("Waktu dekripsi:" + String.valueOf((stopTime-startTime)/1000000) + "ms");

        System.out.println("Alamat file output: ");
        String output = sc.nextLine();
        PrintWriter writer = new PrintWriter(output);
        writer.println(plainFile);
        System.out.println("Isi file:");
        printFile(plainFile.getBytes(Charset.forName("UTF-8")));
        printFileHex(plainFile.getBytes(Charset.forName("UTF-8")));
        fileSize = plainFile.getBytes(Charset.forName("UTF-8")).length;
        System.out.println("Ukuran plainteks: " + String.valueOf(fileSize) + " bytes");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Point publicKey;
            BigInteger privateKey;
            Point basePoint = new Point(new BigInteger("1"), new BigInteger("2")); // Base point? harus pake apa?
            
            Scanner sc = new Scanner(System.in);
            System.out.println("Bangkitkan kunci? (y/n)");
            boolean generateKey = sc.nextLine().equals("y");
            
            if (generateKey){ // Generate kunci
                System.out.println("Kunci privat: ");
                privateKey = new BigInteger(sc.nextLine());
                publicKey = Point.multiplyPoint(privateKey, basePoint);
                System.out.println("Kunci publik berhasil dibuat dari kunci privat!");
                System.out.println("alamat kunci privat:");
                writeBigInt(privateKey, sc.nextLine());
                System.out.println("alamat kunci publik:");
                String publicKeyLocation = sc.nextLine();
                writePoint(publicKey, publicKeyLocation);
            }
            else{ // Load kunci dari file
                System.out.println("Memuat file kunci...");
                System.out.println("alamat kunci privat:");
                String privateKeyLocation = sc.nextLine();
                String privateKeyFile = new String(loadFile(privateKeyLocation),"UTF-8");
                privateKey = new BigInteger(privateKeyFile);
                System.out.println("alamat kunci publik:");
                String publicKeyLocation = sc.nextLine();
                String publicKeyFile[] = new String(loadFile(publicKeyLocation), "UTF-8").split(" ");
                publicKey = new Point(new BigInteger(publicKeyFile[0]), new BigInteger(publicKeyFile[1]));
            }
            
            System.out.println("(e)ncrypt/(d)ecrypt?");
            String choice = sc.nextLine();
            if (choice.equals("e")){
                encode();
            }
            else {
                decode();
            }
            
            
           
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
