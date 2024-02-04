# Building Simple RESTful Services with Spring

This service use an example of order management. Each order has a orderName, orderStatus, and a unique orderId. We want to implement basic CRUD APIs.  

## 1 Basic Logic
The service would have three layers: Data Access, Service(Business Logic), API. And also it need a class representation of Order.  

## 2 Order Class
[Order.java](/src/main/java/com/example/learnSpring/Tables/Order.java) describe the class, with definition of its attributes and methods. Here are some annotations used when building the class Order.  

### 2.1 @Entity
The **@Entity** annotation is a marker annotation used in Java Persistence API (JPA) to indicate that a particular class is an **entity**. An **entity** represents a table **stored in a database**, and each instance of an entity corresponds to a row in that table.  

### 2.2 @Table
The @Table annotation provides additional configuration for the **table** in the database, such as its name, schema, and unique constraints. If @Table is not specified, the default behavior is to map the entity to a table with the same name as the entity class. Some key attributes of @Table are:  
- name: Specifies the name of the table in the database. If not specified, the table name defaults to the name of the entity class.
- schema: Used to specify the schema of the table. This is useful in databases that support schemas.
- catalog: Defines the catalog of the table, which is a way some databases organize database objects into logical groups.
- uniqueConstraints: Allows specifying custom unique constraints on columns of the table. This is represented by an array of @UniqueConstraint annotations.
- indexes: Used to define custom indexes on columns of the table. This is represented by an array of @Index annotations (available since JPA 2.1).

### 2.3 @Id and @GeneratedValue
The @Id annotation is used to designate a field in an entity class as the **primary key** of the corresponding table in the database.    
**Generation Strategies**: JPA allows for automatic generation of primary key values through the @GeneratedValue annotation, which can be specified alongside @Id. This is useful for auto-increment columns in databases, among other strategies.

## 3 Data Access
This layer would access database, including fetching records and writing into database. Service layer interacts with this layer to perform the data manipultion needed by business logic.  

### 3.1 Database connection
[application.properties](src/main/resources/application.properties) specifies how the database connection is performed.   
- **"# Database Configuration"** section denotes the address, user, password. 
- **spring.jpa.properties.hibernate.dialect** property is used to specify the Hibernate SQL dialect that Hibernate should use to communicate with the database. 
- **spring.jpa.hibernate.ddl-auto** property is a Hibernate-specific setting that controls the behavior of schema generation and modification at application startup. Some of the valid actions are:
    - **none**: No action will be performed. Hibernate will not make any changes to the database schema.
    - **validate**: Hibernate will validate that the tables and columns in the database match the entity mappings. If there's a mismatch, the application will fail to start.
    - **update**: Hibernate will update the schema based on the entity mappings. It will attempt to modify the existing schema to match the mappings without losing data.
    - **create**: Hibernate will create the schema every time the application starts, destroying any existing data.
    - **create-drop**: Similar to create, but it also drops the database schema when the SessionFactory is closed, typically when the application shuts down.

### 3.2 JPA Repository
JPA Repository, is an **interface** for generic CRUD operations on a repository for a specific type. It provides an **abstraction layer** on top of the standard JPA APIs, simplifying the data access layer implementation by reducing boilerplate code required for repository operations. In this project, [OrderRepository.java](src/main/java/com/example/learnSpring/Repositories/OrderRepository.java) implement JPARepository interface.  
- **JpaRepository<Order, Long>** indicate the Class on which the repository is built and the Id datatype of the Clsss Object.  
- **Methods** can be define by providing the name, return type, parameters like List<Order> findByOrderName(String orderName); JPA parse the method names and generate appropriate SQLs.  
- **Basic methods** are already implemented by JPA like findById(Long Id).

## 4 Service(Business Logic) Layer
This layer control the business logic of the service.  

### 4.1 @Service or @Component
@Service and @Component serve similar purposes in terms of component scanning and automatic bean registration making it available for dependency injection. It marks the class as a **service provider** that holds business logic, calculations, or calls to external APIs and databases.  

### 4.2 Dependency Injection and Bean
Dependency Injection (DI) is a design pattern used to implement Inversion of Control (IoC) for managing dependencies between objects. Here is how it works:  
- **Injection of Dependencies**: Rather than objects creating dependencies themselves (for example, instantiating services or repositories they need), these dependencies are provided ("injected") into them from the outside. This can be done through constructors, setter methods, or direct field access.
- **Containers and Providers**: A container or an IoC container is responsible for creating the objects, wiring them together, configuring them, and managing their complete lifecycle. The Spring IoC container uses DI to inject dependencies into objects when it creates them.  

### 4.3 @AutoWired
@AutoWired is used to enable automatic dependency injection. By marking a constructor, field, method, or configuration method with @Autowired, you tell Spring to inject dependencies defined in the Spring container into your beans automatically.  For example, in [OrderServices.java](src/main/java/com/example/learnSpring/Services/OrderServices.java)::OrderServices(OrderRepository repo), @AutoWired tells Spring that instance OrderRepository repo should be injected.  

## 5 API Layer
This layer genreates APIs, get HTTP requests and generate responses.  

### 5.1 @RestController
@RestController indicates that the annotated class is a controller where every method returns a domain object instead of a view.  

### 5.2 @RequestMapping(path = "/orders")
@RequestMapping(path = "/orders") indicates that all of the services are in path /orders/

### 5.3 Mapping
@GetMapping("/all") indicated that this is the API for path/all. PUT, POST and other HTTP methods perform in the same way.  

### 5.4 Return Types

#### 5.4.1 EntityModel
- Usage: Wraps a domain object and adds links to it. It's typically used for single resource representations, allowing you to enrich a single entity with hypermedia links (e.g., self-link, links to related entities).
- When to Use: Use EntityModel when returning a single resource from your API endpoint and you want to include hypermedia links with that resource.

#### 5.4.2 CollectionModel
- **Usage**: Wraps a collection of domain objects (usually entities) and adds links to the entire collection. It's useful for list resources, where each item in the list can also be accompanied by links.
- **When to Use**: Use CollectionModel when your API endpoint returns a collection of resources (e.g., a list of all orders) and you want to add hypermedia links to the collection itself, possibly including pagination links or links to related collections.

#### 5.4.3 RepresentationModel
- **Usage**: The base class for EntityModel and CollectionModel. It provides the foundation for adding links to any object, not necessarily tied to domain entities.
- **When to Use**: You might directly use RepresentationModel when you need to create a custom resource representation that doesn't fit the typical single entity or collection pattern and still want to include hypermedia links.

#### 5.4.4 ResponseEntity
- **Usage**: A wrapper for HTTP responses in Spring MVC, not specific to HATEOAS. It allows you to fully configure the HTTP response, including status codes, headers, and body. It's flexible and can be used with or without HATEOAS.
- **When to Use**: Use ResponseEntity when you need fine-grained control over the HTTP response details sent back to the client. This includes setting specific HTTP status codes, headers, or returning data without any hypermedia controls. It's also used when you want to respond with different HTTP status codes based on the outcome of the request handling (e.g., 200 OK, 404 Not Found, 201 Created).


### 5.5 Assembling
RepresentationModelAssembler interface is a part of Spring HATEOAS, a project under the larger Spring ecosystem that aims to simplify creating REST representations that follow the HATEOAS principles. This interface is designed to help easily convert domain models (entities) into RepresentationModel instances, which are enriched with hypermedia links.  

## 6 Exception Handling(@ControllerAdvice and  @ExceptionHandler({Exception}.class))
**@ControllerAdvice** is an annotation provided by the Spring Framework that allows you to handle exceptions across the whole application in one global handling component.  
**@ExceptionHandler** annotation in Spring Framework is used within a controller or controller advice class to handle exceptions that are thrown during the execution of controller methods. When you annotate a method with @ExceptionHandler and specify an exception class, such as ChangeOrderStatusException.class, you're indicating that this method should be invoked to handle exceptions of that specific type if they are thrown by any of the controller's request handling methods.

