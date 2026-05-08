# Day 9: Spring MVC Q&A

## 1. What is a simple flow in Spring MVC?
The Spring MVC flow outlines how a web request is processed from the moment a user initiates it until a response is sent back:
1. **Client Request**: A client (like a web browser) sends an HTTP request to the web application.
2. **DispatcherServlet**: The request is intercepted by the `DispatcherServlet`, which acts as the Front Controller for the application.
3. **HandlerMapping**: The `DispatcherServlet` consults one or more `HandlerMapping`s to identify the appropriate Controller to handle the request based on the URL.
4. **Controller Execution**: The `DispatcherServlet` routes the request to the chosen `Controller`. The Controller executes its business logic and typically updates the `Model`.
5. **ModelAndView Return**: The Controller returns a `ModelAndView` object to the `DispatcherServlet`. This object contains the model data and the logical name of the view to be rendered.
6. **ViewResolver**: The `DispatcherServlet` asks the `ViewResolver` to map the logical view name to a specific view implementation (like a JSP page or Thymeleaf template).
7. **View Rendering**: The chosen View is rendered using the data provided in the Model.
8. **Response**: The final rendered output (usually HTML) is sent back to the client as an HTTP response.

## 2. What is a Model in Spring MVC?
In Spring MVC, the **Model** represents the data or the information of your application.
- It acts as a container for the data that needs to be displayed to the user or processed by the application.
- The Controller interacts with business logic (services) to retrieve or update data, and then populates the Model with this data.
- The View then uses the Model to render the final output. The Model is essentially a `Map` of key-value pairs where the key is the attribute name and the value is the actual data object.
- Spring provides several ways to pass data to the view, such as using the `Model` interface, `ModelMap` class, or `ModelAndView` class.

## 3. What is a DispatcherServlet?
The `DispatcherServlet` is the core component of Spring MVC.
- **Front Controller Pattern**: It implements the Front Controller design pattern. This means it's a central servlet that receives all incoming HTTP requests for your application and delegates them to other components for actual processing.
- **Request Routing**: Its primary job is to orchestrate the request handling flow. It figures out which Controller should handle a specific request, which View should be used to render the response, and manages the entire lifecycle of a request within the Spring MVC framework.
- **Integration**: It integrates seamlessly with the Spring IoC container, allowing you to easily use all of Spring's features (like dependency injection) within your web layer.

## 4. How is validation done in Spring MVC?
Validation in Spring MVC is typically done using the **Bean Validation API (JSR-380)**, most commonly implemented by Hibernate Validator. 
1. **Add Dependencies**: Ensure you have the `spring-boot-starter-validation` dependency (if using Spring Boot) or `hibernate-validator` in a traditional Spring setup.
2. **Annotate the Model**: Add validation constraints directly to the fields of your model/DTO classes using annotations such as `@NotNull`, `@Size`, `@Min`, `@Max`, `@Email`, `@NotBlank`, etc.
3. **Trigger Validation in Controller**: In your Controller's handler method, use the `@Valid` (from `jakarta.validation` / `javax.validation`) or `@Validated` (from Spring) annotation next to the model object parameter.
4. **Capture Validation Results**: Immediately following the `@Valid` parameter, declare a `BindingResult` or `Errors` parameter. Spring MVC will populate this object with the results of the validation.

**Example:**
```java
@PostMapping("/register")
public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "registrationForm"; // Return to the form view to show errors
    }
    // Proceed with business logic
    return "success";
}
```

## 5. How to map validation results to your view?
Once validation errors are captured in the `BindingResult` object in the controller, Spring automatically makes them available to the view layer.
- **JSP**: You can use the Spring form tag library (`<form:errors>`) to display errors next to the relevant input fields.
  ```jsp
  <form:input path="username" />
  <form:errors path="username" cssClass="error-text" />
  ```
- **Thymeleaf**: You can use the `th:errors` attribute or the `#fields` utility object to check for and display errors.
  ```html
  <input type="text" th:field="*{username}" />
  <span th:if="${#fields.hasErrors('username')}" th:errors="*{username}">Name Error</span>
  ```
Spring handles the binding automatically—as long as the `path` or `field` matches the property name in your model, the corresponding validation message will be rendered.

## 6. How to implement common logic for controllers in Spring MVC?
If you have logic that needs to be applied across multiple controllers (e.g., exception handling, data binding, or model attribute population), you can use **Controller Advice**. Instead of repeating the same logic inside each individual controller, Spring provides a mechanism to define this logic globally.
Other common ways include using **Interceptors** (for pre/post-processing of requests) or **Filters** (at the servlet level), but for controller-specific cross-cutting concerns, Controller Advice is the standard and most powerful approach.

## 7. What is Controller Advice?
`@ControllerAdvice` (and its REST counterpart `@RestControllerAdvice`) is an annotation in Spring MVC that allows you to write global code that applies to a wide range of controllers.
- It acts as an interceptor specifically for controller methods.
- By default, the methods inside a class annotated with `@ControllerAdvice` apply globally to all `@RequestMapping` methods.
- You can restrict the scope of a `@ControllerAdvice` to specific packages, specific classes, or controllers carrying specific annotations.
- It is most commonly used for **global exception handling**, but it can also be used for global data binding (using `@InitBinder`) or globally populating the model (using `@ModelAttribute`).

## 8. What is @ExceptionHandler?
`@ExceptionHandler` is an annotation used to handle specific exceptions that are thrown during the execution of a controller method.
- **Local Handling**: If you place `@ExceptionHandler` on a method inside a specific `@Controller` class, it will only handle exceptions thrown by methods within that specific controller.
- **Global Handling**: If you place `@ExceptionHandler` methods inside a class annotated with `@ControllerAdvice`, those methods will handle exceptions thrown by **any** controller across the entire application.
- When an exception occurs, Spring looks for an `@ExceptionHandler` method that matches the exception type and executes it. This allows you to gracefully return a custom error view or a specific structured JSON response instead of a default stack trace or generic server error page.

**Example of Global Exception Handling:**
```java
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles specific exceptions globally
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/user-not-found"; // Returns a custom error view
    }

    // Fallback for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/general";
    }
}
```
