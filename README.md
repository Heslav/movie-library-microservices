# Movie Library Microservices

This project is a hands-on learning experience designed to explore microservices architecture through the creation of a movie library application. 
It features a suite of interconnected services that perform a variety of tasks, from managing movie data to integrating with external APIs for enriched functionalities.

## Core Services

- **Movie Service**: Serves as the backbone for CRUD operations on an H2 database and preloads sample data upon initialization.
- **IMDb Service**: Connects with the IMDb API to fetch the release year of movies, with the added feature of saving this data to the database.
- **Recommendation Service**: Utilizes the ChatGPT API to provide movie suggestions based on genre preferences.
- **API Gateway**: Acts as the central access point to route requests to the appropriate services.
- **Discovery Server**: Enables service discovery to ensure seamless communication between microservices.

## Getting Started

To run the application:

1. Input your API keys for the recommendation and IMDb services in the `docker-compose.yml` file.
2. Alternatively, if running the services outside of Docker, insert your API keys into the `application.properties` files of the respective services.
3. Obtain your OpenAI API key [here](https://platform.openai.com/api-keys).
4. Obtain your IMDb API key [here](https://rapidapi.com/Glavier/api/imdb146).

To launch the application, execute:

``
docker compose up -d
``

## Interacting with the Application

Test the application's functionality using Postman by accessing the following endpoints:

### API-Gateway Endpoints

- **GET**:
  - `http://localhost:8000/api/movies` - Retrieves a list of all movies.
  - `http://localhost:8000/api/movies/{id}` - Fetches a movie by its unique ID.
  - `http://localhost:8000/api/movies/recommendation/{genre}` - Gets a movie recommendation by genre.

- **POST**:
  - `http://localhost:8000/api/movies/imdb/{movieTitle}/{userRating}` - Adds a movie from the IMDb library with a personal rating.
  - `http://localhost:8000/api/movies` - Saves a new movie provided in the request body, e.g.:
  json { "title": "Shrek 3", "releaseYear": 2006, "userRating": 2 }

- **PUT**:
  - `http://localhost:8000/api/movies` - Updates all fields of an existing movie, e.g.:
  json { "id": 5, "title": "Nightcrawler", "releaseYear": 2014, "userRating": 1 }

- **DELETE**:
  - `http://localhost:8000/api/movies/{id}` - Removes a movie by its ID.

### IMDb-Service Endpoints

- **GET**:
  - `http://localhost:8100/api/imdb/{movieTitle}` - Provides details for a specific movie title.

### Recommendation-Service Endpoints

- **GET**:
  - `http://localhost:8200/api/recommendation/{genre}` - Offers a movie recommendation based on the specified genre.

### Movie-Service Endpoints
  - `All the same like in API-GATEWAY but with port:8000`

### Other Endpoints

- **Zipkin Tracing**: Monitor requests tracing at `localhost:9411`.
- **Discovery Server**: View service registry at `localhost:8761`.
