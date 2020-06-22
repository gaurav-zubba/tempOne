package com.leanix.rssfeed.main;

import com.leanix.rssfeed.application.ConsumeSurveyResource;
import com.leanix.rssfeed.application.FetchRssUrlFromLtls;
import com.leanix.rssfeed.configuration.LeanIXFPEConfiguration;
import com.leanix.rssfeed.application.LeanIXFPEResource;
import com.leanix.rssfeed.dao.FpeDao;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class LeanIXFPE extends Application<LeanIXFPEConfiguration> {


    private final HibernateBundle<LeanIXFPEConfiguration> hibernateBundle = new HibernateBundle<LeanIXFPEConfiguration>(
        com.leanix.rssfeed.core.MasterRssEntity.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(LeanIXFPEConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<LeanIXFPEConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    public static void main(String[] args) throws Exception {
        new LeanIXFPE().run("server",args[0]);
    }

    public void run(LeanIXFPEConfiguration rssPoCConfiguration, Environment environment) throws Exception {

        final FpeDao fpeDao
            = new FpeDao(hibernateBundle.getSessionFactory());

        final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);
// Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
// Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        environment.jersey().register(new LeanIXFPEResource());
        environment.jersey().register(new FetchRssUrlFromLtls(rssPoCConfiguration.getApiToken(),rssPoCConfiguration.getWorkspaceId(), fpeDao));
        environment.jersey().register(new ConsumeSurveyResource(rssPoCConfiguration.getApiToken()));
    }
}
