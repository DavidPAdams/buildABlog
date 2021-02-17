# Blog Project Spring Boot

Why start with a blog app?

Because so many apps are basically just blog apps. Sites like Wikipedia, Craiglist, etc. - while not really "blogs", they have the same format.

Start a new project/app, here's a suggestion:

1. Open STS.  
2. Once open, choose **File** -> **New** -> **Spring Starter Project**
3. Fill out the fields:
   * **Name**: MyBlog
     * project name
   * **Group**: com.example
     * group name for your projects
   * **Artifact**: myBlog
     * the name of the jar file
   * **Description**: Blog Starter Project
     * a brief description
   * **Package name:** com.example.myBlog
     * java root package name
4. Select Next, and select dependencies to include: 
   - DevTools
   - JPA
   - H2
   - Web
   - Thymeleaf
5. Modify the `Application.properties` File (**src/main/resources**)
  ```bash
  spring.h2.console.enabled=true
  spring.h2.console.path=/console

  spring.datasource.platform=h2
  spring.datasource.driver-class-name=org.h2.Driver
  spring.datasource.url=jdbc:h2:mem:articleTest
  #the line above keeps the data in temporary memory
  #changing it to the following line puts data in a persisted database file
  #spring.datasource.url=jdbc:h2:file:~/springboot-h2-blogArticles
  spring.datasource.username=sa
  spring.datasource.password=
  #when changing to a persisted database, make the following line active
  #spring.jpa.hibernate.ddl-auto=update
  ```

## Blog Articles

Create packages where everything associated with the Blog Articles will live. Go into **src/main/java** -> **com.example.myBlog** and create a new package called **model**. 

In the **model** package, create an **Article** class to define the Article template. 

What kinds of attributes are needed for an Article. 

* id (Primary Key)
* title 
* author
* entry
* createdAt
* updatedAt

Add the appropriate attributes: 

```java
public class Article {
    
  private Long id; 
  private String title;
  private String author;
  private String entry;
  private Date createdAt;
  private Date updatedAt;
  
}
```

Use the source generators to have spring boot make an empty (no-argument) constructor and a constructor using fields.   

**Source -> Generate Constructors from Superclass...** and select the box "Omit call to default constructor super()" -(not needed since not using inheritance)

**Source -> Generate Constructor using fields** select all of the attributes except for 'id' and 'createdAt', Java will manage the information for those attributes.

```java
public class Article {
    
  private Long id;
  private String title;
  private String author;
  private String entry;
  private Date createdAt;
  private Date updatedAt;

  public Article() {}
  
  public Article(String title, String author, String entry) {
    this.title = title; 
    this.author = author;
    this.entry = entry;
  }

}
```

Add the getters and setters for the Article class using the source generator: 

**Source -> Generate Getters & Setters** choose all of the attributes and then uncheck 'setID(Long)', 'setCreatedAt()', and 'setUpdatedAt(), again Java will manage setting the 'id', the 'createdAt', and 'updatedAt' information on the Article objects, eliminating the potential to corrupt the data table by not having 'setId()','setCreatedAt()', and 'setUpdatedAt()' methods defined.

Generate the toString method, it could come in handy later on: 

**Source -> Generate toString** choose all of the attributes

```java
public class Article {

  private Long id;
  private String title;
  private String author;
  private String entry;
  private Date createdAt;
  private Date updatedAt;
  
  public Article() {}

  public Article(String title, String author, String entry) {
    this.title = title; 
    this.author = author;
    this.entry = entry;
  }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

    public String getEntry() {
      return entry;
    }

    public void setEntry(String entry) {
      this.entry = entry;
    }

    public Date getCreatedAt() {
      return createdAt;
    }

    public Date getUpdatedAt(){
      return updatedAt;
    }

    public Long getId() {
      return id;
    }

    @Override
    public String toString() {
      return "Article [id=" + id + ", title=" + title
          + ", author=" + author + ", entry=" + entry + "]";
    }

}
```
### Annotations

Use annotations to help java do it's job.

To make the Article 'id' set automatically by the system, the annotations @Entity, @Id, @GeneratedValue, @Column are needed. Another useful annotation is @CreationTimestamp for the 'createdAt' field.

* **@Entity** 
  Use this annotation to designate a plain old Java object (POJO) class as an entity so that JPA will work with it to create a data table.

* **@Id** 
  * JPA will recognize it as the object’s ID and primary key.

* **@GeneratedValue** 
  * Allows the database to set the value for the field. It requires a 'strategy' which will be `GenerationType.AUTO`.

* **@Column**
  * Allows the primary key (id) to be explicitly defined for the data table containing the Article objects. It is good practice to make the column name sql-friendly using snake-case instead of the usual camelCase.

* **@CreationTimestamp**
  * Allows the system to set the value of 'createdAt' exactly once when the object is saved to the database, which is preferred over trying to figure it out manually.

* **@UpdateTimestamp**
  * Allows the system to set the value of 'updatedAt' with the Virtual Machine date each time the object is changed in the database, which is preferred over trying to figure it out manually.  

Add these annotations: 
```java
@Entity
public class Article {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name = "article_id")
  private Long id;

  private String title;
  private String author;
  private String entry;

  @CreationTimestamp
  private Date createdAt;

  @UpdateTimestamp
  private Date updatedAt;
}
```

Adding the annotations to the class will tie the class to the JPA and the database. Resolve the appropriate imports: 

```java
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
```

### Article Controller

Create a controller for the Article class. This controller will determine how the data and user will move through the application.  The controller provides the connection between the templates (browser view pages) and the data from the database.

Add a **controller** package.
Add a new class: **ArticleController**

Give the controller class the **@Controller** annotation.  This will help Java understand to send the output to a template page. Create a method that will return a template page: 

```java
@Controller
public class ArticleController {
  
  @GetMapping(value = "/")
  public String index() {
    return "article/index";
  }
}
```

Add the @GetMapping annotation, this annotation will be used for the index method, which will return the template specified - a template called "index" in an **article** template directory.  Create the template.

**Create Article Templates**

Be careful, templates go in a specific area in the Spring Boot application. In the **src/main/resources** directory, the folder **templates** will hold the html files for the pages associated with the project. Add a new folder to **templates** called **article**.

Add a new file to **article** called **index.html**.  This is the template the application will return when the user first comes to the application or enters the root url.  It is the application's "home page". In index.html, build out the basic HTML structure, and add a simple heading.

```html
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>Blog Site</title>
</head>
<body>
  <h1>Welcome to My Blog Site!</h1>
  <h3>Check out my articles!</h3>
</body>
</html>
```

**Start the Server!**

Make sure all files are saved, then start the server.  Click on the main application package or the main project name, and select **Run As -> Spring Boot App**

Wait for it, waaaait for it...
Watch the console window for the status of the application run.

Open a browser to 
```bash
localhost:8080
```
The browser should display the html template page.

If there is a header error in the console that looks something like: 
```bash
java.lang.IllegalArgumentException: Request header is too large
```
Add the following to your application.properties file: 
```bash
server.max-http-header-size=10000000 
```
Now that the application is working, build out the template page.  The index template will contain a form that allows users to submit an article to the blog site.  The information entered will be stored in the database.  This is where Thymeleaf is needed.

## Thymeleaf

Thymeleaf is a server-side Java template engine for both web and standalone environments.  Thymeleaf uses "natural templating", which means HTML templates written in Thymeleaf still appear and function like HTML.

To start, modify the "index" by declaring a namespace for Thymeleaf in the html opening tag. (Thymeleaf works 'within' html.)

```html
<html xmlns:th="http://www.thymeleaf.org">
```

**Add a Form, with Thymeleaf**

The form will bring in the title, author and entry of the article. The form is set up like a standard HTML form with the addition of Thymeleaf notation. Add "th:" attributes in the tags to make the connection between the database tables in java and the html form entry fields.  

```html
<-- Add inside the <body> tag of the template --!>
<p>Please use the form below to enter an article:</p>
<form th:action="@{/}" method="post">
  <table>
    <tr>
      <td>Title</td>
      <td><input type="text" th:field="*{article.title}" /></td>
    </tr>
    <tr>
      <td>Author:</td>
      <td><input type="text" th:field="*{article.author}" /></td>
    </tr>
    <tr>
      <td>Blog Entry:</td>
    <td><input type="text" th:field="*{article.entry}" /></td>
    </tr>
    <tr>
      <td><button>Submit Article</button></td>
    </tr>
  </table>
</form>
```

The controller needs something more for this form to work. The form refers to an object called 'article', but the controller doesn't know about that yet. Modify the controller so it receives an Article object:
```java
  @GetMapping(value = "/")
  public String index(Article article){
    return "article/index";
  }
```

**Notice,** the field names reference the attributes of the Article class (fields in the database table).

**Add a Repository**

To save the info from the form to the database, create an interface that will facilitate adding data to the database.  This is called a repository: "ArticleRepository".

Add a **repository** package.
Add a new **interface**: **ArticleRepository**
Include the annotation `@Repository`, it may seem redundant or obvious, but it helps java understand how to do it's job.

```java
@Repository
public interface ArticleRepository {
}
```

Extend the functionality of the repository with Crud Repository. Don't forget to resolve the imports.
```java
@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
}
```

This will import the Spring [CrudRepository ](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)along with methods to modify data in the database. Remember CRUD: create, read, update, delete. These will be the controller actions (methods).

To add an article to the database, start in the controller. In the controller use the  `@Autowired` annotation to add the `ArticleRepository` to the controller. Add a method 'create()' to create a new article in our database, and provide some information to send back to the model (the DOM) for use in the template page.

```java
@Controller
public class ArticleController {

  @Autowired
  private ArticleRepository articleRepository;
  
  @GetMapping(value = "/")
  public String index(Article article) {
    return "article/index";
  }

  @PostMapping(value = "/")
  public String create(Article article, Model model) {
    articleRepository.save(article);
    model.addAttribute("article", article);
    return "article/show";
  }
}
```

**Note:** Resolve all the imports as necessary.

Create a `show.html` template page in the article folder. 

**Show Template**

Go to **src/main/resources** -> **templates** and then add a new file to the article folder **show.html**

Remember to include Thymeleaf and write the content that will output the attribute fields sent from the controller.
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Blog Article</title>
</head>
<body>
  <h4>Thank you for your article</h4>
  <h4>Article:</h4>
  <p th:text="${article.title}"></p>
  <p th:text="${article.author}"></p>
  <p th:text="${article.entry}"></p>
</body>
</html>
```

Start the server and submit an article through the form on the index page. After the controller receives the `POST` request from the form, it calls the `save()` then redirects to the `show.html` template where the article information is displayed. 

There is the first blog article! Amazing, isn't it beautiful! Well, it doesn't look like much now... 

Take a look in the H2 console to see that the data actually made it into the database.
Make sure that **JDBC URL:** is set to **jdbc:h2:mem:articleTest**. Once is set, click connect. 

In the white input box, write the following select statement: 
```sql
select * from article;
```

It will show the article data table. 

The app is working, but once the server stops - the data disappears!  That will be resolved later.

## Add Bootstrap

Add Bootstrap to the template pages to take advantage of their styling tools.

As with many things in programming there is more than one way to add a resource such as Bootstrap to a Spring Boot application. 

1. Relavent Bootstrap dependencies found through the Maven Repository site can be added to the `pom.xml` file, Spring Boot will import them into the application among the Maven Dependencies as webjars, which can then be included in the html template pages as necessary.

2. Bootstrap references can be added to the html pages to include resources from the CDN.

To add Bootstrap from the CDN:
1. Open a browser window and navigate to Bootstrap: `https://getbootstrap.com/docs/4.5/getting-started/introduction/`
2. Copy the CSS link tag and paste it into the html template page `<head>` section.
3. Copy the 3 JS script tags and paste them above the closing `</body>` tag. (Where all good `<script>` tags belong.)

Bootstrap is now an available resource in the template page.

Wrap the contents of the application body in a `.container-fluid`:
```html
<body>
  <div class="container-fluid">

  </div>
<!-- script tags are here -->
</body>
```

### ArticleController

Modify the ArticleController to be more CRUDdy.

Change the method for the index page to return a list of articles to the DOM through the Model object. The method becomes:

```java
  @GetMapping(value = {"/", "/articles"})
  public String index(Article article, Model model) {
    Iterable<Article> articles = articleRepository.findAll();
    model.addAttribute("articles", articles);
    return "article/index";
  }
```
The type of data returned by the CRUD method findAll() associated with the repository is an Iterable<T> data structure. While this isn't a data structure studied in the Java Basics, it works much like an ArrayList. Use the system object `Model` to carry Java data to the Document Object Model (html template page).

#### Index template page

Return to the `index.html` and add code to display the articles made available to the DOM by Java. Add code, bootstrap-style, working with Thymeleaf:
```HTML
<div class="list-group">
  <div th:each="article:${articles}">
    <div class="list-group-item list-group-item-action">
      <span th:text="${article.title}"></span> 
      By: <span th:text="${article.author}"></span>
      <span th:text="${article.entry}"></span>
    </div>
  </div>
</div>
```
Notice that Thymeleaf has a `th:each` to run an enhanced for-loop on the 'articles' Iterable object. Nice!

#### New template page

Now that the index page shows all of the blog articles, move the input form to another template page called `new.html`

Use the same head info and the same script tags at the bottom of the body from the index page. (Check that the `show.html` page also has the bootstrap CDN references, too.)

Create the `new.html` template page:
```html
<div class="container-fluid">
  <p>Please use the form below to enter a new article:</p>
  <form th:action="@{/articles/new}" method="post" class="form-group">
    <table>
      <tr>
        <td>Title:</td>
        <td>
          <input type="text" th:field="*{article.title}" class="form-control" />
        </td>
      </tr>
      <tr>
        <td>Author:</td>
        <td>
          <input type="text" th:field="*{article.author}" class="form-control" />
        </td>
      </tr>
      <tr>
        <td>Article:</td>
        <td>
          <input type="text" th:field="*{article.entry}" class="form-control" />
        </td>
      </tr>
      <tr>
        <td>Title:</td>
        <td>
          <button class="btn btn-sm btn-success">Submit Article</button>
        </td>
      </tr>
    </table>
  </form>
</div>
```

#### Back to the ArticleController

Go to the `ArticleController` and refactor (modify) the `create()` method:

```java
@PostMapping(value = "/articles/new")
public String create(Article article, Model model) {
  articleRepository.save(article);
  model.addAttribute("article", article);
  return "article/show";
}
```
Now the method responds to the path `/articles/new` which is consistent with the form action. It also calls the `save()` method from the repository to add the new article to the database, then it sends the article object back to the DOM redirecting to the `show.html` page.

There's a problem, however, because there isn't any way to navigate to the `new.html` template page to add another article to the database. Fix that in the controller; add a new get method mapped to the path for the `new.html` page:
```java
@GetMapping(value = "/articles/new")
public String getNewArticleForm(Model model) {
  model.addAttribute("article", new Article());
  return "article/new";
}
```
Notice that there is a brand-new, never-before-used, hot out of the oven article object that is sent to the DOM. This is necessary because the `new.html` page has a form that needs an article object to work with.

#### Template pages - navigation

The final piece of the trail is to add a link in the `index.html` page to navigate to the new article page:
```html
<a th:href="@{/articles/new}">
  <button class="btn btn-sm btn-primary">New Article</button>
</a>
```
The body of the `index.html` page now contains:
```html
<div class="container-fluid">
  <h1>Welcome to my fabulous blog!</h1>
  <h3>Check out the articles on my blog</h3>
  <div class="list-group">
    <div th:each="article:${articles}">
      <span th:text="${article.title}"></span> 
      By: <span th:text="${article.author}"></span>
    </div>
  </div>
  <a th:href="@{/articles/new}">
    <button class="btn btn-sm btn-primary">New Article</button>
  </a>
</div>
```
Next, add some more navigation. On the `show.html` page for the article, add links to either return 'Home' (to the index page) or go to 'Add Article' (the new page):
```html
<a th:href="@{/articles}">
  <button class="btn btn-sm btn-primary">Home</button>
</a>
<a th:href="@{/articles/new}">
  <button class="btn btn-sm btn-primary">New Article</button>
</a>
```
#### Template pages - clean up

Now that several template pages exist, check each page to ensure the bootstrap link is in the head section and the script tags are at the bottom of the body.

Review the bootstrap classes on each page and check that the styling is set up. 

What? The `show.html` template page needs styling?

Styling on the `show.html` page is left for you to do.

### ArticleController, is it CRUDdy?

Go back to the ArticleController and review the methods already developed. Are all of the CRUD methods represented? What is missing? Methods needed include: editArticle(), deleteArticle(), and getArticle(). 

Start out with the easy one, a "getArticle()" method. In order to show a specific article, the controller needs a method mapped to a path. Create a method mapped by "/articles/{articleId}" called showById():
```java
@GetMapping(value = "/articles/{articleId}")
public String showById(@PathVariable("articleId") Long articleId, Model model) {
  Optional<Article> optionArticle = articleRepository.findById(articleId);
  Article articleFound = optionArticle.get();
  model.addAttribute("article", articleFound);
  return "article/show";
}
```
This method needs to use the `articleId` included in the mapped path. This is where the annotation `@PathVariable()` is needed. It allows Java to look at the path and extract the value being passed to use in Java methods.

Because of how the CrudRepository works, the `findById()` method can return the found object or it may return `null` if the object is not found. Since it isn't possible to store `null` as an object, the modifier class `Optional<T>` is used. The extra step to use a `get()` on the optional object is needed to get the actual object.

Look this up in documentation; open a browser to Google and enter "Java CrudRepository methods" then choose the link from `docs.spring.io` `CrudRepository (Spring Data Core 2.3.1.RELEASE API)`. Review the methods, especially findById() and it's detail. In the detail for the `findById()` click on `Optional<T>` to look up info on the Optional class, including methods to use it. There's the `get()` method for extracting the object from the optional, and also a `isPresent()` method that returns a boolean which would be good to keep in mind for possible use in the future.

The showById() controller method can be used to clean up the `index.html` page which now shows all of the articles. (Rather messy, no?)

#### Refactor template pages

Refactor the index page so that the article titles and authors are still listed but change them to be clickable links to go to the `show.html` page to see the entry of the article. Modify the template page like this:
```html
<div th:each="article:${articles}">
  <a th:href="@{/articles/} + ${article.id}">
    <span th:text="${article.title}"></span> By: 
    <span th:text="${article.author}"></span>
  </a>
</div>
```
The article title and author are now a link navigating to a show page mapped by the `getArticleById()` method in the controller.

Previously, the show page had just basic html to show the article information:
```html
<h4>Article:</h4>
<p th:text="${article.title}"></p>
<p th:text="${article.author}"></p>
<p th:text="${article.entry}"></p>
``` 
This page also needs to have links to navigate to other pages, so include them:
```html
<a th:href="@{/articles}">
  <button class="btn btn-sm btn-primary">Home</button>
</a>
<a th:href="@{/articles/new}">
  <button class="btn btn-sm btn-primary">New Article</button>
</a>
```
### Is it CRUDdy now?

What about other CRUD methods? The remaining methods needed are editArticle() and deleteArticle(). The development steps for these are similar:
1. Where will the action be triggered? Consider placing a link in the `show.html` page that maps the path for the method.
2. Build the controller method with the appropriate repository method calls, attributes, and page redirection (as applicable).
3. Create the template page if necessary or redirect to another appropriate controller method.

##### Delete method

Add a link to the `show.html` page to trigger a delete action for an article. Add the following link to the template page:
```html
<a th:href="@{/articles/} + ${article.articleId} + @{/delete}" th:method="delete">
  <button class="btn btn-danger">Delete</button>
</a>
```

The Delete method of CRUD doesn't require a new template page. Create the controller method:
```java
@GetMapping(value = "/articles/{articleId}/delete")
public String deleteArticle(@PathVariable("articleId") Long articleId) {
  articleRepository.deleteById(articleId);
  return "redirect:/articles";
}
```
The mapped path is a common construction using the namespace (/articles) along with the resource identifier (/{articleId}) and the CRUD action (/delete). While this isn't strictly required, and the method could be mapped in other ways, this construction is encouraged as a 'best practice' of the industry.

Also, the annotation type for the mapping looks wrong. However, due to limitations of Java and HTML, we make do with a GET request for delete.

Notice the return statement. It is different than other controller methods because the method doesn't need to go to it's own template page. In fact, since the article would be deleted, there would not be anything left of it to show. The logical next step would be routing the browser to the index method using a redirect command to the "/articles" path, which happens to map to the index() method that renders the `index.html` page.

##### Edit Article method

Add a link to the `show.html` page to trigger an edit action for an article. Add the following link to the template page:
```html
<a th:href="@{/articles/} + ${article.articleId} + @{/edit}" th:method="get">
  <button class="btn btn-warning">Edit Article</button>
</a>
```

The above link indicated a GET method. Therefore the controller needs to have a GET method to direct the browser to an edit page where there needs to be a form to display the current information and allow the user to enter changes. Finally, the form will need to have a 'Submit' button to package the information and send it to the controller for a PUT mapped method. First the GET controller method for the edit page:

Create the `getEditForm()` method in the controller:
```java
@GetMapping(value = "/articles/{articleId}/edit")
public String getEditForm(@PathVariable("articleId") Long articleId, Model model) {
  Optional<Article> optionArticle = articleRepository.findById(articleId);
  Article articleFound = optionArticle.get();
  model.addAttribute("article", articleFound);
  return "article/edit";
}
```

This method looks a lot like the `showById()` controller method but it's mapped path and return statement are different, so it is a different method.

The `edit.html` template page is required. Create it in the templates/articles folder. It will look much like the new article page, but it will start out with information and receive changes.

Since the `edit.html` page is similar to the `new.html` page, it is left for you to build along with its styling. Since html forms don't really support a PUT method natively, use Thymeleaf inside the form tag with an attribute like this: `th:method="put"` Also, the `th:action=` path needs to be a little different, try to figure it out.

The controller method needed to receive the changes and update the article needs to be built next. Create a new method called `updateArticle()`:
```java
@PostMapping(value = "/articles/{articleId}/edit")
public String updateArticle(@PathVariable("articleId") Long articleId, Article article, Model model) {
  Optional<Article> optionArticle = articleRepository.findById(articleId);
  Article updateArticle = optionArticle.get();
  updateArticle.setTitle(article.getTitle());
  updateArticle.setAuthor(article.getAuthor());
  updateArticle.setEntry(article.getEntry());
  articleRepository.save(updateArticle);
  model.addAttribute("article", updateArticle);
  return "article/show";
}
```

This method receives the `articleId` as a path variable, the modified article, and an instance of the DOM system model as arguments / parameters. The article for edit is retrieved from the repository as an Optional. The updateArticle is set as the return from Optional's `get()` method. The information in the `updateArticle` fields are changed to the values received from the form. The updated article is saved back into the repository with the same articleId, effectively replacing the article (PUT action). The updatedArticle is added to the DOM model sent back to the template page, as indicated by the return statement "article/show". The result is that the updated article is rendered on the `show.html` page.

Again, the mapping annotation looks wrong, it should be a PUT request, but due to limitations of Java and HTML, we will make do witha POST request.

Review the methods in the controller for CRUDdiness. The `create()` method corresponds to 'C', the `index()`, and `showById()` methods corresponds to 'R', the `updateArticle()` method corresponds to 'U', and the `deleteArticle()` method corresponds to 'D'. The `ArticleController` is sufficiently CRUDdy.

But what of the 'extra' methods marked with `@GetMapping`? These are necessary routes to navigate to the correct template page for displaying a form to receive input from the user.

Now that the application is nice and CRUDdy, template pages supporting the usual actions are present, and things are mapped nicely, make changes to the `application.properties` file so the data will persisted in a database from this point forward:

```bash
spring.h2.console.enabled=true
spring.h2.console.path=/console

spring.datasource.platform=h2
spring.datasource.driver-class-name=org.h2.Driver
#spring.datasource.url=jdbc:h2:mem:articleTest
#the line above keeps the data in temporary
#changing it to the following line puts data in a persisted database file
spring.datasource.url=jdbc:h2:file:~/myblogArticles
spring.datasource.username=sa
spring.datasource.password=
#when changing to a persisted database, make the following line active
spring.jpa.hibernate.ddl-auto=update
```

#### `show.html` page styling

Styling on the `show.html` page is relatively easy with Bootstrap classes. One of the better choices is their `.card` component classes. Looking at the documentation on the Bootstrap site, choose a Card layout that looks good, then copy and adapt the code as necessary. Here's an example:
```html
<div class="card text-center">
  <div class="card-header">
    Featured
  </div>
  <div class="card-body">
    <h5 class="card-title">Special title treatment</h5>
    <p class="card-text">With supporting text below as a natural lead-in to additional content.</p>
    <a href="#" class="btn btn-primary">Go somewhere</a>
  </div>
  <div class="card-footer text-muted">
    2 days ago
  </div>
</div>
```
Then adapt it to suit the `show.html` page:
```html
  <div class="container-fluid">
    <h1>The Article Says:</h1>
    <div class="row">
      <div class="col-md-12">
        <div class="card text-primary bg-light border-info mb-3">
          <div class="card-header text-dark" th:object="${article}">
            <h3 th:text="${article.title}"></h3>
          </div>
          <div class="card-body">
            <strong>By: <span th:text="${article.author}"></span></strong>
            <p class="card-text" th:text="${article.entry}" />
          </div>
          <div class="card-footer">
            <div class="text-right">
              <a th:href="@{/articles/} + ${article.id} + @{/edit}" th:method="get">
            <button class="btn btn-warning">Edit Article</button>
          </a>
          <a th:href="@{/articles/} + ${article.id} + @{/delete}" th:method="delete">
            <button class="btn btn-danger">Delete</button>
          </a>
        </div>
      </div>
    </div>
  </div>
```

#### The `edit.html` page

Once the `edit.html` page is built, it seems to look familiar. That's right, it looks very much like the `new.html` page with nearly the same form. Here's how it could turn out:
```html
<div class="container">
  <p>Use this form to edit an article:</p>
  <form th:action="@{/articles/} + ${article.id} + @{/edit}" 
     th:method="put" class="form-group">
    <table>
      <tr>
        <td>Title:</td>
        <td>
          <input type="text" th:field="*{article.title}" th:placeholder="${article.title}" class="form-control"/>
        </td>
      </tr>
      <tr>
        <td>Author:</td>
        <td>
          <input type="text" th:field="*{article.author}" th:placeholder="${article.author}" class="form-control"/>
        </td>
      </tr>
      <tr>
        <td>Article:</td>
        <td>
          <input type="text" th:field="*{article.entry}" th:placeholder="${article.entry}" class="form-control"/>
        </td>
      </tr>
      <tr>
        <td>Title:</td>
        <td>
          <button class="btn btn-sm btn-success">Submit Article</button>
        </td>
      </tr>
    </table>
  </form>
</div>
```

#### Review

Last time it was all about making sure the controller is CRUDdy:
 - There is the GET and POST request pair associated with the `new.html` page receiving information from the end-user used to ultimately **Create** a new record.
 - There are a couple of GET requests to **Read** record(s) from the database.
 - A GET and POST request pair to **Update** a record that exists in the database.
 - And a GET request to **Delete** a record from the database.
Yep, nice and CRUDdy!

Thymeleaf is being used to help HTML and Java play nice with each other. Thymeleaf facilitates communication of data to and from the html template pages. With Thymeleaf attributes embedded in the html along with the proper syntax helps to make data passed to the page from the controller accessible; likewise it helps with packaging form data going into the controller so it can be understood and handled by Java. Nice tool.

### The template pages are not DRY, or What are Fragments?

There is a problem at this point, each template page repeats the same code. Specifically, the link tag in the head for Bootstrap along with the three script tags near the closing body tag appear on each of the template pages. 

**NOT GOOD!**

Here's part of the problem, if the information in any of these items needs to be updated someday in the future, another developer will have to be assigned to find all of the places where it occurs to change the information. With only a few pages that may not be so bad, but in a production environment for a commercial website, that could be quite a tedious task taking a lot of time. Hopefully, that other developer won't be a borderline sociopath where this event would trigger their psychosis into becoming a serial killer who knows your address.

In order to sleep at night and not be worried going out to the parking lot alone, make the code DRY. Consolidate the repeated code. Fortunately, Thymeleaf has a relatively easy way to do this. It's called **Fragments**. Use fragments to hold the code and add a reference to the fragment in the html page where it is required.

A **fragment** needs to live in it's own fragments folder next to the pages where it will be used. Create a new folder inside the `/templates/article` folder called `/fragments`. Then inside the 'fragments' folder add new html files to hold the repeated information in the head and script sections, then add a reference line in the template page to access the fragment code.

In the head of each page, there is a link tag for `bootstrapcdn` that needs to be placed in a fragment. Add a new file in `/fragments` called `headLinks.html` and place your bootstrap link in it wrapped in a special div:
```html
<div th:fragment="headLinks">
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
</div>
```
On each html template page replace the link in the head with:
```html
<link th:replace="article/fragments/headLinks">
```
Now even if there are more links needed, or changes need to be made, those things will happen in only one place, the fragment.

Do a similar link for the script tags, too. Add your scripts to a html file called `scripts.html`:
```html
<div th:fragment="scripts">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous">
  </script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous">
  </script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous">
  </script>
</div>
```
On each html template page replace the script tags with:
```html
<script th:replace="article/fragments/scripts"></script>
```
That's better, nice and DRY.

### Navbar

This blog needs a navbar to hold navigation links so they are available on every page. However, there's no need to add a navbar on each page, use a fragment. Add a file to the fragments folder called `navbar.html` and place navbar code from bootstrap in it.
```html
<div th:fragment="navbar">
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">Navbar</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
      <div class="navbar-nav">
        <a class="nav-item nav-link active" href="#">Home <span class="sr-only">(current)</span></a>
        <a class="nav-item nav-link" href="#">Features</a>
        <a class="nav-item nav-link" href="#">Pricing</a>
        <a class="nav-item nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
      </div>
    </div>
  </nav>
</div>
```
On each html page immediately after the `<body>` tag add:
```html
<div th:replace="article/fragments/navbar"></div>
``` 
Be sure to include the closing tag at the end of the line. Now the navbar will automatically be available on every page, and the code will be in one place, easy to maintain.

Time to customize the navbar code:
```html
<div th:fragment="navbar">
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">My Fab Blog</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
      data-target="#navbarNavAltMarkup"
      aria-controls="navbarNavAltMarkup" aria-expanded="false"
      aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
      <div class="navbar-nav">
        <a class="nav-item nav-link active" href="/">Home</a>
        <a class="nav-item nav-link active"
          href="/articles/new">New Article</a> 
        <a class="nav-item nav-link" href="#"></a>
      </div>
    </div>
  </nav>
</div>
```

### More Fragment Fun

Fragments are so much fun, but wait, maybe there's room for one more. The form fields for a new article and for editing an article look way to much alike to be DRY. Work on extracting the repeated code to make another fragment called `formFields.html`.
```html
<div th:fragment="formFields">
  <table class="table">
    <tr>
      <td>Title</td>
      <td><input class="form-control" type="text" autofocus
        th:placeholder="${article.title}" th:field="*{article.title}" /></td>
    </tr>
    <tr>
      <td>Author</td>
      <td><input class="form-control" type="text"
        th:placeholder="${article.author}" th:field="*{article.author}" /></td>
    </tr>
    <tr>
      <td>Entry</td>
      <td><textarea rows="10" class="form-control" style="height: 20em;"
        th:placeholder="${article.entry}" th:field="*{article.entry}"></textarea></td>
    </tr>
  </table>
</div>
```
Now replace the extracted code from both the `new.html` and the `edit.html` template pages with this:
```html
<div th:replace="article/fragments/formFields"></div>
```
The only things left on those forms are the form-tag attributes and the button tag. So much DRYer.

### Add some custom CSS

While making things prettier, add the capability for some custom CSS. In the `/static` folder add another folder called `/stylesheets`. That will be a good place to hold any CSS stylesheet files needed to make the blog look marvelous. And for good measure, add a file called `custom.css`.

Try this CSS in the `custom.css` file:
```css
body{
  background-color: #b3cccc;
}

.container{
  margin-top: 20px;
  padding-top: 10px;
  padding-bottom: 10px;
  border-radius: 5px; 
}
```
Now add a reference link in the headLinks fragment to make it apply to each page. Make sure you use a relative path to get from the fragments folder to the location where Spring Boot needs to look for the stylesheet:
```html
<link rel="stylesheet" type="text/css" href="../../stylesheets/custom.css">
```
### Clean up

Almost done, there is a button that is no longer needed on the index page. That lovely `New Article` button has gotta go. Why? There is a new article link on the navbar, so it isn't needed anymore. Delete that bad boy. Now over to the show page to remove the redundant buttons for `home` and `New Article` for the same reason.

What a nice DRY and stylish blog! Congratulate yourself and enjoy. Double check that all of the fragments are referenced on all of the pages, check the styling and adjust if desired, and show it off to your friends and family.

