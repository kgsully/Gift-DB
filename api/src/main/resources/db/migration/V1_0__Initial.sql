CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(256) NOT NULL UNIQUE,
    hashedPassword VARCHAR BINARY(255) NOT NULL,
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL
    );

INSERT INTO users (username, email, hashedPassword, createdAt, updatedAt)
  VALUES ('testuser1',
          'user@test.com',
          '$2a$10$mQTJAAy1Kw2kgh0UzCB3Yejy8g8gfYrA51K74GR.qAuH3n3aZji9m',
          '2024-12-17T18:17:51Z',
          '2024-12-17T18:17:51Z'
          );

 INSERT INTO users (username, email, hashedPassword, createdAt, updatedAt)
   VALUES ('testuser2',
           'user2@test.com',
           '$2a$10$mQTJAAy1Kw2kgh0UzCB3Yejy8g8gfYrA51K74GR.qAuH3n3aZji9m',
           '2024-12-17T18:17:51Z',
           '2024-12-17T18:17:51Z'
           );