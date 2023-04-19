package com.akatec.aps.testcases;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Dummy {
    static Writer file ;

    static int i =0;

    private static String eol = System.getProperty("line.separator");



    private static void writeUsingFileWriter(String data) throws IOException {


         /*   {
                file = new FileWriter("JobIds.csv",true);
                i++;
            }
            file.append( data);

                System.out.println("printed");

        }*/


        Writer writer = new FileWriter("Region.csv");
        writer.append("region").append(eol);

            System.out.println(data);
            writer.append(data)
                    .append(eol);

    }

    public static void main(String[] args) throws IOException {
        for(int i= 0; i<5; i++)
        {
            System.out.println("before "+i);

            writeUsingFileWriter("sree");
            System.out.println("after "+i);
        }
    }
}