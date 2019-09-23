package simpleMoney.repositories;

import simpleMoney.library.Repository;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.exceptions.AlreadyExistsException;
import simpleMoney.library.exceptions.NotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T> implements Repository<T> {

    private final Map<Long, T> dataSource = new ConcurrentHashMap<>();

    @Override
    public void create(Long id, T t){
        if(dataSource.containsValue(t)){
            throw new AlreadyExistsException(ResponseCode.DUPLICATE_ACCOUNT,
                    "Artifact with same id already exists");
        }
        dataSource.putIfAbsent(id, t);
    }

    @Override
    public T getById(Long id){
        T t = dataSource.get(id);
        if(t == null){
            throw new NotFoundException(ResponseCode.NOT_FOUND, "Artifact not found");
        }
        return t;
    }

    @Override
    public void update(Long id, T t){
        dataSource.replace(id, t);
    }

    @Override
    public void delete(Long id){
        dataSource.remove(id);
    }
}
