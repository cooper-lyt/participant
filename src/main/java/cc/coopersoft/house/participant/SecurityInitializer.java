package cc.coopersoft.house.participant;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static org.picketlink.idm.model.basic.BasicModel.grantRole;

/**
 * Created by cooper on 21/02/2017.
 */
@Singleton
@Startup
public class SecurityInitializer {
    @Inject
    private PartitionManager partitionManager;


    @PostConstruct
    public void create() {


        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        Role seller = new Role("seller");

        Role developer = new Role("developer");
        identityManager.add(seller);
        identityManager.add(developer);


        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();


    }

}
