# Spring Framework: Bean Scopes

## 1. What is Scope?
In the context of software development, **scope** refers to the lifecycle, visibility, and creation strategy of an object. It dictates how long an object lives, how many instances of it are created, and who has access to it. In the Spring Framework, scope specifically defines how the Inversion of Control (IoC) container creates and manages instances of a "Bean".

## 2. What is the Default Scope in Spring?
The default scope in Spring is **`singleton`**. If you do not explicitly define a scope for a bean, Spring will automatically create it as a singleton.

## 3. What is Singleton Scope and Other Scopes in Spring?

### Singleton Scope
When a bean is defined with the `singleton` scope, the Spring IoC container creates **exactly one instance** of that bean definition. This single instance is stored in a cache, and all subsequent requests and references for that named bean return the same cached instance. 

### Other Spring Scopes
Spring provides several other scopes, some of which are only valid in web-aware application contexts:

*   **`prototype`**: A new instance of the bean is created **every time** it is requested from the container. Use this for stateful beans.
*   **`request`**: A new bean instance is created for **every HTTP request**. This is only valid in a web-aware Spring `ApplicationContext`.
*   **`session`**: A bean instance is scoped to the lifecycle of an **HTTP Session**. Valid only in web-aware contexts.
*   **`application`**: Scopes a single bean definition to the lifecycle of a **`ServletContext`**. Valid only in web-aware contexts.
*   **`websocket`**: Scopes a single bean definition to the lifecycle of a **WebSocket**. Valid only in web-aware contexts.

## 4. What is Gang of Four (GoF) Scope vs Spring Singleton?
When we talk about the "Gang of Four" (GoF) scope, we are typically comparing the **GoF Singleton Design Pattern** to the **Spring Singleton Scope**.

While they share the name "Singleton", their implementations and scopes differ significantly:

*   **GoF Singleton**: The standard Design Pattern dictates that a class must ensure that exactly **one instance of it exists per Java ClassLoader**. It is usually implemented by making the constructor private and providing a static `getInstance()` method.
*   **Spring Singleton**: Spring's singleton dictates that exactly **one instance of a bean exists per Spring IoC container** (`ApplicationContext`). 
    *   This means if you have multiple Spring containers running in the same JVM (ClassLoader), you can have multiple instances of the same class (one per container).
    *   It also means you can define *multiple* beans of the exact same class within the *same* container, as long as they have different bean IDs. Spring will create one instance for each distinct bean definition.

In short: GoF Singleton is scoped by **ClassLoader**, while Spring Singleton is scoped by **Container/Bean Definition**.

## What are the Different Types of Dependency Injection?
In Spring, Dependency Injection (DI) is the process where the container injects dependencies into a bean, rather than the bean creating or finding them itself. There are primarily two types of DI in Spring (though field injection using `@Autowired` directly on fields also exists, it is generally discouraged):
1.  **Constructor-based Dependency Injection**
2.  **Setter-based Dependency Injection**

## What is Constructor Injection?
Constructor-based DI is accomplished when the Spring container invokes a constructor with arguments, each representing a dependency.
*   The dependencies are provided as constructor parameters.
*   It ensures that the bean is fully initialized with all its required dependencies before it is used.
*   It promotes immutability because you can declare the injected fields as `final`.

## What is Setter Injection?
Setter-based DI is accomplished by the container calling setter methods on your bean after invoking a no-argument constructor to instantiate it.
*   The dependencies are injected through standard JavaBean `set...()` methods.
*   It allows dependencies to be optional or reconfigured after creation.

## How to Choose Between Setter and Constructor Injection?
The Spring framework documentation generally advocates using **Constructor Injection** for mandatory dependencies, and **Setter Injection** for optional ones.

Here is a breakdown of when to use which:

**Use Constructor Injection when:**
*   **Dependencies are mandatory:** The class absolutely needs these dependencies to function. Constructor injection guarantees the object is never in an invalid or partially-constructed state.
*   **Immutability:** You want your dependency fields to be `final` so they cannot be accidentally changed.
*   **Testing:** It's easier to write unit tests because you simply call the constructor with mock objects, without needing any Spring context or reflection.

**Use Setter Injection when:**
*   **Dependencies are optional:** The class can still function with a reasonable default if the dependency is missing.
*   **Circular Dependencies:** If Class A needs Class B, and Class B needs Class A, constructor injection will throw an exception. Setter injection can resolve this because the objects are instantiated first, and dependencies are injected later.
*   **Many Dependencies:** If a class has many optional dependencies, a constructor with 10 arguments can be messy, whereas setters are cleaner. (Though, having that many dependencies might indicate a violation of the Single Responsibility Principle).

## What is Autowiring?
Autowiring is a feature in the Spring Framework that allows the Spring IoC container to automatically resolve and inject dependencies (collaborating beans) into your class. This means you don't have to explicitly specify the wiring; Spring automatically figures it out by inspecting the application context. In modern Spring applications, this is most commonly done using the `@Autowired` annotation.

## What are the Types (Modes) of Autowiring?
Spring defines different "modes" for autowiring (which are more explicitly seen when using XML configuration, but the concepts apply to how `@Autowired` works behind the scenes):

1.  **`no` (Default):** No autowiring is performed. You must explicitly wire bean dependencies. This is generally recommended for larger deployments for explicit clarity and control.
2.  **`byName`:** Autowiring by property name. Spring inspects the name of the property in your class and searches the container for a bean with the exact same name (bean ID) to inject.
3.  **`byType`:** Autowiring by property data type. Spring looks for a bean in the container whose type matches the type of the property. 
    *   *Note: If more than one bean of the required type is found, Spring will throw an exception (unless one is designated as `@Primary` or you use `@Qualifier`).*
4.  **`constructor`:** Similar to `byType`, but it applies to constructor arguments. Spring looks for beans matching the types of the constructor parameters and injects them.

**(Note on `@Autowired`):** When you use the `@Autowired` annotation, Spring primarily injects dependencies **byType**. If there are multiple beans of the same type, it will fall back and try to resolve the ambiguity **byName** (matching the field/parameter name).

## Common Autowiring Exceptions

### What is `NoSuchBeanDefinitionException`?
This exception is thrown by the Spring container when you try to inject a bean (or retrieve it from the ApplicationContext) that **does not exist**. 
*   **Cause:** You might have forgotten to annotate a class with `@Component` (or `@Service`, `@Repository`, etc.), missed defining it in your XML/Java configuration, or there is a typo in the bean name you are requesting.
*   **Fix:** Ensure the bean is properly defined and that Spring's component scanning is configured to find it.

### What is `NoUniqueBeanDefinitionException`?
This exception is thrown when Spring is trying to autowire a dependency **byType**, but it finds **more than one bean** of that specific type in the container.
*   **Cause:** For example, if you have an interface `PaymentService` and two implementations `CreditCardPaymentService` and `PaypalPaymentService` (both defined as beans), and you try to `@Autowired PaymentService service`, Spring won't know which implementation to choose.
*   **Fix:** You must tell Spring exactly which bean to inject. You can resolve this ambiguity using the `@Primary` or `@Qualifier` annotations.

## Resolving Autowiring Ambiguity

### What is `@Primary`?
The `@Primary` annotation is used to give **preference** to a specific bean when multiple beans of the same type exist. 
*   If you have multiple implementations of an interface, you can add `@Primary` to one of the implementation classes. 
*   When Spring encounters an ambiguous injection point, it will automatically choose the bean marked with `@Primary` as the default.

### What is `@Qualifier`?
The `@Qualifier` annotation provides fine-grained control over which exact bean should be injected when multiple beans of the same type exist. 
*   It is typically used right alongside the `@Autowired` annotation.
*   You specify the exact name of the bean you want to inject: `@Autowired @Qualifier("paypalPaymentService") PaymentService service;`.
*   **Difference from `@Primary`:** While `@Primary` sets a global "default" choice for a given type, `@Qualifier` allows you to be specific at *each individual injection point*. Note that `@Qualifier` has higher precedence than `@Primary`.

## Contexts and Dependency Injection (CDI)

### What is CDI?
**CDI (Contexts and Dependency Injection)** is the standard dependency injection framework defined by Java EE (now Jakarta EE). It provides a standard set of annotations (like `@Inject`, `@Named`, `@Singleton`) for dependency injection, making your code independent of any specific framework. 
*   Spring actually supports these standard annotations, meaning you can use `@Inject` instead of `@Autowired` in a Spring application.

### Would you recommend CDI or Spring Annotations?
**Recommendation: Use Spring Annotations** (like `@Autowired`, `@Component`, etc.) when building a Spring application.

Here is why:
1.  **Feature Richness:** Spring's native annotations are deeply integrated with the rest of the Spring ecosystem and offer more powerful features that go beyond the basic CDI standard.
2.  **Documentation & Community:** Almost all Spring documentation, StackOverflow answers, and tutorials use Spring annotations. It makes learning and debugging much easier.
3.  **The "Portability" Myth:** The main argument for using CDI (e.g., `@Inject`) is that you can easily switch your DI framework (e.g., from Spring to standard Java EE) in the future. In reality, enterprise applications almost never switch out their entire DI framework without a massive rewrite anyway. 

*Exception:* Only use CDI annotations if you are writing a shared standalone library that needs to be consumed by both Spring applications AND non-Spring Java EE applications.