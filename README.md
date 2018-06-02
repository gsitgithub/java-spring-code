# Spring code samples
This repository contains java applications(including webapps) code and samples.

# More examples
 - https://github.com/kolorobot/spring-mvc-quickstart-archetype 
 - https://subversion.assembla.com/svn/spring-generic-dao-jpa-example 
 - https://github.com/openshift/spring-eap6-quickstart
 - https://github.com/springinpractice/sip07 
 - https://github.com/gordonad/enterprise-spring-best-practices 
### Github Project Names
 - ssng-project-master
 - Cerberus-stateless-master
 - greenhouse-master
 - infinit-group-cvdb-14416f9af6da
 - spring-mysql-mongo-rabbit-integration-master
 - spring-resource-handling-master
 - springside4-master StudentEnrollmentWithSpring-1.6 
### Utils/Libraries
 - Fonts: Source Sans Pro and Merriweather (https://18f.gsa.gov/2017/10/03/building-a-large-scale-design-system/)
 - http://fastutil.di.unimi.it/
 - https://github.com/NIT-Software/Modern-Ecommerce

### <a name="architecture"></a>Software Architecture (https://github.com/colinbut/monolith-enterprise-application)

Instead of using a Layered Architecture where you commonly have 3 layers with one directional flow, this image showcase
a Hexagonal Architecture (Ports and Adapters). The core domain comprises of the main business logic would be the inner and
the application infrastructure (Database, Message Queues, REST endpoints) would be the outer layers. 

![Image of a Hexagonal Architecture](https://github.com/colinbut/monolith-enterprise-application/blob/master/etc/HexagonalArchitecture.png)  

This is how the system components fit together:

![Image of System Components](https://github.com/colinbut/monolith-enterprise-application/blob/master/etc/SystemComponents.png)

### <a name="db-design"></a>Database Design

![Image of ER diagram](https://github.com/colinbut/monolith-enterprise-application/blob/master/etc/entity-relationship.png)

Database table structure:

![Image of Table Diagram](https://github.com/colinbut/monolith-enterprise-application/blob/master/etc/relation-table-schema.png)

### <a name="data-access"></a>Data Access

Rather than having a logical Data Access Layer within a 3 layered architecture, in a hexagonal architecture data access are
in an outer layer. 

### Deploy https://blogs.agilefaqs.com/2009/11/24/towards-continuous-deployment-zero-downtime-webapp-deployment/
