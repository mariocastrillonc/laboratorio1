package co.edu.udea.lab1.service;

import co.edu.udea.lab1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserBenchmarkServiceTest {

    private UserBenchmarkService service;

    @BeforeEach
    void setUp() {
        service = new UserBenchmarkService();
    }

    @Test
    @DisplayName("Debe registrar y realizar benchmark de búsqueda correctamente")
    void testRegisterAndBenchmark() {
        User user = new User("102030", "Carlos Ruiz", "carlos@email.com");
        service.registerUser(user);

        UserBenchmarkResult result = service.searchUserWithBenchmark("102030");

        assertNotNull(result.user());
        assertEquals("Carlos Ruiz", result.user().getNombre());
        assertTrue(result.hashingTimeNano() >= 0);
        assertTrue(result.sequentialTimeNano() >= 0);
    }

    @Test
    @DisplayName("Debe realizar carga masiva sintética correctamente")
    void testBulkLoadUsers() {
        int added = service.bulkLoadUsers(50);
        assertEquals(50, added);
        assertEquals(50, service.getTotalUsers());
    }
}
