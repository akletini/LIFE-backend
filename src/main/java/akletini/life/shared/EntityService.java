package akletini.life.shared;

public interface EntityService<T> {
    boolean validate(T object);

    T store(T object);

    T getById(Long id);

    void delete(T object);
}
