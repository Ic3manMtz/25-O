package mx.izt.uam.am;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

// Librerias WEKA
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Clasifica {

    public static void main(String[] args) throws Exception {
        
        Classifier clasificador;
        Instances data;

        // Lee modelo
        File modelo = new File("../JRip.model");
        InputStream in = new FileInputStream(modelo);
        
        clasificador = (Classifier) SerializationHelper.read(in);
        
        // Obtiene el nombre del modelo sin extensión para el archivo de salida
        String nombreModelo = modelo.getName();
        if (nombreModelo.lastIndexOf(".") > 0) {
            nombreModelo = nombreModelo.substring(0, nombreModelo.lastIndexOf("."));
        }
        
        // Construye el nombre del archivo de salida
        String nombreArchivoSalida = "submission_" + nombreModelo + ".csv";
        
        // Lee datos
        File inF = new File("titanic_test_cleaned.arff");
        InputStream inD = new FileInputStream(inF);
        
        DataSource source = new DataSource(inD);
        data = source.getDataSet();
        // Agrega clase que no se usa
        Attribute at = new Attribute("Survived");        
        data.insertAttributeAt(at, 1);
        data.setClassIndex(1);
        
        // Crea archivo CSV para resultados
        PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivoSalida));
        
        // Escribe cabecera del CSV con los nombres solicitados
        writer.println("PassengerId,Survived");
        
        // Clasifica todas las instancias
        for (int i = 0; i < data.size(); i++) {
            Instance datum = data.get(i);
            double clase = clasificador.classifyInstance(datum);
            
            // Redondea los valores
            long passengerId = Math.round(datum.value(0));
            long survived = Math.round(clase);
            
            // Escribe en el archivo CSV
            writer.println(passengerId + "," + survived);
            
            // Opcional: mantener salida en consola
            System.out.println(passengerId + ", " + survived);
        }
        
        // Cierra el archivo
        writer.close();
        
        System.out.println("Resultados guardados en " + nombreArchivoSalida);
				//esta es una prueba
    }
}