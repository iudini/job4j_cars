CREATE TABLE post (
                      id SERIAL PRIMARY KEY,
                      status BOOLEAN DEFAULT false,
                      description TEXT,
                      mark_id INT NOT NULL REFERENCES mark(id),
                      body_id INT NOT NULL REFERENCES body(id),
                      user_id INT NOT NULL REFERENCES users(id)
);

CREATE TABLE mark (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE body (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE photo (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE post_photo (
    post_id INT NOT NULL REFERENCES post(id),
    photos_id INT UNIQUE NOT NULL REFERENCES photo(id)
)