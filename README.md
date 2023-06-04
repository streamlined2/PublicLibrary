# PublicLibrary

<h1>The project models business processes of public library</h1>

<h3>Applied technologies</h3>
<ul>
<li>Java 20</li>
<li>Spring Boot 3.1.0</li>
<li>Spring MVC, Thymeleaf 3.1.1</li>
<li>Spring Security</li>
<li>Spring Data, JPA, Hibernate 6.1.7</li>
<li>Postgresql 15, Liquibase 4.17.2</li>
<li>Spring Email, Kafka</li>
<li>JUnit 5, Mockito, MockMVC</li>
<li>Tomcat, Lombok, Log4j2, Maven, Git</li>
<li>Spring Tools Suite 4.17.2</li>
</ul>

<h3>Use cases and features</h3>
<i>
<ul>
<li>user changes interface locale</li>
<li>user logs in and gets authorized</li>
<li>librarian registers new customer, customer receives notification by email</li>
<li>customer browses and reads book reviews</li>
<li>customer selects books to order
		<ul>
		<li>books filtered out by author, title, ISBN, language, country of origin, cover, etc</li>
		<li>books sorted by author, title, ISBN, language, country, year of publication in ascending or descending order</li>
		<li>book list divided in pages, first, last, next, previous page may be retrieved</li>
		</ul>
</li>
<li>customer marks selected books and forms request, notification sent by email</li>
<li>librarian reviews request, checks if books available, approves or rejects requested books, notification sent</li>
<li>librarian transfers approved books to customer, notification sent</li>
<li>librarian fetches list of customer's books</li>
<li>librarian finds holder of the book</li>
<li>customer returns books, librarian checks for damage and writes out compensation claim, notification sent</li>
<li>customer composes and publishes book review</li>
<li>librarian adds and edits book descriptions</li>
<li>librarian retrieves data on most popular book categories by time, request rate</li>
<li>librarian retrieves statistical data about customers</li>
<li>librarian retrieves data about selected customer on time spent, request rate, reviews</li>
<li>manager registers new librarian or manager</li>
<li>manager validates claims and authorizes compensation sum</li>
<li>user changes personal data</li>
</ul>
</i>
