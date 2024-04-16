<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<h3 align="center">Yoga</h3>

<p align="center">
School work : Testing a basic platform allowing individuals to book yoga sessions. A 80% coverage was expected.
</p>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#frontend-installation">Frontend Installation</a></li>
        <li><a href="#backend-installation">Backend Installation</a></li>
      </ul>
    </li>
    <li><a href="#usages">Usages</a></li>
    <li><a href="#swagger">Swagger</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

Testing a basic platform allowing individuals to book yoga sessions. A 80% coverage was expected.

### Built With

- Spring Boot
- Spring Security
- Lombok Annotations
- MySQL
- Spring JPA
- Jakarta Validation

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

To run the app, you will need to clone two repositories. One for the front-end and the current one for the back-end.

In case you wouldn't want to install the front-end, you could simply test the back-end using the following swagger : http://127.0.0.1:3001/swagger-ui/index.html after installing and running the back-end.

- the back end repository :

  ```
  git clone https://github.com/ask0ldd/P3-SpringV2.git
  ```

- The front end repository
  ```
   git clone https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring.git
  ```

### Prerequisites

First you need to install these softwares, packages and librairies :

- nodejs
  ```
  https://nodejs.org/en
  ```
- npm (after installing nodejs)
  ```
  npm install -g npm
  ```
- java development kit 17 (jdk17) and if needed, add a JAVA_HOME environment variable pointing at your java installation folder.
  ```
  https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
  ```
- mysql & mysqlwork bench (full install)

  ```
  https://dev.mysql.com/downloads/windows/
  ```

- the angular cli (after installing nodejs)
  ```
  npm install -g @angular/cli
  ```

- clone the current repository
  ```
  git clone https://github.com/ask0ldd/SpringAngularTesting.git
  ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Running the Frontend

1. Get into the front folder of the project

2. Install the packages needed for the front end (node & npm should be installed first)
   ```
   npm install
   ```
3. Start the Front End of the App (npm & the angular cli should be installed first)
   ```
   npm run start
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Backend Installation

1. Get into the back folder of the project

2. Install MySQL & Workbench and define a root password.

3. Create an env.properties file into the ressources folder of the project and add the following lines, with your root password replacing 'yourownrootpassword' (don't do this on a production server, create a new user with all the needed authorisations instead) :
   ```
   spring.datasource.username=root
   spring.datasource.password=yourownrootpassword
   ```
4. Open MySQL Workbench
   ```
   The following connection should already be set up :
      Local Instance MySQL80 / user : root / url : localhost:3306.
   ```
5. Create an empty "test" schema with Workbench. You don't need to do more than that since all the mandatory tables will be created by Spring JPA when executing the project.

6. Build the project.

   ```
   mvnw package
   ```

7. Run the project with Maven.
   ```
   mvnw spring-boot:run
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usages

- Register a new user account.
- Log into your account.
- View the user informations.
- Post a new yoga session, including a picture.
- Update an existing session.
- Browse all posted sessions.
- View all the details regarding a specific session.
- Delete a session.
- Subscribe / Unsubscribe from a session.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- TESTING -->

## Jest Unit & Integration Testing

## Cypress E2E Testing

## Junit Unit & Integration Testing

To train myself, I have written almost 200 tests so it may take around 20 minutes to execute them all. Please be patient.

<p align="right">(<a href="#readme-top">back to top</a>)</p>