package co.edu.udea.lab1.hashing;

import co.edu.udea.lab1.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bucket {
    private final int id;
    private final int capacity;
    private int localDepth;
    private final List<User> records;

    public Bucket(int id, int localDepth, int capacity) {
        this.id = id;
        this.localDepth = localDepth;
        this.capacity = capacity;
        this.records = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(int localDepth) {
        this.localDepth = localDepth;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<User> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public boolean isFull() {
        return records.size() >= capacity;
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public int size() {
        return records.size();
    }

    public boolean add(User user) {
        if (isFull()) {
            return false;
        }
        records.add(user);
        return true;
    }

    public User find(String cedula) {
        for (User u : records) {
            if (u.getCedula().equals(cedula)) {
                return u;
            }
        }
        return null;
    }

    public boolean remove(String cedula) {
        return records.removeIf(u -> u.getCedula().equals(cedula));
    }

    public void clear() {
        records.clear();
    }

    @Override
    public String toString() {
        return String.format("Bucket #%d [Local Depth d=%d, Elements=%d/%d]", id, localDepth, records.size(), capacity);
    }
}
