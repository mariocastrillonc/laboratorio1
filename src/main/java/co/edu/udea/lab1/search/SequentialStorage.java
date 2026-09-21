package co.edu.udea.lab1.search;

import co.edu.udea.lab1.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequentialStorage {
    private final List<User> users;

    public SequentialStorage() {
        this.users = new ArrayList<>();
    }

    public synchronized void insert(User user) {
        if (contains(user.getCedula())) {
            throw new IllegalArgumentException("Cédula duplicada en búsqueda secuencial: " + user.getCedula());
        }
        users.add(user);
    }

    public boolean contains(String cedula) {
        return search(cedula) != null;
    }

    public User search(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }
        String target = cedula.trim();
        for (User u : users) {
            if (u.getCedula().equals(target)) {
                return u;
            }
        }
        return null;
    }

    public int size() {
        return users.size();
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void clear() {
        users.clear();
    }
}
