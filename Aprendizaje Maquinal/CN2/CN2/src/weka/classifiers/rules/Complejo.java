package weka.classifiers.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Complejo implements Serializable, Iterator<Condicion>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Condicion> comp;
	
	public Complejo() {
		// Complejo vacio
		comp = new ArrayList<Condicion>();
	}
	
	public void addS(Condicion s) throws Exception {
		comp.add(s);
	}
	
	public boolean evalC(Instance ins) {
		// El complejo es verdad si todos los Condiciones
		// son verdad
		boolean res = (comp.size() == 0) ? false : true; 
		// Asume si, cuando hay al menos una condición
		
		for(Condicion s:comp) {
			res &= s.esAplicable(ins);
		}
		
		return res;
	}
	
	public ArrayList<Instance> cobertura(Instances data) {
		ArrayList<Instance> res = new ArrayList<Instance>();
		
		for(Instance i:data) {
			if (evalC(i)) {
				res.add(i);
			}
		}
		
		return res;
	}

	    /**
     * Calcula la entropía de la condición basada en la clase del primer ejemplo de la cobertura.
     *
     * @param cobertura Lista de ejemplos cubiertos.
     * @return Valor de la entropía.
     */
    public double calcularEntropia(List<Instance> cobertura) {
        if (cobertura.isEmpty()) return 0.0;

        int clasIdx = cobertura.get(0).classIndex();
		int totClas = cobertura.get(0).attribute(clasIdx).numValues();
		int clases[] = new int[totClas];

        for (Instance instancia : cobertura) {
            int clase = (int) instancia.value(clasIdx);
			clases[clase]++;
        }

        
        // Fórmula de entropía
        double entropia = 0.0;

		double tot = cobertura.size();

		for (int i = 0; i < clases.length; i++) {
			if (clases[i] > 0) {
				entropia -= clases[i]/tot*(Math.log(clases[i]/tot)/Math.log(totClas));
			}
		}   

        return entropia;
    }
	
	public boolean isEmpty() {
		return comp.size() == 0;
	}

	@Override
	public boolean hasNext() {
		// El de comp
		return comp.iterator().hasNext();
	}

	@Override
	public Condicion next() {
		// El de comp
		return comp.iterator().next();
	}
	
	public Iterator<Condicion> iterator() {
		return comp.iterator();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String res = "";
		Iterator<Condicion> it = comp.iterator();
		while (it.hasNext()) {
			res += it.next().toString();
			res += (it.hasNext()) ? " and " : "";
		}
		
		return res;
	}

	public String toString(Instances data) {
		String res = "";
		Iterator<Condicion> it = comp.iterator();
		while (it.hasNext()) {
			res += it.next().toString(data);
			res += (it.hasNext()) ? " and " : "";
		}
		
		return res;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// Haz un clone
		Complejo n = new Complejo();
		
		n.comp = new ArrayList<Condicion>(comp);
		
		return n;
	}

	public static void main(String[] args) throws Exception{
        // Lee datos de tenis
        DataSource source = new DataSource("weather.nominal.arff");
        Instances datos = source.getDataSet();

        // Pon clase
        datos.setClassIndex(4);

        // Imprimelos 
        // System.out.println(datos);

        Condicion c1 = new Condicion(0, "sunny");
		Condicion c2 = new Condicion(2, "high");

		Complejo com = new Complejo();
		com.addS(c1);
		com.addS(c2);

		System.out.println("Complejo:" + com.toString(datos));

        List<Instance> cob = com.cobertura(datos);

        System.out.println(cob);

        double ent = com.calcularEntropia(cob);

        System.out.println(ent);
    }
}
