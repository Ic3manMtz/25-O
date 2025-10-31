/**
 * 
 */
package weka.classifiers.rules;

import java.io.File;
import java.util.ArrayList;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * @author alumno
 * Se debe comentar el código
 */
public class CN2 extends AbstractClassifier {
	
	ListaReglas listaReglas = new ListaReglas();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1789045553777829235L;

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	/**
	 * @param datos
	 */
	@Override
	public void buildClassifier(Instances datos) throws Exception {
		// TODO Crea una lista de reglas
		ArrayList<Complejo> complejos = new ArrayList<Complejo>();

		Condicion condicion;
		for(int i=0; i<datos.numAttributes();i++){
			Attribute atributo = datos.attribute(i);
			for(int j=0; j<datos.attribute(i).numValues() ;j++){
				condicion = new Condicion(i, atributo.value(j));
				System.out.println(condicion.toString());
				
				Complejo complejo = new Complejo();
				complejo.addS(condicion);
				System.out.println(complejo.toString(datos));
				complejos.add(complejo);
			}
		}

		//Encontrar el mejor complejo
		double menorEntropia=1.0;
		Complejo mejorComplejo=complejos.get(0);
		for(Complejo complejo : complejos){
			ArrayList<Instance> cobertura = complejo.cobertura(datos);
			double entropia = complejo.calcularEntropia(cobertura);
			if(entropia<menorEntropia){
				menorEntropia=entropia;
				mejorComplejo=complejo;
			}
		}

		System.out.println("Mejor complejo: "+mejorComplejo.toString(datos)+" con entropia: "+menorEntropia);

		//Clase mayoritaria, no 0
		Regla regla1 = new Regla(mejorComplejo,0);
		System.out.println("Regla creada: "+regla1.toString(datos));

		listaReglas.addR(regla1);

		//Elimina los elementos de la cobertura
		Instances nuevosDatos = new Instances(datos);
		nuevosDatos.clear();
		for(Instance instancia : datos){
			if(mejorComplejo.evalC(instancia)==false){
				nuevosDatos.add(instancia);
			}
		}

		System.out.println(nuevosDatos);

		for(int i=0; i<nuevosDatos.numAttributes();i++){
			if(i!=nuevosDatos.classIndex()){
				Attribute atributo = nuevosDatos.attribute(i);
				for(int j=0; j<nuevosDatos.attribute(i).numValues() ;j++){
					condicion = new Condicion(i, atributo.value(j));	
					Complejo complejo = new Complejo();
					complejo.addS(condicion);
					complejos.add(complejo);
				}
			}
		}

		//Encontrar el mejor complejo
		menorEntropia=1.0;
		double entropia;
		for(Complejo complejo : complejos){
			ArrayList<Instance> cobertura = complejo.cobertura(nuevosDatos);
			if(cobertura.isEmpty()){
				entropia=1.0;
			}else{
				entropia = complejo.calcularEntropia(cobertura);
			}
			System.out.println("Complejo: "+complejo.toString()+" Entropia: "+entropia);
			if(entropia<menorEntropia){
				menorEntropia=entropia;
				mejorComplejo=complejo;
			}
		}

		System.out.println("Mejor complejo: "+mejorComplejo.toString(nuevosDatos)+" con entropia: "+menorEntropia);

		Regla regla2 = new Regla(mejorComplejo,1);
		System.out.println("Regla creada: "+regla2.toString(datos));

		listaReglas.addR(regla2);

		Instances nuevosDatos1 = new Instances(nuevosDatos);
		nuevosDatos1.clear();
		for(Instance instancia : datos){
			if(mejorComplejo.evalC(instancia)==false){
				nuevosDatos1.add(instancia);
			}
		}

		System.out.println(nuevosDatos1);

				for(int i=0; i<nuevosDatos1.numAttributes();i++){
			if(i!=nuevosDatos1.classIndex()){
				Attribute atributo = nuevosDatos1.attribute(i);
				for(int j=0; j<nuevosDatos1.attribute(i).numValues() ;j++){
					condicion = new Condicion(i, atributo.value(j));	
					Complejo complejo = new Complejo();
					complejo.addS(condicion);
					complejos.add(complejo);
				}
			}
		}

		//Encontrar el mejor complejo
		menorEntropia=1.0;
		for(Complejo complejo : complejos){
			ArrayList<Instance> cobertura = complejo.cobertura(nuevosDatos1);
			if(cobertura.isEmpty()){
				entropia=1.0;
			}else{
				entropia = complejo.calcularEntropia(cobertura);
			}
			System.out.println("Complejo: "+complejo.toString()+" Entropia: "+entropia);
			if(entropia<menorEntropia){
				menorEntropia=entropia;
				mejorComplejo=complejo;
			}
		}

		System.out.println("Mejor complejo: "+mejorComplejo.toString(nuevosDatos1)+" con entropia: "+menorEntropia);

	}

	/**
	 * @param ejemplo
	 */
	@Override
	public double classifyInstance(Instance ejemplo) throws Exception {
		// TODO Dada una lista de reglas clasifica un ejemplo
		return 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// TODO Escribe las reglas encontradas
		return ("Las reglas son:");
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		// Lee datos de tenis
		System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
    
    File archivo = new File("weather.nominal.arff");
    System.out.println("Buscando archivo en: " + archivo.getAbsolutePath());
    System.out.println("¿Existe el archivo? " + archivo.exists());
    
    if (archivo.exists()) {
				// Para pruebas
        CN2 cn2 = new CN2();
        DataSource source = new DataSource("weather.nominal.arff");
        Instances datos = source.getDataSet();
        System.out.println("Datos cargados correctamente: " + datos.numInstances() + " instancias");
				// Imprimelos 
				System.out.println(datos);
        cn2.buildClassifier(datos);
				// Clasifica el primero
        System.out.println(cn2.classifyInstance(datos.firstInstance()));
    } else {
        System.err.println("ERROR: Archivo no encontrado. Coloca 'weather.nominal.arff' en: " + System.getProperty("user.dir"));
    }

	}
}
