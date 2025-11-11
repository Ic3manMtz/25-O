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
 * @author Jorge Rafael Martínez Buenrostro
 * Se debe comentar el código
 */
public class CN2 extends AbstractClassifier {
    
    private ListaReglas listaReglas;
    private static final long serialVersionUID = 1789045553777829235L;

    /**
     * Construye el clasificador generando reglas a partir de los datos
     * 
     * @param datos Conjunto de entrenamiento
     */
    @Override
    public void buildClassifier(Instances datos) throws Exception {
        listaReglas = new ListaReglas();
        
        // Crear copia de los datos para ir eliminando instancias cubiertas
        Instances datosRestantes = new Instances(datos);
        
        // Mientras queden instancias por cubrir
        while (!datosRestantes.isEmpty() && datosRestantes.numInstances() > 0) {
            // Encontrar el mejor complejo usando búsqueda beam
            Complejo mejorComplejo = encontrarMejorComplejo(datosRestantes);
            
            if (mejorComplejo == null) break;
            
            // Obtener cobertura del mejor complejo
            ArrayList<Instance> cobertura = mejorComplejo.cobertura(datosRestantes);
            
            if (cobertura.isEmpty()) break;
            
            // Determinar la clase mayoritaria en la cobertura
            int claseMayoritaria = obtenerClaseMayoritaria(cobertura, datosRestantes);
            
            // Crear y agregar la regla
            Regla nuevaRegla = new Regla(mejorComplejo, claseMayoritaria);
            listaReglas.addR(nuevaRegla);
            
            // Eliminar instancias cubiertas
            datosRestantes = eliminarInstanciasCubiertas(datosRestantes, mejorComplejo);
        }
        
        // Agregar regla por defecto (clase mayoritaria de todo el dataset)
        ArrayList<Instance> todasInstancias = new ArrayList<Instance>();
        for (int i = 0; i < datos.numInstances(); i++) {
            todasInstancias.add(datos.instance(i));
        }
        int clasePorDefecto = obtenerClaseMayoritaria(todasInstancias, datos);
        Regla reglaDefecto = new Regla(clasePorDefecto);
        listaReglas.addR(reglaDefecto);
    }

    /**
     * Clasifica una instancia usando las reglas generadas
     * 
     * @param ejemplo Instancia a clasificar
     * @return Clase predicha
     */
    @Override
    public double classifyInstance(Instance ejemplo) throws Exception {
        if (listaReglas == null) {
            throw new Exception("Classifier has not been built yet");
        }
        int resultado = listaReglas.evalR(ejemplo);
        return (resultado >= 0) ? resultado : 0;
    }
    
    /**
     * Encuentra el mejor complejo usando búsqueda beam
     */
    private Complejo encontrarMejorComplejo(Instances datos) throws Exception {
        ArrayList<Complejo> complejosActuales = new ArrayList<Complejo>();
        Complejo mejorComplejo = null;
        double mejorEntropia = Double.MAX_VALUE;
        
        // Inicializar con complejos de una condición
        for (int i = 0; i < datos.numAttributes(); i++) {
            if (i == datos.classIndex()) continue;
            
            Attribute atributo = datos.attribute(i);
            for (int j = 0; j < atributo.numValues(); j++) {
                Condicion condicion = new Condicion(i, atributo.value(j));
                Complejo complejo = new Complejo();
                complejo.addS(condicion);
                complejosActuales.add(complejo);
            }
        }
        
        // Búsqueda beam (simplificada)
        int beamWidth = 5;
        boolean encontradoMejor = true;
        int maxIteraciones = 10;
        int iteracion = 0;
        
        while (encontradoMejor && !complejosActuales.isEmpty() && iteracion < maxIteraciones) {
            iteracion++;
            encontradoMejor = false;
            ArrayList<Complejo> nuevosComplejos = new ArrayList<Complejo>();
            
            for (Complejo complejo : complejosActuales) {
                ArrayList<Instance> cobertura = complejo.cobertura(datos);
                
                if (!cobertura.isEmpty()) {
                    double entropia = complejo.calcularEntropia(cobertura);
                    
                    if (entropia < mejorEntropia) {
                        mejorEntropia = entropia;
                        mejorComplejo = complejo;
                        encontradoMejor = true;
                    }
                }
                
                // Generar especializaciones
                ArrayList<Complejo> especializaciones = generarEspecializaciones(complejo, datos);
                nuevosComplejos.addAll(especializaciones);
            }
            
            // Seleccionar los mejores para el siguiente paso
            complejosActuales = seleccionarMejoresComplejos(nuevosComplejos, datos, beamWidth);
        }
        
        return mejorComplejo;
    }
    
    /**
     * Genera especializaciones de un complejo añadiendo condiciones
     */
    private ArrayList<Complejo> generarEspecializaciones(Complejo base, Instances datos) throws Exception {
        ArrayList<Complejo> especializaciones = new ArrayList<Complejo>();
        
        for (int i = 0; i < datos.numAttributes(); i++) {
            if (i == datos.classIndex()) continue;
            
            Attribute atributo = datos.attribute(i);
            for (int j = 0; j < atributo.numValues(); j++) {
                // Verificar si el atributo ya está en el complejo
                boolean atributoYaPresente = false;
                for (Condicion cond : base.comp) {
                    if (cond.getAtributoIndex() == i) {
                        atributoYaPresente = true;
                        break;
                    }
                }
                
                if (!atributoYaPresente) {
                    Condicion nuevaCondicion = new Condicion(i, atributo.value(j));
                    Complejo especializacion = new Complejo();
                    // Copiar condiciones existentes
                    for (Condicion condExistente : base.comp) {
                        especializacion.addS(condExistente);
                    }
                    // Añadir nueva condición
                    especializacion.addS(nuevaCondicion);
                    especializaciones.add(especializacion);
                }
            }
        }
        
        return especializaciones;
    }
    
    /**
     * Selecciona los mejores complejos según entropía
     */
	private ArrayList<Complejo> seleccionarMejoresComplejos(ArrayList<Complejo> complejos, 
                                                           Instances datos, int beamWidth) {
        ArrayList<Complejo> complejosValidos = new ArrayList<Complejo>();
        
        // Filtrar complejos con cobertura no vacía
        for (Complejo complejo : complejos) {
            ArrayList<Instance> cobertura = complejo.cobertura(datos);
            if (!cobertura.isEmpty()) {
                complejosValidos.add(complejo);
            }
        }
        
        // Ordenar manualmente por entropía 
        for (int i = 0; i < complejosValidos.size() - 1; i++) {
            for (int j = i + 1; j < complejosValidos.size(); j++) {
                try {
                    Complejo c1 = complejosValidos.get(i);
                    Complejo c2 = complejosValidos.get(j);
                    
                    double entropia1 = c1.calcularEntropia(c1.cobertura(datos));
                    double entropia2 = c2.calcularEntropia(c2.cobertura(datos));
                    
                    if (entropia1 > entropia2) {
                        // Intercambiar posiciones
                        Complejo temp = complejosValidos.get(i);
                        complejosValidos.set(i, complejosValidos.get(j));
                        complejosValidos.set(j, temp);
                    }
                } catch (Exception e) {
                    // En caso de error, mantener el orden actual
                }
            }
        }
        
        // Tomar los mejores (menor entropía)
        ArrayList<Complejo> mejores = new ArrayList<Complejo>();
        for (int i = 0; i < Math.min(beamWidth, complejosValidos.size()); i++) {
            mejores.add(complejosValidos.get(i));
        }
        
        return mejores;
    }
    
    /**
     * Obtiene la clase mayoritaria de un conjunto de instancias
     */
    private int obtenerClaseMayoritaria(ArrayList<Instance> instancias, Instances datos) {
        if (instancias.isEmpty()) {
            return 0; 
        }
        
        int numClases = datos.classAttribute().numValues();
        int[] conteoClases = new int[numClases];
        
        for (Instance instancia : instancias) {
            int clase = (int) instancia.classValue();
            conteoClases[clase]++;
        }
        
        int claseMayoritaria = 0;
        int maxConteo = conteoClases[0];
        for (int i = 1; i < numClases; i++) {
            if (conteoClases[i] > maxConteo) {
                maxConteo = conteoClases[i];
                claseMayoritaria = i;
            }
        }
        
        return claseMayoritaria;
    }
    
    /**
     * Elimina instancias cubiertas por un complejo
     */
    private Instances eliminarInstanciasCubiertas(Instances datos, Complejo complejo) {
        Instances nuevosDatos = new Instances(datos);
        nuevosDatos.clear();
        
        for (Instance instancia : datos) {
            if (!complejo.evalC(instancia)) {
                nuevosDatos.add(instancia);
            }
        }
        
        return nuevosDatos;
    }
    
    /**
     * Retorna la representación en string del clasificador
     */
    @Override
    public String toString() {
        if (listaReglas == null) {
            return "CN2 Classifier (not built yet)";
        }
        return "CN2 Rules Classifier\n" + listaReglas.toString();
    }
    
    /**
     * Método main para pruebas
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
        
        File archivo = new File("weather.nominal.arff");
        System.out.println("Buscando archivo en: " + archivo.getAbsolutePath());
        System.out.println("¿Existe el archivo? " + archivo.exists());
        
        if (archivo.exists()) {
            CN2 cn2 = new CN2();
            DataSource source = new DataSource("weather.nominal.arff");
            Instances datos = source.getDataSet();
            
            // Establecer índice de clase (último atributo)
            datos.setClassIndex(datos.numAttributes() - 1);
            
            System.out.println("Datos cargados correctamente: " + datos.numInstances() + " instancias");
            System.out.println(datos);
            
            // Construir clasificador
            cn2.buildClassifier(datos);
            
            // Mostrar reglas generadas
            System.out.println("\n=== REGLAS GENERADAS ===");
            System.out.println(cn2.toString());
            
            // Probar clasificación
            System.out.println("\n=== PRUEBAS DE CLASIFICACIÓN ===");
            for (int i = 0; i < Math.min(5, datos.numInstances()); i++) {
                Instance instancia = datos.instance(i);
                double prediccion = cn2.classifyInstance(instancia);
                String claseReal = datos.classAttribute().value((int) instancia.classValue());
                String clasePredicha = datos.classAttribute().value((int) prediccion);
                System.out.println("Instancia " + i + ": Real=" + claseReal + ", Predicha=" + clasePredicha);
            }
        } else {
            System.err.println("ERROR: Archivo no encontrado. Coloca 'weather.nominal.arff' en: " + System.getProperty("user.dir"));
        }
    }
}