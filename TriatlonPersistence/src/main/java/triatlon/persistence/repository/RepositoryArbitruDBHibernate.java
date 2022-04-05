package triatlon.persistence.repository;

import domain.Arbitru;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryArbitruDBHibernate implements IRepositoryArbitru{
    private static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
    @Override
    public void add(Arbitru elem) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(elem);
            session.getTransaction().commit();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            close();
        }

    }

    @Override
    public void delete(Long aLong) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(aLong);
            session.getTransaction().commit();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            close();
        }
    }

    @Override
    public void update(Arbitru elem, Long aLong) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(elem);
            session.getTransaction().commit();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            close();
        }
    }

    @Override
    public Arbitru extractEntity(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public Long generateID() {
        return null;
    }

    @Override
    public Arbitru findOne(Long aLong) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query=session.createQuery("from Arbitru where id=:aLong");
            query.setParameter("aLong",aLong);
            Arbitru arbitru=(Arbitru)query.uniqueResult();
            session.getTransaction().commit();
            close();
            return arbitru;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public Iterable<Arbitru> findAll() {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query=session.createQuery("from Arbitru");
            Iterable<Arbitru> arbitrii=(Iterable<Arbitru>)query.uniqueResult();
            session.getTransaction().commit();
            close();
            return arbitrii;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public Arbitru getArbitruByUsernamePassword(String username, String password) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query=session.createQuery("from Arbitru where username=:username and password=:password");
            query.setParameter("username",username);
            query.setParameter("password",password);
            Arbitru arbitru=(Arbitru)query.uniqueResult();
            session.getTransaction().commit();
            close();
            return arbitru;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }
}
