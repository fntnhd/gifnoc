package com.us.fountainhead.gifnoc.server.service.impl;

import com.us.fountainhead.gifnoc.server.entity.*;
import com.us.fountainhead.gifnoc.server.service.PropertyService;
import com.us.fountainhead.gifnoc.server.validate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    ApplicationDAO applicationDAO;

    @Autowired
    EnvironmentPropertyDAO environmentPropertyDAO;

    @Autowired
    EnvironmentDAO environmentDAO;

    @Autowired
    PropertyDAO propertyDAO;

    @PersistenceContext(unitName = "gifnocPU")
    private EntityManager em;

    private static final String UNDEFINED_PROPERTY_MESSAGE = "Invalid property [{0}] for application [{1}] environment [{2}]";
    private static final String INVALID_APPLICATION_MESSAGE = "Invalid application [{0}]";
    private static final String DUPLICATE_APPLICATION_MESSAGE = "Application with name [{0}] already exists";
    private static final String DUPLICATE_PROPERTY_MESSAGE = "Property with name [{0}] already exists for application [{1}]";
    private static final String DUPLICATE_ENVIRONMENT_MESSAGE = "Environment with name [{0}] already exists for application [{1}]";
    private static final String NO_ENVIRONMENT_PROPERTIES_MESSAGE = "No properties exist for environment [{0}] and application [{1}]";

    /**
     *
     * @param environment
     * @param property
     * @param propertyValue
     * @throws ValidationException
     */
    @Override
    public void setEnvironmentPropertyValue(Environment environment, Property property, String propertyValue) throws ValidationException {

        Query q = em.createQuery("select ep from EnvironmentProperty ep where ep.property.id=:propertyId and ep.environment.id=:envId");
        q.setParameter("propertyId", property.getId());
        q.setParameter("envId", environment.getId());
        List<EnvironmentProperty> environmentPropertyList = q.getResultList();

        if(environmentPropertyList.size() == 1) {
            EnvironmentProperty environmentProperty = environmentPropertyList.get(0);
            environmentProperty.setValue(propertyValue);
            environmentPropertyDAO.update(environmentProperty);
        }
        else {
            String[] inserts = new String[] {property.getName(), property.getApplication().getName(), environment.getName()};
            throw validationException(UNDEFINED_PROPERTY_MESSAGE, inserts);
        }
    }


    /**
     * Add a new environment and one EnvironmentProperty for each defined property
     *
     * @param application
     * @param environmentName
     * @return
     * @throws ValidationException
     */
    @Override
    public Environment addEnvironment(Application application, String environmentName) throws ValidationException {
        Application app = applicationDAO.findById(application.getId());

        if(app==null) {
            String[] inserts = new String[] {application.getId().toString()};
            throw validationException(INVALID_APPLICATION_MESSAGE, inserts);
        }

        for(Environment e : app.getEnvironmentList()) {
            if(e.getName().equals(environmentName)) {
                String[] inserts = new String[] {environmentName, app.getName()};
                throw validationException(DUPLICATE_ENVIRONMENT_MESSAGE, inserts);
            }
        }

        Environment e = new Environment();
        e.setName(environmentName);
        app.addEnvironment(e);
        e = environmentDAO.create(e);

        for(Property p : app.getPropertyList()) {
            EnvironmentProperty ep = new EnvironmentProperty();
            ep.setEnvironment(e);
            ep.setProperty(p);
            environmentPropertyDAO.create(ep);
        }

        return e;
    }

    /**
     *
     * @param application
     * @param propertyName
     * @return
     * @throws ValidationException
     */
    @Override
    public Property addProperty(Application application, String propertyName) throws ValidationException {
        Application app = applicationDAO.findById(application.getId());

        if(app==null) {
            String[] inserts = new String[] {application.getId().toString()};
            throw validationException(INVALID_APPLICATION_MESSAGE, inserts);
        }

        for(Property p : app.getPropertyList()) {
            if(p.getName().equals(propertyName)) {
                String[] inserts = new String[] {propertyName, app.getName()};
                throw validationException(DUPLICATE_PROPERTY_MESSAGE, inserts);
            }
        }

        Property p = new Property();
        p.setName(propertyName);
        app.addProperty(p);
        p = propertyDAO.create(p);

        for(Environment e : app.getEnvironmentList()) {
            EnvironmentProperty ep = new EnvironmentProperty();
            ep.setEnvironment(e);
            ep.setProperty(p);
            environmentPropertyDAO.create(ep);
        }

        return p;
    }


    /**
     *
     * @param applicationName
     * @return
     * @throws ValidationException
     */
    @Override
    public Application addApplication(String applicationName) throws ValidationException {
        Query q = em.createQuery("select a from Application a where a.name=:name");
        q.setParameter("name", applicationName);
        List<Application> applicationList = q.getResultList();

        if(applicationList.size() > 0) {
            String[] inserts = new String[] {applicationName};
            throw validationException(DUPLICATE_APPLICATION_MESSAGE, inserts);
        }

        Application application = new Application();
        application.setName(applicationName);

        return applicationDAO.create(application);
    }


    /**
     *
     * @param applicationName
     * @param environmentName
     * @return
     * @throws ValidationException
     */
    @Override
    public List<EnvironmentProperty> getPropertiesForEnvironment(String applicationName, String environmentName) throws ValidationException {
        Query q = em.createQuery("select ep from EnvironmentProperty ep where ep.environment.name=:environmentName and ep.environment.application.name=:applicationName");
        q.setParameter("environmentName", environmentName);
        q.setParameter("applicationName", applicationName);
        List<EnvironmentProperty> environmentPropertyList = q.getResultList();

        if(environmentPropertyList.size() == 0) {
            String[] inserts = new String[] {environmentName, applicationName};
            throw validationException(NO_ENVIRONMENT_PROPERTIES_MESSAGE, inserts);
        }

        return environmentPropertyList;

    }

    /**
     * getPropertyValue
     * @param appName
     * @param envName
     * @param propertyName
     * @return String
     * @see String
     * @see String
     * @see String
     */
    @Override
    public String getPropertyValue(String appName, String envName, String propertyName) throws ValidationException {
        String propertyValue = null;

        Query q = em.createQuery("select ep from EnvironmentProperty ep where ep.property.name=:propertyName and ep.environment.name=:envName and ep.environment.application.name=:appName");
        q.setParameter("appName", appName);
        q.setParameter("envName", envName);
        q.setParameter("propertyName", propertyName);
        List<EnvironmentProperty> environmentPropertyList = q.getResultList();

        if(environmentPropertyList.size() == 1) {
            EnvironmentProperty environmentProperty = environmentPropertyList.get(0);
            propertyValue = environmentProperty.getValue();
        }
        else {
            String[] inserts = new String[] {propertyName, appName, envName};
            throw validationException(UNDEFINED_PROPERTY_MESSAGE, inserts);
        }

        return propertyValue;

    }

    /**
     *
     * @param message
     * @param inserts
     * @return
     */
    private ValidationException validationException(String message, String[] inserts) {
        MessageFormat messageFormat = new MessageFormat(message);
        ValidationError validationError = new ValidationError(messageFormat.format(inserts));
        return new ValidationException(validationError);
    }


}