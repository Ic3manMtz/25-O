package weka.classifiers.rules;

import java.io.Serializable;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Regla implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Complejo conds;
	
	int cons; // Si la regla es verdad
	
	Regla(int c)  { // La regla debe tener al menos un consecuente
	
		cons = c;
		conds = new Complejo();
	}
	
	Regla(Complejo i, int c) {
		conds = i;
		cons = c;
	}
	
	public void addC(Complejo c) {
		conds = c;
	}
	
	public int evalR(Instance ins) { 
		// Regresa -1 si no se pudo evaluar
		
		if (conds.isEmpty()) { // No hay nada solo regresa consecuente
			return cons;
		}
		else {
			if (conds.evalC(ins)) { // Eval si hay condiciones
				// Checa complejo			
				return (cons);
			}
			
			return -1;

		}
	}
	
	public String toString() {
		// Regresa condicion
		
		String regla = (conds.isEmpty()) ? "" : "if ";

		regla += conds.toString();

		regla += (conds.isEmpty()) ? "" : " then ";
		
		regla += "" + cons;

		return regla;
	}
	
	public String toString(Instances data) {
		// Regresa condicion
		
		String regla = (conds.isEmpty()) ? "" : "if ";

		regla += conds.toString(data);

		regla += (conds.isEmpty()) ? "" : " then ";
		
		regla += "" + data.attribute(data.classIndex()).value(cons);

		return regla;
	}
	public static void main(String[] args) throws Exception{
        // Lee datos de tenis
        DataSource source = new DataSource("weather.nominal.arff");
        Instances datos = source.getDataSet();

        // Pon clase
        datos.setClassIndex(4);

        // Imprimelos 
        System.out.println(datos);

        Condicion c1 = new Condicion(0, "sunny");
		Condicion c2 = new Condicion(3, "TRUE");

		Complejo com = new Complejo();
		com.addS(c1);
		com.addS(c2);

		System.out.println("Complejo:" + com.toString(datos));

		Regla reg = new Regla(0);

        System.out.println("Regla :" + reg.toString(datos));

		reg.addC(com);

        System.out.println("Regla :" + reg.toString(datos));

		for (Instance i : datos) {
			if (reg.evalR(i) >= 0) {
				System.out.println("La regla es cierta para :" + i);
				System.out.println("Y el valor que regresa es " + 
					datos.attribute(datos.classIndex()).value(reg.cons));
			}
			
		}

	}

}
