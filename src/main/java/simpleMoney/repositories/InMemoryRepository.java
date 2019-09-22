package simpleMoney.repositories;

import simpleMoney.library.Repository;
import simpleMoney.library.ResponseCode;
import simpleMoney.library.exceptions.AccountException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T> implements Repository<T> {

    private final Map<Long, T> dataSource = new ConcurrentHashMap<>();

    @Override
    public void create(Long id, T t){
        if(dataSource.containsValue(t)){
            throw new AccountException(ResponseCode.DUPLICATE_ACCOUNT,"Account with same id already exists");
        }
        dataSource.putIfAbsent(id, t);
    }

    @Override
    public T getById(Long id){
        return dataSource.get(id);
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
