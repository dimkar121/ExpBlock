/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.edu.ihu.expblock;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.List;

/**
* A simple MinHash implementation inspired by https://github.com/jmhodges/minhash
*
* @author tpeng (pengtaoo@gmail.com)
*/
public class MinHash {
    
 

    private HashFunction hash = Hashing.murmur3_32_fixed();

    public String hash(String string) {
        int min = Integer.MAX_VALUE;
        for (int i=0; i<string.length(); i++) {
            int c = string.charAt(i);
            int n = hash.hashInt(c).asInt();
            if (n < min) {
                min = n;
            }
        }
        return Integer.toHexString(min)+string.charAt(0) ;
    }

    public static void main(String... args) {
        MinHash minHash = new MinHash();
        System.out.println(minHash.hash("Dimitris"));
        System.out.println(minHash.hash("Dimitrios"));
        ArrayList<String>  a =new ArrayList();
        a.add("Dimitrios");
       
    }
}