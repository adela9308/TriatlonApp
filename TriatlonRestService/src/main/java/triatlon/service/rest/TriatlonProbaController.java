package triatlon.service.rest;

import domain.Arbitru;
import domain.Proba;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triatlon.persistence.repository.RepositoryArbitruDB;
import triatlon.persistence.repository.RepositoryProbaDB;

@RestController
@RequestMapping("/triatlon/probe")
@CrossOrigin
public class TriatlonProbaController {
    private static final String template = "Hello, %s!";

    ApplicationContext factory= new ClassPathXmlApplicationContext("classpath:RestServiceConfig.xml");
    @Autowired
    private RepositoryArbitruDB repoArbitru = (RepositoryArbitruDB) factory.getBean("repoArbitruDB");
    @Autowired
    private RepositoryProbaDB repoProba = (RepositoryProbaDB) factory.getBean("repoProbaDB");

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }
    //GET ALL
    @RequestMapping( method= RequestMethod.GET)
    public Iterable<Proba> getAll(){
        System.out.println("GetAll proba ... ");
        return repoProba.findAll();
    }
    //GET BY ID
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("FindOne proba ... ");
        Proba proba=repoProba.findOne(Long.parseLong(id));
        if (proba==null)
            return new ResponseEntity<String>("Proba with this ID not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Proba>(proba, HttpStatus.OK);
    }
    //DELETE
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting proba ... ");
        try {
            repoProba.delete(Long.parseLong(id));
            return new ResponseEntity<Proba>(HttpStatus.OK);
        }catch (Exception ex){
            System.out.println("Ctrl Delete proba exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    //ADD
    @RequestMapping(method = RequestMethod.POST)
    public Proba create(@RequestBody Proba proba){
        System.out.println("Adding proba ... ");
        Proba p=repoProba.addProba(proba);
        return p;

    }
    //UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Proba update(@RequestBody Proba proba) {
        System.out.println("Updating proba ...");
        repoProba.update(proba,proba.getId());
        return proba;

    }
    //GET BY ID ARBITRU
    @RequestMapping(value="/proba_arb/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> probaByArbitru(@PathVariable String  id){
        System.out.println("GetProbaByArbitru ...");
        Arbitru arbitru=repoArbitru.findOne(Long.parseLong(id));
        Proba result=repoProba.getProbaByIdArbitru(arbitru);
        if (result==null)
            return new ResponseEntity<String>("Proba with this ArbitruId not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Proba>(result, HttpStatus.OK);
    }

}
