package Perisistencia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Actividades.Actividad;
import Consola.ConsolaEstudiante;
import LearningPaths.Feedback;
import LearningPaths.LearningPath;
import LearningPaths.Progreso;
import Usuarios.Profesor;
 
public class PersistenciaLearningPaths implements IpersistenciaLearningPaths {
	
	public List<LearningPath> cargarLearningPath(String archivo) throws Exception {
	   
	    String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
	    JSONArray learningPaths = new JSONArray(jsonCompleto);
	    ArrayList<LearningPath> lista = new ArrayList<LearningPath>();

	    for (int i = 0; i < learningPaths.length(); i++) {
	        JSONObject learningPath = learningPaths.getJSONObject(i);
	        String LearningPathID = learningPath.get("LearningPathID").toString();
	        String titulo = learningPath.get("titulo").toString();
	        String descripcion = learningPath.get("descripcion").toString();
	        String objetivos = learningPath.get("objetivos").toString();
	        int nivelDificultad = learningPath.getInt("nivelDificultad");
	        int duracion = learningPath.getInt("duracion");
	        String profesorID = learningPath.get("profesorID").toString();

	        // Convertir JSONArray de actividadesID a List<String>
	        JSONArray actividadesArray = learningPath.getJSONArray("actividadesID");
	        List<String> actividadesID = new ArrayList<>();
	        for (int j = 0; j < actividadesArray.length(); j++) {
	            actividadesID.add(actividadesArray.getString(j));
	        }

	        // Convertir JSONArray de intereses a List<String>
	        JSONArray interesesArray = learningPath.getJSONArray("intereses");
	        List<String> intereses = new ArrayList<>();
	        for (int j = 0; j < interesesArray.length(); j++) {
	            intereses.add(interesesArray.getString(j));
	        }

	        // Crear el objeto LearningPath
	        LearningPath nuevoLearningPath = new LearningPath(LearningPathID, titulo, descripcion,
	                objetivos, nivelDificultad, duracion, profesorID, actividadesID, intereses);

	        lista.add(nuevoLearningPath);
	    }
	    return lista;
	}


	public void actualizarLearningPathEstudiante(LearningPath lp) throws Exception {
		List<LearningPath> lps = cargarLearningPath(ConsolaEstudiante.getLearningPathsFile());
		boolean found = false;
		for (int i = 0; i < lps.size(); i++) {
			if (lps.get(i).getLearningPathID().equals(lp.getLearningPathID())) {
				lps.set(i, lps.get(i));
				found = true;
				break;
			}
		}
		if (!found) {
			lps.add(lp);
		}
		salvarListaLearningPaths(lps);
	}

	public void salvarListaLearningPaths(List<LearningPath> lps) throws Exception {
		JSONArray jsonArray = new JSONArray();
		for (LearningPath lp : lps) {
			jsonArray.put(lp.toJSON());
		}
		try{
			Files.write(Paths.get(ConsolaEstudiante.getLearningPathsFile()), jsonArray.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	public void salvarLearningPath(String archivo, String LearningPathID, String titulo, String descripcion, String objetivos,
			int nivelDificultad, int duracion, String profesorID, List<String> actividadesID, List<String> intereses ) {
		
		String content;
		try {
			content = new String(Files.readAllBytes(Paths.get(archivo)));
			JSONArray jsonArray = new JSONArray(content);
	        JSONObject newObject = new JSONObject();
	        
	        newObject.put("LearningPathID", LearningPathID);
	        newObject.put("titulo", titulo);
	        newObject.put("descripcion", descripcion);
	        newObject.put("objetivos", objetivos);
	        newObject.put("nivelDificultad", nivelDificultad);
	        newObject.put("duracion", duracion);
	        newObject.put("profesorID", profesorID);
	        newObject.put("actividadesID", actividadesID);
	        newObject.put("intereses", intereses);
	        jsonArray.put(newObject);
	        Files.write(Paths.get(archivo), jsonArray.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
	}
	
	public void cargarActividadesDelLearningPath(List<Actividad> actividades, List<LearningPath> learningpaths) {
		for (LearningPath lp: learningpaths) {
			Map<String, Actividad> ActividadeslpActual = new HashMap<>();
			
			for (Actividad actividad : actividades) {
				for (String id : lp.getActividadesID()) {
					if (actividad.getActividadID().equals(id)) {
						ActividadeslpActual.put(actividad.getActividadID(), actividad);
					}
				}
			
			}
			
			lp.setActividades(ActividadeslpActual);

		}
	}
	
	public void cargarProgresoDelLearningPath(List<Progreso >listaProgreso, List<LearningPath> listaLearningPaths) {
        for (LearningPath lp: listaLearningPaths) {
            Progreso progresoActual = null;
            
            for (Progreso progreso : listaProgreso) {
                if (progreso.getLearningPath().equals(lp.getLearningPathID())) {
                    progresoActual=progreso;
                }
            }
            
            lp.setProgreso(progresoActual);

        }
    }


	public void cargarFeedbackDelLearningPath(List<Feedback> feedbacks, List<LearningPath> learningPaths) {
		for (LearningPath lp: learningPaths) {
            List<Feedback> feedbacksActual = new ArrayList<>();
            
            for (Feedback feedback : feedbacks) {
            	
                if (feedback.getLearningPath().equals(lp.getLearningPathID())) {
             
                    feedbacksActual.add(feedback);
                }
            }
            
            lp.setFeedback(feedbacksActual);
            
        }
    
		
	}
	
	

}