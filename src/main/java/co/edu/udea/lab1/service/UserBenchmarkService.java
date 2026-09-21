package co.edu.udea.lab1.service;

import co.edu.udea.lab1.hashing.ExtendibleHashTable;
import co.edu.udea.lab1.model.User;
import co.edu.udea.lab1.search.SequentialStorage;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserBenchmarkService {

    private final ExtendibleHashTable extendibleHashTable;
    private final SequentialStorage sequentialStorage;
    private final Random random;

    public UserBenchmarkService() {
        this.extendibleHashTable = new ExtendibleHashTable(1, 4);
        this.sequentialStorage = new SequentialStorage();
        this.random = new Random();
    }

    public synchronized void registerUser(User user) {
        if (extendibleHashTable.contains(user.getCedula())) {
            throw new IllegalArgumentException("Cédula duplicada: La cédula '" + user.getCedula() + "' ya existe en el sistema.");
        }
        extendibleHashTable.insert(user);
        sequentialStorage.insert(user);
    }

    public UserBenchmarkResult searchUserWithBenchmark(String cedula) {
        String key = cedula.trim();

        // 1. Cronometrar Extendible Hashing (Dynamic Hash Table)
        long startHash = System.nanoTime();
        User hashUser = extendibleHashTable.search(key);
        long endHash = System.nanoTime();
        long hashTimeNano = endHash - startHash;

        // 2. Cronometrar Búsqueda Secuencial (Linear Scan)
        long startSeq = System.nanoTime();
        User seqUser = sequentialStorage.search(key);
        long endSeq = System.nanoTime();
        long seqTimeNano = endSeq - startSeq;

        return new UserBenchmarkResult(hashUser, hashTimeNano, seqTimeNano);
    }

    public int bulkLoadUsers(int count) {
        // Usar nombres limpios sin tildes para evitar problemas de codificación según la consola del SO
        String[] nombresEjemplo = {"Juan", "Maria", "Carlos", "Ana", "Luis", "Sofia", "Diego", "Valeria", "Andres", "Camila", "Mateo", "Isabella", "Gabriel", "Lucia"};
        String[] apellidosEjemplo = {"Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Torres", "Vargas", "Rios", "Silva", "Mendoza", "Castillo"};
        String[] dominios = {"gmail.com", "udea.edu.co", "yahoo.com", "outlook.com", "hotmail.com"};

        int added = 0;
        int attempts = 0;

        while (added < count && attempts < count * 10) {
            attempts++;
            long ccNum = 10000000L + (long)(random.nextDouble() * 900000000L);
            String cc = String.valueOf(ccNum);

            if (extendibleHashTable.contains(cc)) {
                continue;
            }

            String nombre = nombresEjemplo[random.nextInt(nombresEjemplo.length)] + " " + apellidosEjemplo[random.nextInt(apellidosEjemplo.length)];
            String email = nombre.toLowerCase().replace(" ", ".") + random.nextInt(999) + "@" + dominios[random.nextInt(dominios.length)];

            User user = new User(cc, nombre, email);
            extendibleHashTable.insert(user);
            sequentialStorage.insert(user);
            added++;
        }
        return added;
    }

    public ExtendibleHashTable getExtendibleHashTable() {
        return extendibleHashTable;
    }

    public SequentialStorage getSequentialStorage() {
        return sequentialStorage;
    }

    public int getTotalUsers() {
        return extendibleHashTable.getTotalElements();
    }
}
