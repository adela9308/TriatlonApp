package triatlon.persistence.repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import domain.Participant;
import org.springframework.stereotype.Component;
import triatlon.persistence.repository.utils.JdbcUtils;



public class RepositoryParticipantDB implements IRepositoryParticipant {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RepositoryParticipantDB(Properties props) {
        logger.info("Initializing RepositoryParticipantDB with properties: {} ",props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Participant elem) {
        logger.traceEntry("add task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Participants (id,first_name,last_name) values (?,?,?)")){
            preSmt.setLong(1,generateID());
            preSmt.setString(2,elem.getFirstName());
            preSmt.setString(3,elem.getLastName());
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
        try(PreparedStatement preSmt=con.prepareStatement("DELETE from Participants WHERE id = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Participant elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("UPDATE Participants SET first_name = ?,last_name = ? WHERE id = ?;")){
            preSmt.setString(1,elem.getFirstName());
            preSmt.setString(2,elem.getLastName());
            preSmt.setLong(3,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Participant findOne(Long aLong) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Participant participant =null;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Participants where id=?")){
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();
                if(result.next()){
                    participant=extractEntity(result);
                }
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Participant> participants =new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Participants")){
            try(ResultSet result=preSmt.executeQuery()){
                while(result.next()){
                    Participant p=extractEntity(result);
                    participants.add(p);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public Long generateID(){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Long id=0L;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Participants")){
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
    public Participant extractEntity(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName =resultSet.getString("last_name");

        Participant p = new Participant(firstName,lastName);
        p.setId(id);
        return p;
    }


}
