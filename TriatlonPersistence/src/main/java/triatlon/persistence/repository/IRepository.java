package triatlon.persistence.repository;

import domain.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRepository<ID,T extends Entity<ID>> {
    void add(T elem);
    void delete(ID id);
    void update (T elem, ID id);
    T findOne (ID id);
    Iterable<T> findAll();

    T extractEntity(ResultSet resultSet) throws SQLException;
    Long generateID();

}
