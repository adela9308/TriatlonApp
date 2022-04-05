package triatlon.persistence.repository;

import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.persistence.repository.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryRezultatDB implements IRepositoryRezultat{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private IRepositoryProba repoProba;
    private IRepositoryParticipant repoParticipant;

    public RepositoryRezultatDB(Properties props, IRepositoryProba repoProba,IRepositoryParticipant repoParticipant) {
        logger.info("Initializing RepositoryRezultatDB with properties: {} ",props);
        this.dbUtils = new JdbcUtils(props);
        this.repoProba=repoProba;
        this.repoParticipant=repoParticipant;
    }

    @Override
    public void add(Rezultat elem) {
        logger.traceEntry("add task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Results (id,id_proba,id_participant,points) values (?,?,?,?)")){
            preSmt.setLong(1,generateID());
            preSmt.setLong(2,elem.getProba().getId());
            preSmt.setLong(3,elem.getParticipant().getId());
            preSmt.setFloat(4,elem.getPoints());
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
        try(PreparedStatement preSmt=con.prepareStatement("DELETE from Results WHERE id = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Rezultat elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("UPDATE Results SET id_proba = ?,id_participant = ?,points=? WHERE id = ?;")){
            preSmt.setLong(1,elem.getProba().getId());
            preSmt.setLong(2,elem.getParticipant().getId());
            preSmt.setFloat(3,elem.getPoints());
            preSmt.setLong(4,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Rezultat findOne(Long aLong) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Rezultat rezultat =null;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Results where id=?")){
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();
            if(result.next()){
                rezultat=extractEntity(result);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return rezultat;
    }

    @Override
    public Iterable<Rezultat> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Rezultat> results =new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Results")){
            try(ResultSet result=preSmt.executeQuery()){
                while(result.next()){
                    Rezultat a=extractEntity(result);
                    results.add(a);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return results;
    }


    @Override
    public List<ParticipantPointsDTO> pointsParticipant() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<ParticipantPointsDTO> list=new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select P.id,first_name,last_name,sum(points) as total from Participants P\n" +
                "left outer join Results R on P.id = R.id_participant\n" +
                "group by P.id\n" +
                "order by first_name,last_name")){
            ResultSet result=preSmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                Float points=result.getFloat("total");
                Participant p=repoParticipant.findOne(id);
                ParticipantPointsDTO dto=new ParticipantPointsDTO(p,points);
                list.add(dto);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return list;
    }
//@Override
//    public Float pointsParticipant(Participant Participant) {
//        logger.traceEntry();
//        Connection con=dbUtils.getConnection();
//        Float points=0F;
//        try(PreparedStatement preSmt=con.prepareStatement("select sum(points) as suma from Results where id_participant=?")){
//            preSmt.setLong(1,Participant.getId());
//            ResultSet result=preSmt.executeQuery();
//            if(result.next()){
//                points=result.getFloat("suma");
//            }
//        }
//        catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        logger.traceExit();
//        return points;
//    }

    @Override
    public List<Participant> participantsNotFromProba(Proba Proba) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Participant> participants =new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("SELECT  *\n" +
                "FROM    Participants P\n" +
                "WHERE   id NOT IN (SELECT id_participant FROM Results R where id_proba=?)")){
            preSmt.setLong(1,Proba.getId());
            try(ResultSet result=preSmt.executeQuery()){
                while(result.next()){
                    Long id=result.getLong("id");
                    Participant p=repoParticipant.findOne(id);
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
    public List<ParticipantPointsDTO> participantsFromProba(Proba Proba) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<ParticipantPointsDTO> list=new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select P.id,R.points from Participants P\n" +
                "inner join Results R on P.id = R.id_participant\n" +
                "where id_proba=?\n" +
                "group by P.id\n" +
                "order by points desc")){
            preSmt.setLong(1,Proba.getId());
            ResultSet result=preSmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                Float points=result.getFloat("points");
                Participant p=repoParticipant.findOne(id);
                ParticipantPointsDTO dto=new ParticipantPointsDTO(p,points);
                list.add(dto);
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return list;
    }

    @Override
    public Long generateID(){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Long id=0L;
        try(PreparedStatement preSmt=con.prepareStatement("select * from Results")){
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
    public Rezultat extractEntity(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        Long idProba = resultSet.getLong("id_proba");
        Long idParticipant =resultSet.getLong("id_participant");
        Float points =resultSet.getFloat("points");

        Proba proba=repoProba.findOne(idProba);
        Participant participant=repoParticipant.findOne(idParticipant);
        Rezultat r = new Rezultat(proba,participant,points);
        r.setId(id);
        return r;
    }
}
