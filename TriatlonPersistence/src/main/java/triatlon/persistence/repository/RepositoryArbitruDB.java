package triatlon.persistence.repository;

import domain.Arbitru;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import triatlon.persistence.repository.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Component
public class RepositoryArbitruDB implements IRepositoryArbitru{

    private static JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static Properties newProps;
    private static int count=0;
    public RepositoryArbitruDB(Properties props) {
        logger.info("Initializing RepositoryArbitruDB with properties: {} ",props);
        if(count==0){
            newProps=props;
            count+=1;
        }
        System.out.println(newProps.getProperty("jdbc.url"));
//        System.out.println(props.getProperty("jdbc.url"));
        dbUtils = new JdbcUtils(newProps);
    }


    @Override
    public void add(Arbitru elem) {
        logger.traceEntry("add task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Arbitrii (id,username,password,name) values (?,?,?,?)")){
            preSmt.setLong(1,generateID());
            preSmt.setString(2,elem.getUsername());
            preSmt.setString(3,elem.getPasword());
            preSmt.setString(4,elem.getName());
            int result=preSmt.executeUpdate();
            logger.trace("added {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting task {} ",aLong);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("DELETE from Arbitrii WHERE id = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Arbitru elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("UPDATE Arbitrii SET username = ?,password = ?, name=? WHERE id = ?;")){
            preSmt.setString(1,elem.getUsername());
            preSmt.setString(2,elem.getPasword());
            preSmt.setString(3,elem.getName());
            preSmt.setLong(4,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Arbitru findOne(Long aLong) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Arbitru arbitru =null;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Arbitrii where id=?")){
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();
            if(result.next()){
                arbitru=extractEntity(result);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return arbitru;
    }

    @Override
    public Iterable<Arbitru> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Arbitru> arbitrii =new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Arbitrii")){
            try(ResultSet result=preSmt.executeQuery()){
                while(result.next()){
                    Arbitru a=extractEntity(result);
                    arbitrii.add(a);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return arbitrii;
    }

    @Override
    public Arbitru getArbitruByUsernamePassword(String username, String password) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Arbitru arbitru =null;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Arbitrii where username=? and password=?")){
            preSmt.setString(1,username);
            preSmt.setString(2,password);
            ResultSet result=preSmt.executeQuery();
            if(result.next()){
                arbitru=extractEntity(result);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return arbitru;
    }

    @Override
    public Long generateID(){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Long id=0L;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Arbitrii")){
            try(ResultSet result=preSmt.executeQuery()){
                while(result.next()){
                    Long idResult= result.getLong("id");
                    if(idResult>id) id=idResult;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return id+1;
    }
    @Override
    public Arbitru extractEntity(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password =resultSet.getString("password");
        String name =resultSet.getString("name");

        Arbitru a= new Arbitru(username,password,name);
        a.setId(id);
        return a;
    }
}
