package weka.classifiers.rules;

import java.util.ArrayList;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;

import weka.core.converters.ConverterUtils.DataSource;

public class Condicion {

    private int atributoIndex; // Índice del atributo
    private String valor;      // Valor esperado del atributo

    // Constructor
    public Condicion(int atributoIndex, String valor) {
        this.atributoIndex = atributoIndex;
        this.valor = valor;
    }

    /**
     * Verifica si esta condición es aplicable a una instancia.
     *
     * @param instancia Instancia a evaluar.
     * @return true si la condición se cumple, false en caso contrario.
     */
    public boolean esAplicable(Instance instancia) {
        return instancia.stringValue(atributoIndex).equals(valor);
    }

    /**
     * Obtiene la cobertura de la condición sobre un conjunto de ejemplos.
     *
     * @param datos Conjunto de ejemplos (Instances).
     * @return Lista de instancias que cumplen la condición.
     */
    public List<Instance> cobertura(Instances datos) {
        List<Instance> cubiertos = new ArrayList<>();
        for (Instance instancia : datos) {
            if (esAplicable(instancia)) {
                cubiertos.add(instancia);
            }
        }
        return cubiertos;
    }

    @Override
    public String toString() {
        return "Atributo[" + atributoIndex + "] = " + valor;
    }

    public String toString(Instances data) {
        return data.attribute(atributoIndex).name() + " = " + valor;
    }


    public static void main(String[] args) throws Exception{
        // Lee datos de tenis
        DataSource source = new DataSource("weather.nominal.arff");
        Instances datos = source.getDataSet();

        // Pon clase
        datos.setClassIndex(4);

        // Imprimelos 
        // System.out.println(datos);

        Condicion c = new Condicion(1, "cool");

        List<Instance> cob = c.cobertura(datos);

        System.out.println(cob);

    }
}

