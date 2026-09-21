package co.edu.udea.lab1.cli;

import co.edu.udea.lab1.model.User;
import co.edu.udea.lab1.service.UserBenchmarkResult;
import co.edu.udea.lab1.service.UserBenchmarkService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final UserBenchmarkService benchmarkService;

    public ConsoleRunner(UserBenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("\n======================================================================");
        System.out.println("   LABORATORIO 1: EXTENDIBLE HASHING VS BÚSQUEDA SECUENCIAL");
        System.out.println("   Universidad de Antioquia - Estructura de Datos");
        System.out.println("======================================================================");

        while (!exit) {
            printMenu();
            System.out.print("Ingrese el número de la opción deseada: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleRegisterUser(scanner);
                case "2" -> handleSearchUser(scanner);
                case "3" -> handleBulkLoad(scanner);
                case "4" -> handleInspectStructure();
                case "5" -> handleListUsers();
                case "6" -> {
                    System.out.println("\n¡Gracias por usar el sistema! Saliendo...");
                    exit = true;
                }
                default -> System.out.println("\n[ERROR] Opción inválida. Por favor intente de nuevo.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n----------------------------------------------------------------------");
        System.out.printf(" MENÚ PRINCIPAL (Usuarios registrados: %d)%n", benchmarkService.getTotalUsers());
        System.out.println("----------------------------------------------------------------------");
        System.out.println("1. Registrar nuevo usuario (Nombre, CC, Correo)");
        System.out.println("2. Buscar usuario por Cédula (Comparar tiempos de búsqueda)");
        System.out.println("3. Cargar lote masivo de usuarios de prueba (Faker sintético)");
        System.out.println("4. Inspeccionar estructura de Extendible Hashing (Directorios/Buckets)");
        System.out.println("5. Listar usuarios almacenados");
        System.out.println("6. Salir");
        System.out.println("----------------------------------------------------------------------");
    }

    private void handleRegisterUser(Scanner scanner) {
        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
        System.out.print("Ingrese Cédula (CC): ");
        String cc = scanner.nextLine().trim();

        if (cc.isEmpty()) {
            System.out.println("[ERROR] La cédula no puede estar vacía.");
            return;
        }

        System.out.print("Ingrese Nombre completo: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Ingrese Correo electrónico: ");
        String email = scanner.nextLine().trim();

        try {
            User user = new User(cc, nombre, email);
            benchmarkService.registerUser(user);
            System.out.println("\n[ÉXITO] Usuario registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERROR RESTRICCIÓN] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERROR INESPERADO] " + e.getMessage());
        }
    }

    private void handleSearchUser(Scanner scanner) {
        System.out.println("\n--- BÚSQUEDA Y COMPARACIÓN DE TIEMPOS DE EJECUCIÓN ---");
        System.out.print("Ingrese Cédula (CC) a buscar: ");
        String cc = scanner.nextLine().trim();

        if (cc.isEmpty()) {
            System.out.println("[ERROR] La cédula no puede estar vacía.");
            return;
        }

        UserBenchmarkResult result = benchmarkService.searchUserWithBenchmark(cc);
        result.printFormattedResult();
    }

    private void handleBulkLoad(Scanner scanner) {
        System.out.println("\n--- CARGA MASIVA DE PRUEBA (BENCHMARKING) ---");
        System.out.print("Ingrese la cantidad de usuarios a generar (ej: 1000, 10000, 50000): ");
        String countStr = scanner.nextLine().trim();

        try {
            int count = Integer.parseInt(countStr);
            if (count <= 0) {
                System.out.println("[ERROR] La cantidad debe ser un entero positivo.");
                return;
            }

            System.out.println("Generando " + count + " usuarios sintéticos y actualizando estructuras...");
            long start = System.currentTimeMillis();
            int added = benchmarkService.bulkLoadUsers(count);
            long duration = System.currentTimeMillis() - start;

            System.out.printf("[ÉXITO] Se registraron %d usuarios exitosamente en %d ms.%n", added, duration);
            System.out.printf("Total actual en el sistema: %d usuarios.%n", benchmarkService.getTotalUsers());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Ingrese un número entero válido.");
        }
    }

    private void handleInspectStructure() {
        benchmarkService.getExtendibleHashTable().printStructure();
    }

    private void handleListUsers() {
        System.out.println("\n--- LISTA DE USUARIOS REGISTRADOS ---");
        var users = benchmarkService.getSequentialStorage().getUsers();
        if (users.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
            return;
        }

        int maxShow = Math.min(users.size(), 20);
        System.out.printf("Mostrando primeros %d de %d usuarios:%n", maxShow, users.size());
        for (int i = 0; i < maxShow; i++) {
            System.out.printf("%d. %s%n", i + 1, users.get(i));
        }
        if (users.size() > maxShow) {
            System.out.printf("... y %d usuarios más.%n", users.size() - maxShow);
        }
    }
}
