package triatlon.persistence.repository;

import domain.Arbitru;

public interface IRepositoryArbitru extends IRepository<Long,Arbitru> {

    public Arbitru getArbitruByUsernamePassword(String username,String password);
}
