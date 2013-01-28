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

    private static final String UNDEFINED_PROPERTY_FORMAT = "Invalid property [{0}] for application [{1}] environment [{2}]";
    private static final String INVALID_APP_FORMAT = "Invalid application name [{0}]";


    /**
     * setPropertyValue
     * @param appName
     * @param envName
     * @param propertyName
     * @param propertyValue
     * @see String
     * @see String
     * @see String
     * @see String
     */
    @Override
    public EnvironmentProperty setPropertyValue(String appName, String envName, String propertyName, String propertyValue) throws ValidationException {
        EnvironmentProperty ep = null;

        Query q = em.createQuery("select ep from EnvironmentProperty ep where ep.property.name=:propertyName and ep.environment.name=:envName and ep.environment.application.name=:appName");
        q.setParameter("appName", appName);
        q.setParameter("envName", envName);
        q.setParameter("propertyName", propertyName);
        List<EnvironmentProperty> environmentPropertyList = q.getResultList();

        if(environmentPropertyList.size() == 1) {
            EnvironmentProperty environmentProperty = environmentPropertyList.get(0);
            environmentProperty.setValue(propertyValue);
            ep = environmentPropertyDAO.update(environmentProperty);
        }
        else {
            MessageFormat messageFormat = new MessageFormat(INVALID_APP_FORMAT);
            String[] inserts = new String[] {propertyName, appName, envName};
            ValidationError validationError = new ValidationError(messageFormat.format(inserts));
            ValidationException validationException = new ValidationException(validationError);
            throw validationException;
        }

        return ep;
    }

    /**
     * Add a new environment and one EnvironmentProperty for each defined property
     *
     * @param applicationName
     * @param environmentName
     * @return
     * @throws ValidationException
     */
    @Override
    public Environment addEnvironment(String applicationName, String environmentName) throws ValidationException {
        Query q = em.createQuery("select a from Application a where a.name=:name");
        q.setParameter("name", applicationName);
        List<Application> applicationList = q.getResultList();
        if(applicationList.size() != 1) {
            MessageFormat messageFormat = new MessageFormat(UNDEFINED_PROPERTY_FORMAT);
            String[] inserts = new String[] {applicationName};
            ValidationError validationError = new ValidationError(messageFormat.format(inserts));
            ValidationException validationException = new ValidationException(validationError);
            throw validationException;
        }

        Application app = applicationList.get(0);

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

    @Override
    public Property addProperty(String applicationName, String propertyName) throws ValidationException {
        Query q = em.createQuery("select a from Application a where a.name=:name");
        q.setParameter("name", applicationName);
        List<Application> applicationList = q.getResultList();
        if(applicationList.size() != 1) {
            MessageFormat messageFormat = new MessageFormat(UNDEFINED_PROPERTY_FORMAT);
            String[] inserts = new String[] {applicationName};
            ValidationError validationError = new ValidationError(messageFormat.format(inserts));
            ValidationException validationException = new ValidationException(validationError);
            throw validationException;
        }

        Application app = applicationList.get(0);

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
            MessageFormat messageFormat = new MessageFormat(UNDEFINED_PROPERTY_FORMAT);
            String[] inserts = new String[] {propertyName, appName, envName};
            ValidationError validationError = new ValidationError(messageFormat.format(inserts));
            ValidationException validationException = new ValidationException(validationError);
            throw validationException;
        }

        return propertyValue;

    }

    /**
     *
     *
     * @param applicationName
     * @return
     * @throws ValidationException
     */
    @Override
    public Application addApplication(String applicationName) throws ValidationException {
        Application application = new Application();
        application.setName(applicationName);

        return applicationDAO.create(application);
    }


}