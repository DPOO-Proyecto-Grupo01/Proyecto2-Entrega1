
package Consola;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Actividades.Actividad;
import Actividades.Encuesta;
import Actividades.Examen;
import Actividades.Pregunta;
import Actividades.Quiz;
import Actividades.Tarea;
import Exceptions.ActividadNoPertenece;
import Exceptions.LearningPathNoInscrito;
import Exceptions.NombreRepetido;
import Exceptions.YaSeCompleto;
import LearningPaths.LearningPath;
import Perisistencia.PersistenciaActividades;
import Perisistencia.PersistenciaLearningPaths;
import Perisistencia.PersistenciaUsuarios;
import Usuarios.Estudiante;
import Usuarios.Profesor;
import Usuarios.Usuario;

public class ConsolaEstudiante {

    private static Scanner scanner = new Scanner(System.in);
    private static Estudiante estudianteActual;
    private static PersistenciaUsuarios persistenciaUsuarios = new PersistenciaUsuarios();
    private static PersistenciaActividades persistenciaActividades = new PersistenciaActividades();
    private static PersistenciaLearningPaths persistenciaLearningPaths = new PersistenciaLearningPaths();
    private static final String usuariosFile = "src/datos/users.json";
    private static final String actividadesFile = "src/datos/activities.json";
    private static final String learningPathsFile = "src/datos/learning_paths.json";
    private static List<LearningPath> learningPaths;
    private static Map<String, Profesor> profesores = new HashMap<>();
    private static List<Actividad> actividades;

    private static int contador = 1;

    public static void main(String[] args) throws NombreRepetido, LearningPathNoInscrito, ActividadNoPertenece, YaSeCompleto {
        cargarActividades();
        cargarLearningPaths();
        cargarProfesores();
        cargarLpProfesoress();
        cargarActividadesLP();
        cargarEstudiantes();

        for (Profesor profesor : profesores.values()) {
            System.out.println("El profesor: " + profesor.getUsuarioID() + " tiene estos lps: " + profesor.getLearningPathsCreados().keySet() + "\n");
        }

        boolean authenticated = false;

        if (iniciarSesion() == 1) {
            while (!authenticated) {
                if (authenticar()) {
                    authenticated = true;
                    menu();
                } else {
                    System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.\n");
                }
            }
        } else {
            boolean registered = false;
            while (!registered) {
                registrarse();
                System.out.println("Usuario registrado\n");
                System.out.println("Iniciar sesión\n");
                if (authenticar()) {
                    registered = true;
                    menu();
                } else {
                    System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.\n");
                }
            }
        }
    }

    private static void cargarEstudiantes() {
        List<Estudiante> estudiantes;
        try {
            estudiantes = persistenciaUsuarios.cargarEstudiantes(usuariosFile);
            System.out.println("Datos de estudiantes cargados correctamente\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarLearningPaths() {
        try {
            learningPaths = persistenciaLearningPaths.cargarLearningPath(learningPathsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarProfesores() {
        List<Profesor> profesoresLista;
        try {
            profesoresLista = persistenciaUsuarios.cargarProfesores(usuariosFile);
            for (Profesor profesor : profesoresLista) {
                profesores.put(profesor.getUsuarioID(), profesor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarActividades() {
        try {
            actividades = persistenciaActividades.cargarActividades(actividadesFile);
            System.out.println("Actividades cargadas correctamente: " + actividades.size() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarLpProfesoress() {
        try {
            persistenciaUsuarios.cargarLearningPathsProfesor(learningPaths, profesores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargarActividadesLP() {
        try {
            persistenciaLearningPaths.cargarActividadesDelLearningPath(actividades, learningPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int iniciarSesion() {
        System.out.println("1. Iniciar Sesion\n");
        System.out.println("2. Registrarse\n");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    private static void menu() throws NombreRepetido, LearningPathNoInscrito, ActividadNoPertenece, YaSeCompleto {
        int option;
        do {
            System.out.println("---- Estudiante Interface ----\n");
            System.out.println("1. Inscribirse a un Learning Path\n");
            System.out.println("2. Completar actividad\n");
            System.out.println("3. Ver progreso de un learningPath\n");
            System.out.println("4. Ver actividades por completar\n");
            System.out.println("5. Enviar Feedback\n");
            System.out.println("6. Salir\n");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            handleOption(option);
        } while (option != 6);
    }

    private static void handleOption(int option) throws NombreRepetido, LearningPathNoInscrito, ActividadNoPertenece, YaSeCompleto {
        switch (option) {
            case 1 -> mostrarRecomendacionesYInscribirLearningPath();
            case 2 -> completarActividad();
            case 3 -> verProgresoLearningPath();
            case 4 -> verActividadesPorCompletar();
            case 5 -> enviarFeedback();
            case 6 -> System.out.println("Saliendo...\n");
            default -> System.out.println("Opción inválida.\n");
        }
    }

	private static void registrarse() throws NombreRepetido {
	        
	    	System.out.print("Ingrese su usuario ID: ");
	        String usuarioID = scanner.nextLine();
	        System.out.print("Ingrese su nombre: ");
	        String nombre = scanner.nextLine();
	        System.out.print("Ingrese su contraseña: ");
	        String contrasena = scanner.nextLine();
	        System.out.print("Ingrese su email: ");
	        String email = scanner.nextLine();
	        System.out.print("Ingrese sus intereses academicos: ");
	        String intereses = scanner.nextLine();
	
	        Estudiante estudiante = new Estudiante(usuarioID, nombre, contrasena, email, "Estudiante");
	        estudiante.setIntereses(intereses);
	        persistenciaUsuarios.salvarEstudiante(usuariosFile, estudiante.getUsuarioID(), estudiante.getNombre(), estudiante.getContraseña(), estudiante.getEmail(), estudiante.getTipoUsuario());
	    }

    private static boolean authenticar() {
        System.out.print("UsuarioID: ");
        String usuarioID = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        List<Estudiante> estudiantes;
        try {
            estudiantes = persistenciaUsuarios.cargarEstudiantes(usuariosFile);
            for (Estudiante estudiante : estudiantes) {
                if (estudiante.getUsuarioID().equals(usuarioID) && estudiante.getContraseña().equals(contrasena)) {
                    estudianteActual = estudiante;
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void mostrarRecomendacionesYInscribirLearningPath() throws LearningPathNoInscrito {
        try {
            System.out.println("Lista de profesores: \n");
            for (Profesor profesor : profesores.values()) {
                System.out.println(profesor.getUsuarioID() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Ingrese el ID del profesor al que desea inscribir: ");
        String profesorID = scanner.nextLine();

        for (String profesor : profesores.keySet()) {
            if (profesor.equals(profesorID)) {
                System.out.println("Profesor encontrado\n");
                estudianteActual.inscribirProfesor(profesores.get(profesor), profesorID);
                break;
            }
        }

        System.out.print("Ingrese su intereses academicos: ");
        String intereses = scanner.nextLine();
        estudianteActual.setIntereses(intereses);
        String recomendaciones = estudianteActual.obtenerRecomendacion(intereses, profesorID);
        System.out.println(recomendaciones + "\n");

        System.out.print("Ingrese el ID del Learning Path al que desea inscribirse: ");
        String learningPathID = scanner.nextLine();

        estudianteActual.inscribirLearningPath(learningPathID, profesorID);
    }

    private static void completarActividad() throws ActividadNoPertenece, YaSeCompleto {
        boolean validInput = false;
        String learningPathID = "";
        String actividadID = "";

        while (!validInput) {
            System.out.print("Ingrese el ID del Learning Path en el que desea trabajar: ");
            learningPathID = scanner.nextLine();

            System.out.print("Ingrese el ID de la actividad que desea completar: ");
            actividadID = scanner.nextLine();

            LearningPath lp = estudianteActual.getLearningPathsInscritos().get(learningPathID);

			if (lp != null && lp.getActividades().containsKey(actividadID)) {
				Actividad actividad = lp.getActividades().get(actividadID);
				if (actividad != null) {
					validInput = true;

					// Check the type of the activity and print questions if applicable
					if (actividad instanceof Quiz) {
						List<Pregunta> preguntas = ((Quiz) actividad).getPreguntas();
						System.out.println("Preguntas de la actividad (Quiz):");
						for (Pregunta pregunta : preguntas) {
							System.out.println(pregunta.getPregunta());
							System.out.print("Ingrese su respuesta: ");
							String respuesta = scanner.nextLine();
							System.out.println("Su respuesta: " + respuesta + "\n");
						}
					} else if (actividad instanceof Examen) {
						List<Pregunta> preguntas = ((Examen) actividad).getPreguntas();
						System.out.println("Preguntas de la actividad (Examen):");
						for (Pregunta pregunta : preguntas) {
							System.out.println(pregunta.getPregunta());
							System.out.print("Ingrese su respuesta: ");
							String respuesta = scanner.nextLine();
							System.out.println("Su respuesta: " + respuesta + "\n");
						}
					} else if (actividad instanceof Tarea) {
						List<Pregunta> preguntas = ((Tarea) actividad).getPreguntas();
						System.out.println("Preguntas de la actividad (Tarea):");
						for (Pregunta pregunta : preguntas) {
							System.out.println(pregunta.getPregunta());
							System.out.print("Ingrese su respuesta: ");
							String respuesta = scanner.nextLine();
							System.out.println("Su respuesta: " + respuesta + "\n");
						}
					}
				}
			}
            
            if (lp != null && lp.getActividades().containsKey(actividadID)) {
                validInput = true;
            } else {
                System.out.println("Learning Path o Actividad no encontrados. Por favor, intente de nuevo.");
            }
        }
        
        

        estudianteActual.completarActividad(actividadID, learningPathID);
    }

    private static void verProgresoLearningPath() throws LearningPathNoInscrito {
        System.out.print("Ingrese el ID del Learning Path que desea revisar: ");
        String learningPathID = scanner.nextLine();
        System.out.println(estudianteActual.getProgresoLearningPath(learningPathID) + "\n");
    }

    private static void verActividadesPorCompletar() throws LearningPathNoInscrito {
        System.out.print("Ingrese el ID del Learning Path que desea revisar: ");
        String learningPathID = scanner.nextLine();
        System.out.println(estudianteActual.actividadesDisponibles(learningPathID) + "\n");
    }

    private static void enviarFeedback() throws LearningPathNoInscrito {
        System.out.print("Ingrese el ID del Learning Path que desea revisar: ");
        String learningPathID = scanner.nextLine();
        System.out.println("Ingrese su feedback: ");
        String feedback = scanner.nextLine();
        System.out.println("Ingrese su calificación: ");
        int calificacion = scanner.nextInt();
        String feedbackID = "F" + Integer.toString(contador);

        estudianteActual.enviarFeedback(learningPathID, feedback, calificacion, feedbackID);
    }

    private static void persistData() {
        persistenciaUsuarios.salvarEstudiante(usuariosFile, estudianteActual.getUsuarioID(), estudianteActual.getNombre(), estudianteActual.getContraseña(), estudianteActual.getEmail(), estudianteActual.getTipoUsuario());
    }
}
 