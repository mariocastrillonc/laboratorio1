package co.edu.udea.lab1.hashing;

import co.edu.udea.lab1.model.User;

import java.util.*;

public class ExtendibleHashTable {
    private int globalDepth;
    private final int bucketCapacity;
    private final List<Bucket> directory;
    private int nextBucketId;
    private int totalElements;

    public ExtendibleHashTable(int initialGlobalDepth, int bucketCapacity) {
        if (initialGlobalDepth < 1) {
            throw new IllegalArgumentException("La profundidad global inicial debe ser al menos 1.");
        }
        if (bucketCapacity < 1) {
            throw new IllegalArgumentException("La capacidad del bucket debe ser al menos 1.");
        }
        this.globalDepth = initialGlobalDepth;
        this.bucketCapacity = bucketCapacity;
        this.directory = new ArrayList<>();
        this.nextBucketId = 0;
        this.totalElements = 0;

        int initialSize = 1 << globalDepth;
        for (int i = 0; i < initialSize; i++) {
            directory.add(new Bucket(nextBucketId++, globalDepth, bucketCapacity));
        }
    }

    public ExtendibleHashTable() {
        this(1, 4); // Default: Profundidad Global 1 (2 entradas en directorio), Capacidad 4
    }

    public int getGlobalDepth() {
        return globalDepth;
    }

    public int getBucketCapacity() {
        return bucketCapacity;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getDirectorySize() {
        return directory.size();
    }

    public List<Bucket> getDirectory() {
        return Collections.unmodifiableList(directory);
    }

    public Set<Bucket> getUniqueBuckets() {
        return new HashSet<>(directory);
    }

    public double getLoadFactor() {
        int uniqueBuckets = getUniqueBuckets().size();
        if (uniqueBuckets == 0 || bucketCapacity == 0) return 0.0;
        return (double) totalElements / (uniqueBuckets * bucketCapacity);
    }

    public int hash(String cedula) {
        return Math.abs(cedula.hashCode());
    }

    public int getDirectoryIndex(String cedula, int depth) {
        int h = hash(cedula);
        int mask = (1 << depth) - 1;
        return h & mask;
    }

    public boolean contains(String cedula) {
        return search(cedula) != null;
    }

    public User search(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }
        int index = getDirectoryIndex(cedula.trim(), globalDepth);
        Bucket bucket = directory.get(index);
        return bucket.find(cedula.trim());
    }

    public synchronized void insert(User user) {
        if (contains(user.getCedula())) {
            throw new IllegalArgumentException("Cédula duplicada: La cédula '" + user.getCedula() + "' ya existe en el sistema.");
        }
        insertInternal(user);
        totalElements++;
    }

    private void insertInternal(User user) {
        int index = getDirectoryIndex(user.getCedula(), globalDepth);
        Bucket target = directory.get(index);

        if (!target.isFull()) {
            target.add(user);
            return;
        }

        // Si el bucket está lleno y su profundidad local es igual a la profundidad global, duplicar el directorio
        if (target.getLocalDepth() == globalDepth) {
            doubleDirectory();
        }

        // Dividir el bucket en desbordamiento
        splitBucket(index, target);

        // Reintentar la inserción
        insertInternal(user);
    }

    private void doubleDirectory() {
        int oldSize = directory.size();
        globalDepth++;
        for (int i = 0; i < oldSize; i++) {
            directory.add(directory.get(i));
        }
    }

    private void splitBucket(int index, Bucket target) {
        int oldLocalDepth = target.getLocalDepth();
        int newLocalDepth = oldLocalDepth + 1;
        target.setLocalDepth(newLocalDepth);

        Bucket newBucket = new Bucket(nextBucketId++, newLocalDepth, bucketCapacity);

        // Actualizar apuntadores en el directorio
        for (int i = 0; i < directory.size(); i++) {
            if (directory.get(i) == target) {
                if (((i >> oldLocalDepth) & 1) == 1) {
                    directory.set(i, newBucket);
                }
            }
        }

        // Redistribuir registros existentes en el bucket que sufrió desbordamiento
        List<User> tempRecords = new ArrayList<>(target.getRecords());
        target.clear();

        for (User u : tempRecords) {
            int idx = getDirectoryIndex(u.getCedula(), globalDepth);
            directory.get(idx).add(u);
        }
    }

    public void printStructure() {
        System.out.println("=================================================");
        System.out.println("   ESTADO ACTUAL: EXTENDIBLE HASHING (DINÁMICO)  ");
        System.out.println("=================================================");
        System.out.printf("Profundidad Global (D): %d%n", globalDepth);
        System.out.printf("Tamaño del Directorio (2^D): %d%n", directory.size());
        System.out.printf("Buckets Únicos en Uso: %d%n", getUniqueBuckets().size());
        System.out.printf("Total de Usuarios Registrados: %d%n", totalElements);
        System.out.printf("Factor de Carga (alpha): %.2f%%%n", getLoadFactor() * 100);
        System.out.println("-------------------------------------------------");
        System.out.println("Directorio de Apuntadores [Índice -> Bucket ID (Prof. Local d)]:");
        for (int i = 0; i < directory.size(); i++) {
            Bucket b = directory.get(i);
            String binaryIndex = String.format("%" + globalDepth + "s", Integer.toBinaryString(i)).replace(' ', '0');
            System.out.printf(" Dir[%s] (Idx %d) -> %s%n", binaryIndex, i, b.toString());
        }
        System.out.println("=================================================");
    }
}
