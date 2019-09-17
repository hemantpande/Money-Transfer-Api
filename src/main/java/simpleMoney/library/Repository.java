package simpleMoney.library;

public interface Repository<T> {

    void create(Long id, T t);

    T getById(Long id);

    void update(Long id, T t);

    void delete(Long id);
}
