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

## 5. What are the Different Types of Dependency Injection?
In Spring, Dependency Injection (DI) is the process where the container injects dependencies into a bean, rather than the bean creating or finding them itself. There are primarily two types of DI in Spring (though field injection using `@Autowired` directly on fields also exists, it is generally discouraged):
1.  **Constructor-based Dependency Injection**
2.  **Setter-based Dependency Injection**

## 6. What is Constructor Injection?
Constructor-based DI is accomplished when the Spring container invokes a constructor with arguments, each representing a dependency.
*   The dependencies are provided as constructor parameters.
*   It ensures that the bean is fully initialized with all its required dependencies before it is used.
*   It promotes immutability because you can declare the injected fields as `final`.

## 7. What is Setter Injection?
Setter-based DI is accomplished by the container calling setter methods on your bean after invoking a no-argument constructor to instantiate it.
*   The dependencies are injected through standard JavaBean `set...()` methods.
*   It allows dependencies to be optional or reconfigured after creation.

## 8. How to Choose Between Setter and Constructor Injection?
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
