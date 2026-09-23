package Vista;

import Controlador.ControladorParticipante;
import Modelo.Clases.Participante;

import java.util.List;
import java.util.Scanner;

public class JavaDatabaseClase {

    public static void main(String[] args) {
        ControladorParticipante controlador = new ControladorParticipante();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENU INSCRIPCIONES WEBINAR =====");
            System.out.println("1. Inscribir participante");
            System.out.println("2. Listar participantes");
            System.out.println("3. Buscar participantes por empresa");
            System.out.println("4. Contar participantes");
            System.out.println("5. Eliminar participante por id");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Correo: ");
                    String correo = sc.nextLine();
                    System.out.print("Empresa: ");
                    String empresa = sc.nextLine();
                    System.out.println(controlador.inscribir(nombre, correo, empresa));
                    break;

                case 2:
                    List<Participante> lista = controlador.listar();
                    if (lista.isEmpty()) {
                        System.out.println("No hay participantes inscritos.");
                    } else {
                        for (Participante p : lista) {
                            System.out.println(p);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Empresa a buscar: ");
                    String empresaBuscar = sc.nextLine();
                    List<Participante> resultados = controlador.buscarPorEmpresa(empresaBuscar);
                    if (resultados.isEmpty()) {
                        System.out.println("No hay participantes de esa empresa.");
                    } else {
                        for (Participante p : resultados) {
                            System.out.println(p);
                        }
                    }
                    break;

                case 4:
                    System.out.println("Total de participantes inscritos: " + controlador.contar());
                    break;

                case 5:
                    System.out.print("Id del participante a eliminar: ");
                    int id = leerEntero(sc);
                    System.out.println(controlador.eliminar(id));
                    break;

                case 6:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 6);

        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }
}