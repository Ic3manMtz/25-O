package weka.classifiers.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ListaReglas implements Serializable {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private ArrayList<Regla> reglas;
		
		ListaReglas() {
			reglas = new ArrayList<Regla>();
		}

		ListaReglas(Regla r) {
			reglas = new ArrayList<Regla>();
			reglas.add(r);
		}
		
		public void addR(Regla r) {
			reglas.add(r);
		}
		
		int evalR(Instance ins) {
			// Evalua hasta que case una
			int r=-1;
			Iterator<Regla> it=reglas.iterator();
			
			while (r < 0 && it.hasNext()) {
				Regla reg=it.next();
				r = reg.evalR(ins);
			}
			
			return r;
		}

		@Override
		public String toString() {
			// 
			String res = "";
			Iterator<Regla> it = reglas.iterator();
			
			while (it.hasNext()) {
				res += it.next().toString();
				res += "\n  ";
				}
			
			return res;
		}

		public String toString(Instances data) {
			// 
			String res = "";
			Iterator<Regla> it = reglas.iterator();
			
			while (it.hasNext()) {
				res += it.next().toString(data);
				res += "\n  ";
				}
			
			return res;
		}
	public static void main(String[] args) throws Exception{
        // Lee datos de tenis
        DataSource source = new DataSource("weather.nominal.arff");
        Instances datos = source.getDataSet();

        // Pon clase
        datos.setClassIndex(4);

        // Imprimelos 
        System.out.println(datos);

		Condicion c1 = new Condicion(0, "overcast");

        Condicion c2 = new Condicion(0, "sunny");
		Condicion c3 = new Condicion(3, "TRUE");

		Complejo com1 = new Complejo();
		com1.addS(c1);

		Complejo com2 = new Complejo();
		com2.addS(c2);
		com2.addS(c3);

		// if outlook = overcast then yes
		Regla reg1 = new Regla(com1, 0);
		// if outlook = sunny and windy = TRUE then no 
		Regla reg2 = new Regla(com2, 1);
		// no
		Regla no = new Regla(1);

		ListaReglas lreg = new ListaReglas(reg1);

		lreg.addR(reg2);
		lreg.addR(no);

		System.out.println("La lista es \n" + lreg.toString(datos));

		for (Instance i : datos) {
			System.out.println("Para " + i);
			System.out.println("   se obtiene :" 
				+ datos.attribute(datos.classIndex()).value(lreg.evalR(i)));
		}

	}

}
