package triatlon.persistence.repository;

import domain.Arbitru;
import domain.Proba;

public interface IRepositoryProba extends IRepository<Long, Proba> {
    public Proba getProbaByIdArbitru(Arbitru arbitru);
    Proba addProba(Proba proba);
}
