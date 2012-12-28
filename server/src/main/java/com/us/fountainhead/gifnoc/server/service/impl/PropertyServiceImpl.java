package com.us.fountainhead.gifnoc.server.service.impl;

import com.us.fountainhead.gifnoc.server.entity.*;
import com.us.fountainhead.gifnoc.server.service.PropertyService;
import com.us.fountainhead.gifnoc.server.validate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    ApplicationDAO applicationDAO;

    @Autowired
    EnvironmentPropertyDAO environmentPropertyDAO;

    @PersistenceContext(unitName = "gifnocPU")
    private EntityManager em;

    /**
     * createApplication
     * @param application
     * @return Application
     * @see Application
     */
    @Override
    public Application createApplication(Application application) throws ValidationException {
        return applicationDAO.create(application);
    }

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
    public void setPropertyValue(String appName, String envName, String propertyName, String propertyValue) throws ValidationException {
        Query q = em.createQuery("select app from Application app where app.name=:appName");
        q.setParameter("appName", appName);
        List<Application> appList = q.getResultList();

        if(appList.size() == 1) {
            Application application = appList.get(0);
            Environment environment = null;
            Property property = null;
            for(Environment e : application.getEnvironmentList()) {
                if(e.getName().equals(envName)) {
                    environment = e;
                    break;
                }
            }
            if(environment == null) {
                // TO-DO throw exception
            }
            for(Property p : application.getPropertyList()) {
                if(p.getName().equals(propertyName)) {
                    property = p;
                    break;
                }
            }
            if(property == null) {
                //TO-DO throw exception
            }

            //TO-DO check if env prop already exists and just update value

            EnvironmentProperty environmentProperty = new EnvironmentProperty();
            environmentProperty.setEnvironment(environment);
            environmentProperty.setProperty(property);
            environmentProperty.setValue(propertyValue);
            environmentProperty = environmentPropertyDAO.create(environmentProperty);
        }
        else {
            //TO-DO throw exception
        }

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
            //TO-DO throw exception
        }

        return propertyValue;

    }

    /**
     * getPropertyValues
     * @param app
     * @param environment
     * @return EnvironmentProperty
     * @see String
     * @see String
     */
    @Override
    public List<EnvironmentProperty> getPropertyValues(String app, String environment) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * getSystemProperties
     * @param app
     * @return Property
     * @see String
     */
    @Override
    public List<Property> getSystemProperties(String app) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}