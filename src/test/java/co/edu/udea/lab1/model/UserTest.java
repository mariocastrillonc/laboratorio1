package co.edu.udea.lab1.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Debe crear usuario con correo válido")
    void testValidEmail() {
        User user = new User("123456", "Juan Perez", "juan.perez@udea.edu.co");
        assertEquals("juan.perez@udea.edu.co", user.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el correo no tiene formato válido")
    void testInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () -> new User("123456", "Juan Perez", "correo_invalido"));
        assertThrows(IllegalArgumentException.class, () -> new User("123456", "Juan Perez", "juan@"));
        assertThrows(IllegalArgumentException.class, () -> new User("123456", "Juan Perez", "@dominio.com"));
    }
}
