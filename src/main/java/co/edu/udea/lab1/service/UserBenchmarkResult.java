package co.edu.udea.lab1.service;

import co.edu.udea.lab1.model.User;

public record UserBenchmarkResult(
        User user,
        long hashingTimeNano,
        long sequentialTimeNano
) {
    public double getHashingTimeMs() {
        return hashingTimeNano / 1_000_000.0;
    }

    public double getSequentialTimeMs() {
        return sequentialTimeNano / 1_000_000.0;
    }

    public void printFormattedResult() {
        if (user == null) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("RESULTADO: Usuario NO encontrado.");
            System.out.printf("Tiempo búsqueda (Hashing): %.4f ms (%d ns)%n", getHashingTimeMs(), hashingTimeNano);
            System.out.printf("Tiempo búsqueda (Secuencial): %.4f ms (%d ns)%n", getSequentialTimeMs(), sequentialTimeNano);
            System.out.println("-------------------------------------------------");
            return;
        }

        System.out.println("\n-------------------------------------------------");
        System.out.println("Usuario encontrado:");
        System.out.println("Nombre: " + user.getNombre());
        System.out.println("CC: " + user.getCedula());
        System.out.println("Correo: " + user.getEmail());
        System.out.println();
        System.out.printf("Tiempo búsqueda (Hashing): %.4f ms (%d ns)%n", getHashingTimeMs(), hashingTimeNano);
        System.out.printf("Tiempo búsqueda (Secuencial): %.4f ms (%d ns)%n", getSequentialTimeMs(), sequentialTimeNano);
        System.out.println("-------------------------------------------------");
    }
}
