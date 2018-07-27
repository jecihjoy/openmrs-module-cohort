package org.openmrs.module.cohort.web.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortMember;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.cohort.rest.v1_0.resource.CohortRest;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + CohortRest.COHORT_NAMESPACE + "/cohortmember", supportedClass = CohortMember.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*, 1.10.*, 1.11.*", "1.12.*"})
public class CohortMemberRequestResource extends DataDelegatingCrudResource<CohortMember> {

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {

        DelegatingResourceDescription description = null;

        if (Context.isAuthenticated()) {
            description = new DelegatingResourceDescription();
            
            if (rep instanceof DefaultRepresentation) {
                description.addProperty("person", Representation.DEFAULT);
                description.addProperty("cohort");
                description.addProperty("role");
                description.addProperty("startDate");
                description.addProperty("endDate");
                description.addProperty("head");
                description.addProperty("uuid");
	            description.addSelfLink();                
            } 
            else if (rep instanceof FullRepresentation) {
                description.addProperty("person", Representation.FULL);
                description.addProperty("cohort");
                description.addProperty("role");
                description.addProperty("startDate");
                description.addProperty("endDate");
                description.addProperty("head");
                description.addProperty("uuid");
				description.addProperty("auditInfo");
	            description.addSelfLink();
	        }
        }
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("person");
        description.addRequiredProperty("cohort");
        description.addProperty("role");
        description.addProperty("startDate");
        description.addProperty("endDate");
        description.addRequiredProperty("head");
        return description;
    }
    
    @Override
    public DelegatingResourceDescription getUpdatableProperties() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("role");
        description.addProperty("startDate");
        description.addProperty("endDate");
        description.addRequiredProperty("head");
        return description;
    }

    @Override
    public CohortMember save(CohortMember cohortmember) {
    	// TODO rename API method
        return Context.getService(CohortService.class).saveCPatient(cohortmember);
    }

    @Override
    public CohortMember newDelegate() {
    	return new CohortMember();
    }
    
    @Override
    protected void delete(CohortMember cohortmember, String reason, RequestContext request) throws ResponseException {
    	// TODO rename API method
    	cohortmember.setVoidReason(reason);
        Context.getService(CohortService.class).saveCPatient(cohortmember);
    }

    @Override
    public CohortMember getByUniqueId(String uuid) {
    	// TODO rename API method
        return Context.getService(CohortService.class).getCohortMemUuid(uuid);
    }

    @Override
    public void purge(CohortMember cohortmember, RequestContext request) throws ResponseException {
        Context.getService(CohortService.class).saveCPatient(cohortmember);
    }

    @Override
    public SimpleObject search(RequestContext context) throws ResponseException {
    	//TODO refactor
        List<CohortMember> cohort = Context.getService(CohortService.class).findCohortMember(context.getRequest().getParameter("q"));
        return new NeedsPaging<CohortMember>(cohort, context).toSimpleObject(this);
    }
}
