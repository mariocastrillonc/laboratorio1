package co.edu.udea.lab1.hashing;

import co.edu.udea.lab1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtendibleHashTableTest {

    private ExtendibleHashTable hashTable;

    @BeforeEach
    void setUp() {
        // Inicializar con Profundidad Global 1 y Capacidad del Bucket 2 para forzar splits rápidos
        hashTable = new ExtendibleHashTable(1, 2);
    }

    @Test
    @DisplayName("Debe insertar y encontrar usuarios correctamente")
    void testInsertAndSearch() {
        User u1 = new User("12345", "Juan Pérez", "juan@email.com");
        hashTable.insert(u1);

        User found = hashTable.search("12345");
        assertNotNull(found);
        assertEquals("Juan Pérez", found.getNombre());
        assertEquals("juan@email.com", found.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar excepción al insertar cédula duplicada")
    void testDuplicateCedulaThrowsException() {
        User u1 = new User("12345", "Juan Pérez", "juan@email.com");
        User u2 = new User("12345", "Juan Copia", "juan.copia@email.com");

        hashTable.insert(u1);
        assertThrows(IllegalArgumentException.class, () -> hashTable.insert(u2));
    }

    @Test
    @DisplayName("Debe realizar split de buckets y duplicación de directorio correctamente")
    void testBucketSplitAndDirectoryDoubling() {
        // Insertar más elementos que la capacidad inicial para provocar splits
        User u1 = new User("100", "Usuario 1", "u1@email.com");
        User u2 = new User("200", "Usuario 2", "u2@email.com");
        User u3 = new User("300", "Usuario 3", "u3@email.com");
        User u4 = new User("400", "Usuario 4", "u4@email.com");
        User u5 = new User("500", "Usuario 5", "u5@email.com");

        hashTable.insert(u1);
        hashTable.insert(u2);
        hashTable.insert(u3);
        hashTable.insert(u4);
        hashTable.insert(u5);

        assertEquals(5, hashTable.getTotalElements());
        assertNotNull(hashTable.search("100"));
        assertNotNull(hashTable.search("200"));
        assertNotNull(hashTable.search("300"));
        assertNotNull(hashTable.search("400"));
        assertNotNull(hashTable.search("500"));
        assertTrue(hashTable.getGlobalDepth() >= 1);
    }

    @Test
    @DisplayName("Debe retornar null al buscar cédula inexistente")
    void testSearchNonExistent() {
        assertNull(hashTable.search("99999999"));
    }
}
