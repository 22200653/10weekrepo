package org.example;
import java.util.List;

public interface ICRUD<T> {
    int add(T item);
    int update(T item);
    int delete(int id);
    T getById(int id);
    List<T> getAll();
}
